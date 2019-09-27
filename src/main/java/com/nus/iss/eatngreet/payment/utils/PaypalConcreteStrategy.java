package com.nus.iss.eatngreet.payment.utils;

import lombok.Data;

@Data
public class PaypalConcreteStrategy implements PaymentStrategy {

	private String emailId;
	private String password;

	public PaypalConcreteStrategy(String email, String pwd) {
		this.emailId = email;
		this.password = pwd;
	}

	@Override
	public void pay(int amount) {
		System.out.println(amount + " paid using Paypal.");
	}

}
