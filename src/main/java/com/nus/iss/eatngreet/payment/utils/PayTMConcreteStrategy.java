package com.nus.iss.eatngreet.payment.utils;

import lombok.Data;

@Data
public class PayTMConcreteStrategy implements PaymentStrategy {

	private String phoneNo;
	private String password;
	
	public PayTMConcreteStrategy (String phone, String pwd) {
		this.phoneNo = phone;
		this.password = pwd;
	}
	
	@Override
	public void pay(int amount) {
		System.out.println(amount + " paid using PayTM.");
	}

}
