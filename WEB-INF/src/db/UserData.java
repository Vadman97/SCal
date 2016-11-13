package db;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserData {
	String username;
	String email;
	String password;
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}


	public void setUsername(String value) {
		username = value;
	}

	public void setEmail(String value) {
		email = value;
	}

	public void setPassword(String password) {
		this.password = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(password.getBytes("UTF-8"));
			byte[] digest = md.digest();
			this.password = bytesToHex(digest);
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
	
	public String getPassword() {
		return password;
	}
	
	public void write()
    {
		throw new RuntimeException();
    }
}
