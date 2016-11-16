package user;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Util;

public class User {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	public static User getUser(Connection con, long user_id) throws SQLException {
		PreparedStatement s = con.prepareStatement("SELECT username, password, email FROM Users WHERE id=?");
		s.setLong(1, user_id);
		ResultSet rs = s.executeQuery();
		if (rs.next())
			return new User(rs.getString(1), rs.getString(2), rs.getString(3));
		return null;
	}
	private static String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return bytesToHex(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	String email = null;
	String password = null;
	String username = null;
	long id = -1;
	
	public User() {}
	
	public User(String username, String raw_password, String email) {
		setUsername(username);
		setPassword(raw_password);
		setEmail(email);
	}
	
	public void clear() {
		this.username = null;
		this.password = null;
		this.email = null;
	};
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			return ((User) o).username.equals(username);
		}
		return false;
	}

	public String getEmail() {
		return email;
	}

	public long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}
	
	private ResultSet getUser(Connection con, String username, String raw_password) throws SQLException {
		PreparedStatement s = con.prepareStatement("SELECT * FROM Users WHERE username=? AND password=?");
		s.setString(1, username);
		s.setString(2, hashPassword(raw_password));
		return s.executeQuery();
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	public boolean isLoggedIn() {
		return (username != null);
	}

	public boolean login(String username, String password) {
		if (username == null || password == null)
			return false;
		if (username.length() == 0 || password.length() == 0)
			return false;
		Connection con = null;
		try {
			con = Util.getConn();
			ResultSet rs = getUser(con, username, password);
			if (rs.next()) {
				// assert only one such user
				if (rs.isFirst() && rs.isLast()) {
					setId(rs.getLong(1));
					setUsername(username);
					setPassword(password);
					setEmail(rs.getString(4));
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setId(-1);
		setUsername(null);
		setPassword(null);
		setEmail(null);
		return false;
	}

	public void setEmail(String value) {
		email = value;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setPassword(String password) {
		if (password == null) {
			this.password = null;
		} else {
			this.password = hashPassword(password);
		}
	}
	
	public void setUsername(String value) {
		username = value;
	}
	
	public String toString() {
		return "User " + username;
	}

	public boolean write() {
		Connection con = null;
		try {
			con = Util.getConn();
			PreparedStatement s = con.prepareStatement("SELECT * FROM Users WHERE username=? OR email=?");
			s.setString(1, username);
			s.setString(2, email);
			ResultSet rs = s.executeQuery();
			// if such a user does not exist
			if (!rs.next()) {
				s = con.prepareStatement("INSERT INTO Users(username, password, email) VALUES (?, ?, ?)");
				s.setString(1, username);
				s.setString(2, password);
				s.setString(3, email);
				s.executeUpdate();
				// after we write the user, re-login so we can initialize our new ID
				return login(username, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
