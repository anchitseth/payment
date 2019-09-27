package com.nus.iss.eatngreet.payment.requestdto;

import lombok.Data;

@Data
public class PaymentRequestDto {

	Long producerOrderId;
	Long price;
	String producerEmailId;
	
}
