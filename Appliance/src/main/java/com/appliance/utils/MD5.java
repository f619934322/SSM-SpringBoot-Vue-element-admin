package com.appliance.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MD5 {

	private static final String ERROR = "Error:{}";

	public static String md5(String str) {

		String returnString = str;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(returnString.getBytes());
			byte[] b = md.digest();

			int i;

			StringBuilder buf = new StringBuilder("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			returnString = buf.toString();
		} catch (Exception e) {
			log.warn(ERROR, e);
		}
		return returnString;
	}

	private MD5() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean toMD5(String white, String black) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(white.getBytes("UTF8"));
			byte[] byteDigest = md.digest();
			int i;
			StringBuilder sb = new StringBuilder("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					sb.append("0");
				sb.append(Integer.toHexString(i));
			}
			// 32位加密
			white = sb.toString();
			if (black.equals(white)) {
				return black.equals(white);// sonar要求返回相同的文本，布尔的值不应直接用true或false
			}
			// 16位的加密
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.warn(ERROR, e);
			return false;
		}
		return false;
	}

	public static String getMD5(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.warn(ERROR, e);
		}
		try {
			md.update(data.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			log.warn(ERROR, e);
		}
		byte[] byteDigest = md.digest();
		int i;
		StringBuilder sb = new StringBuilder("");
		for (int offset = 0; offset < byteDigest.length; offset++) {
			i = byteDigest[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				sb.append("0");
			sb.append(Integer.toHexString(i));
		}
		// 32位加密
		return sb.toString();
	}

}
