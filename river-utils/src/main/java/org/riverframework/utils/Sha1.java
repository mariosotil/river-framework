package org.riverframework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.riverframework.RiverException;

public class Sha1 {
	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static MessageDigest digest = null;
	
	static {
		try {
			digest = MessageDigest.getInstance("SHA-1");
			
		} catch (NoSuchAlgorithmException e) {
			throw new RiverException(e);
		}
	}

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String getHash(String text) {
		String hash = "";
		try {
			digest.update(text.getBytes());
			hash = bytesToHex(digest.digest());
		} catch (Exception e) {
			// Do nothing. Just return an empty string.
		}
		return hash;
	}
}
