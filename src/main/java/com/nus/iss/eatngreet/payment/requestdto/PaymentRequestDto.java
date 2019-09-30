package com.nus.iss.eatngreet.payment.requestdto;

import lombok.Data;

@Data
public class PaymentRequestDto {

	Long producerOrderId;
	Float amount;
	String producerEmailId;
	String consumerEmailId;
	
}
