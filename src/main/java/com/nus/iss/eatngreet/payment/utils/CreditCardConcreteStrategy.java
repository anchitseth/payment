package com.nus.iss.eatngreet.payment.utils;

import lombok.Data;

@Data
public class CreditCardConcreteStrategy implements PaymentStrategy {

	private String name;
	private String cardNumber;
	private String cvv;
	private String dateOfExpiry;

	public CreditCardConcreteStrategy(String nm, String ccNum, String cvv, String expiryDate) {
		this.name = nm;
		this.cardNumber = ccNum;
		this.cvv = cvv;
		this.dateOfExpiry = expiryDate;
	}

	@Override
	public void pay(int amount) {
		System.out.println(amount + " paid with credit card.");
	}

}
