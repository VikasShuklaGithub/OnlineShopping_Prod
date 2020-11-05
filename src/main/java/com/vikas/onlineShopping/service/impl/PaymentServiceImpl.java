package com.vikas.onlineShopping.service.impl;

import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.Payment;
import com.vikas.onlineShopping.model.UserPayment;
import com.vikas.onlineShopping.service.PaymentService;



@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
		// TODO Auto-generated method stub
		
		payment.setType(userPayment.getType());
		payment.setHolderName(userPayment.getHolderName());
		payment.setCardNumber(userPayment.getCardNumber());
		payment.setExpiryMonth(userPayment.getExpiryMonth());
		payment.setExpiryYear(userPayment.getExpiryMonth());
		payment.setCvc(userPayment.getCvc());
		return payment;
	}

}
