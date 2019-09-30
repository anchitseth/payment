package com.nus.iss.eatngreet.payment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "txn_log")
public class TxnLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "txn_id")
	Long txnId;

	@Column(name = "sender_email_id")
	String senderEmailId;

	@Column(name = "receiver_email_id")
	String receiverEmailId;

	@Column(name = "amount")
	Float amount;

	@Column(name = "producer_order_id")
	Long producerOrderId;
	
	@Column(name = "txn_type")
	String txnType;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
	private Date createdOn;
}
