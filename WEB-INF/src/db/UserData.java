package db;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserData {
	String username;
	String email;
	String password;
	int age;

	public void setUsername(String value) {
		username = value;
	}

	public void setEmail(String value) {
		email = value;
	}

	public void setAge(int value) {
		age = value;
	}

	public void setPassword(String password) {
		this.password = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			this.password = new String(digest, "UTF-8"); 
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

}
