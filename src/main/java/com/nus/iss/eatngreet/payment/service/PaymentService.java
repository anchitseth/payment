package com.nus.iss.eatngreet.payment.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nus.iss.eatngreet.payment.responsedto.CommonResponseDto;
import com.nus.iss.eatngreet.payment.responsedto.DataResponseDto;

public interface PaymentService {

	public CommonResponseDto signupBonus(Map<String, String> request);
	
	public CommonResponseDto topup(HttpServletRequest request, Map<String, Object> requestObj);
	
	public DataResponseDto getUserBalance(HttpServletRequest request);
}
