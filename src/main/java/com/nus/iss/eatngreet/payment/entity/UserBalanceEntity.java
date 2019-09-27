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
@Table(name = "user_balance")
public class UserBalanceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_balance_id")
	Long userBalanceId;

	@Column(name = "email_id")
	String userEmailId;

	@Column(name = "balance")
	Float balance;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", length = 19, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
	private Date createdOn;
}
