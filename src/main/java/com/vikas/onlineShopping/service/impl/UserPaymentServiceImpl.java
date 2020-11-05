package com.vikas.onlineShopping.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.UserPayment;
import com.vikas.onlineShopping.repository.UserPaymentRepository;
import com.vikas.onlineShopping.service.UserPaymentService;



@Service
public class UserPaymentServiceImpl implements UserPaymentService {
	
	@Autowired
	UserPaymentRepository userPaymentRepository;

	@Override
	public UserPayment findById(Long creditCardId) {
		// TODO Auto-generated method stub
		return userPaymentRepository.findById(creditCardId).orElse(null); 
	}

	@Override
	public void removeById(Long creditCardId) {
		
		userPaymentRepository.deleteById(creditCardId); 
		// TODO Auto-generated method stub
		
	}

}
