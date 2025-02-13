package com.moda.moda_api.user.infrastructure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypt {
	public static String encrypt(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();

			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("비밀번호 암호화 실패", e);
		}
	}

	public boolean verifyPassword(String inputPassword, String storedHashedPassword) {
		String hashedInputPassword = PasswordEncrypt.encrypt(inputPassword);
		return hashedInputPassword.equals(storedHashedPassword);
	}
}