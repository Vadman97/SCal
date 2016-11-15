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
	String username = null;
	String email = null;
	String password = null;

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
	
	public static User getUser(Connection con, long user_id) throws SQLException {
		PreparedStatement s = con.prepareStatement("SELECT username, password, email FROM Users WHERE id=?");
		s.setLong(1, user_id);
		ResultSet rs = s.executeQuery();
		if (rs.next())
			return new User(rs.getString(1), rs.getString(2), rs.getString(3));
		return null;
	}
	
	public User() {};
	
	public User(String username, String raw_password, String email) {
		setUsername(username);
		setPassword(raw_password);
		setEmail(email);
	}

	public void setUsername(String value) {
		username = value;
	}

	public void setEmail(String value) {
		email = value;
	}

	public void setPassword(String password) {
		if (password == null) {
			this.password = null;
		} else {
			this.password = hashPassword(password);
		}
	}
	
	public void clear() {
		this.username = null;
		this.password = null;
		this.email = null;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public boolean write() {
		try {
			Connection con = Util.getConn();
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
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean login(String username, String password) {
		if (username == null || password == null)
			return false;
		if (username.length() == 0 || password.length() == 0)
			return false;
		try {
			ResultSet rs = getUser(Util.getConn(), username, password);
			if (rs.next()) {
				// assert only one such user
				if (rs.isFirst() && rs.isLast()) {
					setUsername(username);
					setPassword(password);
					setEmail(rs.getString(4));
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setUsername(null);
		setPassword(null);
		setEmail(null);
		return false;
	}
	
	public boolean isLoggedIn() {
		return (username != null);
	}
	
	private ResultSet getUser(Connection con, String username, String raw_password) throws SQLException {
		PreparedStatement s = con.prepareStatement("SELECT * FROM Users WHERE username=? AND password=?");
		s.setString(1, username);
		s.setString(2, hashPassword(raw_password));
		return s.executeQuery();
	}
}
