package com.nus.iss.eatngreet.payment.utils;

import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class PaymentUtil {

	public static boolean isStringEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static String getEmailFromHeaders(HttpServletRequest request) {
		String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
		String decryptedEmail = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		return decryptedEmail;
	}

	public static Boolean isValidEmail(String email) {
		if (isStringEmpty(email))
			return false;
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		return pat.matcher(email).matches();
	}

	public static boolean isListEmpty(List list) {
		boolean isEmpty = true;
		if (list != null && !list.isEmpty()) {
			isEmpty = false;
		}
		return isEmpty;
	}
}
