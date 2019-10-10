package com.nus.iss.eatngreet.payment.restcontroller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentRestController.class);
	
	@PostMapping("/now")
	public CommonResponseDto payNow(@RequestBody PaymentRequestDto requestObj) {
		logger.info("payNow() of PaymentRestController. Request Obj: {}", requestObj);
		return paymentService.executeBookingTransaction(requestObj);
	}
	
	@PostMapping("/signup-bonus")
	public CommonResponseDto newUser(@RequestBody Map<String, String> requestObj) {
		logger.info("newUser() of PaymentRestController. Request Obj: {}", requestObj);
		return paymentService.signupBonus(requestObj);
	}

	@PostMapping("/topup")
	public CommonResponseDto addBalance(HttpServletRequest request, @RequestBody Map<String, Object> requestObj) {
		logger.info("addBalance() of PaymentRestController. Request Obj: {}", requestObj);
		return paymentService.topup(request, requestObj);
	}
	
	@PostMapping("/get-balance")
	public CommonResponseDto getUserBalance(HttpServletRequest request) {
		logger.info("getUserBalance() of PaymentRestController. Request Obj: {}", request);
		return paymentService.getUserBalance(request);
	}
	
	@GetMapping("/health-check")
	public String healthCheck() {
		logger.info("healthCheck() of PaymentRestController.");
		return "Payment microservice is up and running.";
	}

}
