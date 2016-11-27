import requests
from datetime import datetime

class USCClass(object):
	semester = "20163"
	def __init__(self, section_id, dept, classnum, name, location, time, days, instructor, type, parent_lecture):
		self.section_id = section_id
		self.dept = dept
		self.classnum = ''.join(c for c in classnum if '0' <= c <= '9')
		self.name = name
		self.location = location
		self.type = type
		self.parent_lecture = parent_lecture

		self.set_time(time)
		self.set_days(days)
		self.set_instructor(instructor)

	def __repr__(self):
		return str(self.__dict__)

	def to_SQL(self):
		if self.type == "Lecture":
			string = "INSERT INTO USCClasses"
		elif self.type == "Discussion":
			string = "INSERT INTO Discussions"
		elif self.type == "Lab":
			string = "INSERT INTO Labs"
		else:
			return None

		if self.start is None or self.end is None:
			return None

		if self.type == "Lecture":
			string += " (section_id, dept, class_num, semester, name, professor, location, start_time, end_time, monday, tuesday, wednesday, thursday, friday) VALUES (" \
						+ str(self.section_id) + ",'" + self.dept + "'," + self.classnum + "," + self.semester + ",'" + self.name + "','" + self.instructor + "','" + self.location \
											 + "','" + self.start + "','" + self.end + "'," \
											 + str(self.monday) + "," + str(self.tuesday) + "," + str(self.wednesday) + "," + str(self.thursday) + "," + str(self.friday)
		elif self.type == "Discussion" or self.type == "Lab":
			string += " (section_id, class_id, start_time, end_time, monday, tuesday, wednesday, thursday, friday) VALUES (" \
						+ str(self.section_id) + "," + "(SELECT class_id FROM USCClasses WHERE section_id=" + str(parent_lecture) + ")" + ",'" + self.start + "','" + self.end + "'," \
											 + str(self.monday) + "," + str(self.tuesday) + "," + str(self.wednesday) + "," + str(self.thursday) + "," + str(self.friday)

		string = string.replace("<br />", " ");

		return (string + ");\n")

	def set_time(self, time):
		if "TBA" in time:
			self.start = None
			self.end = None
		else:
			if len(time.split("-")) > 2:
				self.type = "invalid"
				return
			start, end = time.split("-")
			format_in = '%I:%M%p'
			format_out = '%H:%M:%S'
			start, end = datetime.strptime(start, format_in), datetime.strptime(end, format_in)
			self.start, self.end = start.strftime(format_out), end.strftime(format_out)
		self.time = None

	def set_days(self, days):
		self.monday = False
		self.tuesday = False
		self.wednesday = False
		self.thursday = False
		self.friday = False

		if "M" in days:
			self.monday = True
		if "Th" in days:
			self.thursday = True
			days = "".join(days.split("Th"))
		if "W" in days:
			self.wednesday = True
		if "T" in days:
			self.tuesday = True
		if "F" in days:
			self.friday = True


	def set_instructor(self, instructor):
		if (len(instructor)):
			self.instructor = instructor.split(", ")[1] + " " + instructor.split(", ")[0]
			self.instructor = self.instructor.replace("'", "")
		else:
			self.instructor = "TBA"


cookies = {
	"uscsso-my": "2WNgv7tOh33J2fL%2BV2hq%2BnQMebrpOn9%2BoSB24VTtpXPJ2g0sJgYM6s3UeGH0kIpUFGsQ%2B18hLltVXuhzVtl5MzA2g2YoBkttm%2FhKASduqPIwDkRGIYeSqpGWcFhCzSYB",
	"ASP.NET_SessionId": "jxouvkj0zkduyepjrhxqorra",
	"NSC_BU-xfc-dbm-xfcsfh": "ffffffff096cb21245525d5f4f58455e445a4a422970"
}

requests.get('https://webreg.usc.edu/Terms/termSelect?term=' + USCClass.semester, cookies=cookies)

r = requests.get('https://webreg.usc.edu/Departments', cookies=cookies)

with open('classes.sql', 'wb') as sql:
	for string in r.text.split("/Courses?DeptId="):
		if "' class" in string:
			string = string.split("'")[0]
			req = requests.get('https://webreg.usc.edu/Courses?DeptId=' + string, cookies=cookies)
			
			parent_lecture = -1
			for string2 in req.text.split('<span class="crsID">'):
				if 'Section: </span><b>' in string2:
					for sub in string2.split('<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">'):
						if 'Section: </span><b>' in sub:
							cl = USCClass(int(sub.split('Section: </span><b>')[1].split(' ')[0]),
												string, string2.split(':</span> <span class="crsTitl">')[0].split('-')[1],
												string2.split('<span class="crsTitl">')[1].split('</span>')[0],
												sub.split('Location: </span>')[1].split('</span>')[0],
												sub.split('Time: </span>')[1].split('</span>')[0],
												sub.split('Days: </span>')[1].split('</span>')[0],
												sub.split('Instructor: </span>')[1].split('</span>')[0],
												sub.split('Type: </span>')[1].split('</span>')[0], parent_lecture)

							if cl.type == "Lecture":
								parent_lecture = cl.section_id
							# print(repr(cl))
							one = cl.to_SQL()
							if one is not None:
								print(one)
								sql.write(one)