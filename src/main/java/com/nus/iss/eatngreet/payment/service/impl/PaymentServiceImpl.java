package com.nus.iss.eatngreet.payment.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nus.iss.eatngreet.payment.entity.TxnLogEntity;
import com.nus.iss.eatngreet.payment.entity.UserBalanceEntity;
import com.nus.iss.eatngreet.payment.repository.TxnLogRepository;
import com.nus.iss.eatngreet.payment.repository.UserBalanceRepository;
import com.nus.iss.eatngreet.payment.responsedto.CommonResponseDto;
import com.nus.iss.eatngreet.payment.responsedto.DataResponseDto;
import com.nus.iss.eatngreet.payment.service.PaymentService;
import com.nus.iss.eatngreet.payment.utils.PaymentUtil;
import com.nus.iss.eatngreet.payment.utils.ResponseUtil;

@Service

public class PaymentServiceImpl implements PaymentService {

	@Autowired
	TxnLogRepository txnLogRepository;

	@Autowired
	UserBalanceRepository userBalanceRepository;

	@Value("${user.signup.bonus}")
	Float signupBonus;

	@Override
	public CommonResponseDto signupBonus(Map<String, String> request) {
		CommonResponseDto response = new CommonResponseDto();
		String email = request.get("emailId");
		if (PaymentUtil.isValidEmail(email)) {
			List<UserBalanceEntity> userRecords = userBalanceRepository.findByUserEmailId(email);
			if (PaymentUtil.isListEmpty(userRecords)) {
				UserBalanceEntity newUserBalance = new UserBalanceEntity();
				newUserBalance.setUserEmailId(email);
				newUserBalance.setBalance(signupBonus);
				userBalanceRepository.save(newUserBalance);
				TxnLogEntity txnLog = new TxnLogEntity();
				txnLog.setAmount(signupBonus);
				txnLog.setReceiverEmailId(email);
				txnLog.setSenderEmailId("N/A");
				txnLog.setTxnType("SIGNUP_BONUS");
				txnLogRepository.save(txnLog);
				ResponseUtil.prepareResponse(response, "Bonus credited.", "SUCCESS", "Bonus credited.", true);
			} else {
				ResponseUtil.prepareResponse(response, "Email id already registered.", "FAILURE",
						"Email id already registered.", false);
			}
		} else {
			ResponseUtil.prepareResponse(response, "Couldn't process signup bonus at this time.", "FAILURE",
					"Invalid email id given for providing signup bonus.", false);
		}
		return response;
	}

	@Override
	public CommonResponseDto topup(HttpServletRequest request, Map<String, Object> requestObj) {
		CommonResponseDto response = new CommonResponseDto();
		try {
			Float topupAmount = (Double.valueOf((Double) requestObj.get("amount"))).floatValue();
			String email = PaymentUtil.getEmailFromHeaders(request);
			if (PaymentUtil.isValidEmail(email)) {
				List<UserBalanceEntity> userBalanceList = userBalanceRepository.findByUserEmailId(email);
				UserBalanceEntity userBal;
				if (PaymentUtil.isListEmpty(userBalanceList)) {
					userBal = new UserBalanceEntity();
					userBal.setUserEmailId(email);
					userBal.setBalance(topupAmount);
					userBalanceRepository.save(userBal);
					TxnLogEntity txnLog = new TxnLogEntity();
					txnLog.setAmount(topupAmount);
					txnLog.setReceiverEmailId(email);
					txnLog.setSenderEmailId("N/A");
					txnLog.setTxnType("SIGNUP_TOPUP");
					txnLogRepository.save(txnLog);
					ResponseUtil.prepareResponse(response, "Successfully topped up.", "SUCCESS",
							"Successfully topped up.", true);
				} else if (userBalanceList.size() > 1) {
					ResponseUtil.prepareResponse(response, "Please try again later.", "FAILURE",
							"Multiple entries found in user_balance table for email id: " + email + ".", false);
				} else {
					userBal = userBalanceList.get(0);
					userBal.setBalance(userBal.getBalance() + topupAmount);
					userBalanceRepository.save(userBal);
					TxnLogEntity txnLog = new TxnLogEntity();
					txnLog.setAmount(topupAmount);
					txnLog.setReceiverEmailId(email);
					txnLog.setSenderEmailId("N/A");
					txnLog.setTxnType("TOPUP");
					txnLogRepository.save(txnLog);
					ResponseUtil.prepareResponse(response, "Successfully topped up.", "SUCCESS",
							"Successfully topped up.", true);
				}
			} else {
				ResponseUtil.prepareResponse(response, "Please try again later.", "FAILURE",
						"Unable to fetch a valid email id from headers.", false);
			}
		} catch (Exception e) {
			ResponseUtil.prepareResponse(response, "Please try again.", "FAILURE",
					"Exception occurred in reading topup amount. Exception message: " + e.getMessage() + ".", false);
		}
		return response;
	}

	@Override
	public DataResponseDto getUserBalance(HttpServletRequest request) {
		DataResponseDto response = new DataResponseDto();
		try {
			String email = PaymentUtil.getEmailFromHeaders(request);
			if (PaymentUtil.isValidEmail(email)) {
				List<UserBalanceEntity> userBalanceList = userBalanceRepository.findByUserEmailId(email);
				UserBalanceEntity userBal;
				if (PaymentUtil.isListEmpty(userBalanceList)) {
					// no user
					// return 0 balance
					ResponseUtil.prepareResponse(response, "No user exists with the given email id.", "SUCCESS",
							"Successfully topped up.", true);
				} else if (userBalanceList.size() > 1) {
					// multiple users
					ResponseUtil.prepareResponse(response, "Please try again later.", "FAILURE",
							"Multiple entries found in user_balance table for email id: " + email + ".", false);
				} else {
					// 1 user
					userBal = userBalanceList.get(0);
					Map<String, Float> data = new HashMap<String, Float>();
					data.put("balance", userBal.getBalance());
					response.setData(data);
					ResponseUtil.prepareResponse(response, "Successfully topped up.", "SUCCESS",
							"Successfully topped up.", true);
				}
			} else {
				ResponseUtil.prepareResponse(response, "Please try again later.", "FAILURE",
						"Unable to fetch a valid email id from headers.", false);
			}
		} catch (Exception e) {
			ResponseUtil.prepareResponse(response, "Unable to fetch balance right now, please try again later.", "FAILURE",
					"Exception occurred in getting user balance. Exception message: " + e.getMessage() + ".", false);
		}
		return response;
	}

}
