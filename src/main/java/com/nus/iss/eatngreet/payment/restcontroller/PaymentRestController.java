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
	
	// pay for a txn
	@PostMapping("/now")
	public CommonResponseDto payNow(@RequestBody PaymentRequestDto requestObj) {
		return paymentService.executeBookingTransaction(requestObj);
	}
	
	// get all user txn
	@PostMapping("/get-all-txns")
	public CommonResponseDto getAllTxns() {
		return null;
	}
	
	@PostMapping("/signup-bonus")
	public CommonResponseDto newUser(@RequestBody Map<String, String> request) {
		return paymentService.signupBonus(request);
	}

	@PostMapping("/topup")
	public CommonResponseDto addBalance(HttpServletRequest request, @RequestBody Map<String, Object> requestObj) {
		return paymentService.topup(request, requestObj);
	}
	
	@PostMapping("/get-balance")
	public CommonResponseDto getUserBalance(HttpServletRequest request) {
		return paymentService.getUserBalance(request);
	}
	
	@GetMapping("/health-check")
	public String healthCheck() {
		return "Payment microservice is up and running. :D";
	}

}
