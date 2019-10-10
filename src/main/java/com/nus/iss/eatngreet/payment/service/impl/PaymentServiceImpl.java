package com.nus.iss.eatngreet.payment.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nus.iss.eatngreet.payment.entity.TxnLogEntity;
import com.nus.iss.eatngreet.payment.entity.UserBalanceEntity;
import com.nus.iss.eatngreet.payment.repository.TxnLogRepository;
import com.nus.iss.eatngreet.payment.repository.UserBalanceRepository;
import com.nus.iss.eatngreet.payment.requestdto.PaymentRequestDto;
import com.nus.iss.eatngreet.payment.responsedto.CommonResponseDto;
import com.nus.iss.eatngreet.payment.responsedto.DataResponseDto;
import com.nus.iss.eatngreet.payment.service.PaymentService;
import com.nus.iss.eatngreet.payment.utils.Constants;
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

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Override
	public CommonResponseDto signupBonus(Map<String, String> request) {
		logger.info("signupBonus() of PaymentServiceImpl.");
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
				ResponseUtil.prepareResponse(response, "Bonus credited.", Constants.STATUS_SUCCESS, "Bonus credited.",
						true);
			} else {
				logger.warn("User already registered. Email id: {}", email);
				ResponseUtil.prepareResponse(response, "Email id already registered.", Constants.STATUS_FAILURE,
						"Email id already registered.", false);
			}
		} else {
			logger.error(Constants.INVALID_EMAIL_ID_LOG_MSG, email);
			ResponseUtil.prepareResponse(response, "Couldn't process signup bonus at this time.",
					Constants.STATUS_FAILURE, "Invalid email id given for providing signup bonus.", false);
		}
		return response;
	}

	@Override
	public CommonResponseDto topup(HttpServletRequest request, Map<String, Object> requestObj) {
		logger.info("topup() of PaymentServiceImpl.");
		CommonResponseDto response = new CommonResponseDto();
		try {
			Float topupAmount = ((Double) requestObj.get("amount")).floatValue();
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
					ResponseUtil.prepareResponse(response, Constants.TOPUP_SUCCESS_MSG, Constants.STATUS_SUCCESS,
							Constants.TOPUP_SUCCESS_MSG, true);
				} else if (userBalanceList.size() > 1) {
					logger.error("Multiple records exist for the given user. User email id: {}.", email);
					ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
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
					ResponseUtil.prepareResponse(response, Constants.TOPUP_SUCCESS_MSG, Constants.STATUS_SUCCESS,
							Constants.TOPUP_SUCCESS_MSG, true);
				}
			} else {
				logger.error(Constants.INVALID_EMAIL_ID_LOG_MSG, email);
				ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
						"Unable to fetch a valid email id from headers.", false);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while trying to topup. Exception message: {}.",
					e.getMessage());
			ResponseUtil.prepareResponse(response, "Please try again.", Constants.STATUS_FAILURE,
					"Exception occurred while topup. Exception message: " + e.getMessage() + ".", false);
		}
		return response;
	}

	@Override
	public DataResponseDto getUserBalance(HttpServletRequest request) {
		logger.info("getUserBalance() of PaymentServiceImpl.");
		DataResponseDto response = new DataResponseDto();
		try {
			String email = PaymentUtil.getEmailFromHeaders(request);
			if (PaymentUtil.isValidEmail(email)) {
				List<UserBalanceEntity> userBalanceList = userBalanceRepository.findByUserEmailId(email);
				UserBalanceEntity userBal;
				if (PaymentUtil.isListEmpty(userBalanceList)) {
					logger.error("No user exists with the given email id. Email id: {}.", email);
					ResponseUtil.prepareResponse(response, "No user exists with the given email id.",
							Constants.STATUS_FAILURE, "No user exists with the given email id.", false);
				} else if (userBalanceList.size() > 1) {
					logger.error("Multiple entries found in user_balance table for email id: {}.", email);
					ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
							"Multiple entries found in user_balance table for email id: " + email + ".", false);
				} else {
					userBal = userBalanceList.get(0);
					Map<String, Float> data = new HashMap<>();
					data.put("balance", userBal.getBalance());
					response.setData(data);
					logger.info("Successfully executed transaction.");
					ResponseUtil.prepareResponse(response, "Successfully fetched balance.", "SUCCESS",
							"Successfully fetched balance.", true);
				}
			} else {
				logger.error(Constants.INVALID_EMAIL_ID_LOG_MSG, email);
				ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
						"Unable to fetch a valid email id from headers.", false);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while trying to fetch balance. Exception message: {}.",
					e.getMessage());
			ResponseUtil.prepareResponse(response, "Unable to fetch balance right now, please try again later.",
					Constants.STATUS_FAILURE,
					"Exception occurred in getting user balance. Exception message: " + e.getMessage() + ".", false);
		}
		return response;
	}

	@Override
	public CommonResponseDto executeBookingTransaction(PaymentRequestDto requestObj) {
		logger.info("executeBookingTransaction() of PaymentServiceImpl.");
		CommonResponseDto response = new CommonResponseDto();
		try {
			String guestEmailId = requestObj.getConsumerEmailId();
			String hostEmailId = requestObj.getProducerEmailId();
			Float amount = requestObj.getAmount();
			List<UserBalanceEntity> guestRecordList = userBalanceRepository.findByUserEmailId(guestEmailId);
			List<UserBalanceEntity> hostRecordList = userBalanceRepository.findByUserEmailId(hostEmailId);
			if (guestRecordList.size() == 1 && hostRecordList.size() == 1) {
				UserBalanceEntity guest = guestRecordList.get(0);
				UserBalanceEntity host = hostRecordList.get(0);
				if (guest.getBalance() < amount) {
					logger.error("Insufficient balance in guest's wallet. Required amount: {}, actual balance: {}",
							amount, guest.getBalance());
					ResponseUtil.prepareResponse(response,
							"You do not have sufficient balance in your EnG wallet for this transaction.",
							Constants.STATUS_FAILURE, "Insufficient balance in consumer's account for the txn.", false);
				} else {
					guest.setBalance(guest.getBalance() - amount);
					host.setBalance(host.getBalance() + amount);
					userBalanceRepository.save(guest);
					userBalanceRepository.save(host);
					TxnLogEntity txn = new TxnLogEntity();
					txn.setAmount(amount);
					txn.setProducerOrderId(requestObj.getProducerOrderId());
					txn.setReceiverEmailId(hostEmailId);
					txn.setSenderEmailId(guestEmailId);
					txn.setTxnType("BOOKING");
					txnLogRepository.save(txn);
					logger.info("Successfully executed payment transaction.");
					ResponseUtil.prepareResponse(response, "Transaction success.", Constants.STATUS_SUCCESS,
							"Transaction success.", true);
				}
			} else if (guestRecordList.isEmpty()) {
				logger.error("No account found corresponding to guest. Guest email id: {}", guestEmailId);
				ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
						"No account found corresponding to guest.", false);
			} else if (hostRecordList.isEmpty()) {
				logger.error("No account found corresponding to host. Host email id: {}.", hostEmailId);
				ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
						"No account found corresponding to host.", false);
			} else if (guestRecordList.size() > 1) {
				logger.error("Multiple records exist for the given guest. Guest email id: {}.", guestEmailId);
				ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
						"Multiple records exist for the given guest. Consumer email: " + guestEmailId, false);
			} else if (hostRecordList.size() > 1) {
				logger.error("Multiple records exist for the given host. Guest email id: {}.", hostEmailId);
				ResponseUtil.prepareResponse(response, Constants.TRY_AGAIN_MSG_FOR_USERS, Constants.STATUS_FAILURE,
						"Multiple records exist for the given host. Consumer email: " + hostEmailId, false);
			}
		} catch (Exception e) {
			logger.error("Exception occurred while trying to execute payment transaction. Exception message: {}.",
					e.getMessage());
			ResponseUtil.prepareResponse(response,
					"Some problem occurred while executing transaction. Please try again later.",
					Constants.STATUS_FAILURE,
					"Exception occurred while trying to execute transaction. Exception message: " + e.getMessage(),
					false);
		}
		return response;
	}

}
