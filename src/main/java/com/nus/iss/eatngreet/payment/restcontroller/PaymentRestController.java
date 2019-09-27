package com.nus.iss.eatngreet.payment.restcontroller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nus.iss.eatngreet.payment.requestdto.PaymentRequestDto;
import com.nus.iss.eatngreet.payment.responsedto.CommonResponseDto;
import com.nus.iss.eatngreet.payment.service.PaymentService;

@RestController
@RequestMapping("/pay")
public class PaymentRestController {
	
	@Autowired
	PaymentService paymentService;
	
	@PostMapping("/now")
	CommonResponseDto payNow(@RequestBody PaymentRequestDto requestObj) {
		return null;
	}
	
	// 1. signup pe 20 SGD
	@PostMapping("/signup-bonus")
	CommonResponseDto newUser(@RequestBody Map<String, String> request) {
		return paymentService.signupBonus(request);
	}
	
	// 2. api to add balance
	@PostMapping("/topup")
	CommonResponseDto addBalance(HttpServletRequest request, @RequestBody Map<String, Object> requestObj) {
		return paymentService.topup(request, requestObj);
	}
	
	@PostMapping("/get-balance")
	CommonResponseDto getUserBalance(HttpServletRequest request) {
		return paymentService.getUserBalance(request);
	}
	
	@GetMapping("/health-check")
	public String healthCheck() {
		return "Payment microservice is up and running.";
	}

}
