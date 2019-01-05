package com.appliance.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

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
//			LogUtil.error("", MD5.class, "error", e);
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
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			white = buf.toString();
			if (black.equals(white)) {
				return true;
			} else {
				return false;
			}
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static String getMD5(String data){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			md.update(data.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteDigest = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < byteDigest.length; offset++) {
			i = byteDigest[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		// 32位加密
		return buf.toString();
	}
	
}
