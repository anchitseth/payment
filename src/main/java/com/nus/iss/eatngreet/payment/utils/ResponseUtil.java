package com.nus.iss.eatngreet.payment.utils;

import com.nus.iss.eatngreet.payment.responsedto.CommonResponseDto;

public class ResponseUtil {

	public static void prepareResponse(CommonResponseDto response, String message, String status, String info,
			boolean success) {
		if (response != null) {
			response.setMessage(message);
			response.setStatus(status);
			response.setSuccess(success);
			response.setInfo(info);
		}
	}
}
