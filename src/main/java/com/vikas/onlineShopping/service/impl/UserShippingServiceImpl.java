package com.vikas.onlineShopping.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.repository.UserShippingRepository;
import com.vikas.onlineShopping.service.UserShippingService;


@Service
public class UserShippingServiceImpl implements UserShippingService {
	
	@Autowired
	UserShippingRepository userShippingRepository;

	@Override
	public UserShipping findById(Long shippingAddressId) {
		// TODO Auto-generated method stub
		return userShippingRepository.findById(shippingAddressId).orElse(null);
	}

	@Override
	public void removeById(Long shippingAddressId) {
		userShippingRepository.deleteById(shippingAddressId);
		
	}

}
