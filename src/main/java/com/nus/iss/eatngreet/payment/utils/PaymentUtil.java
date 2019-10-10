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
		String authToken = request.getHeader(Constants.AUTHORIZATION_HEADER_NAME).substring("Basic".length()).trim();
		return new String(Base64.getDecoder().decode(authToken)).split(":")[0];
	}

	public static boolean isValidEmail(String email) {
		if (isStringEmpty(email))
			return false;
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		return pat.matcher(email).matches();
	}

	@SuppressWarnings("rawtypes")
	public static boolean isListEmpty(List list) {
		boolean isEmpty = true;
		if (list != null && !list.isEmpty()) {
			isEmpty = false;
		}
		return isEmpty;
	}

	private PaymentUtil() {

	}
}
