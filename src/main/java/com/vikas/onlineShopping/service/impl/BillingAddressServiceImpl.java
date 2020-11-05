package com.vikas.onlineShopping.service.impl;

import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.BillingAddress;
import com.vikas.onlineShopping.model.UserBilling;
import com.vikas.onlineShopping.service.BillingAddressService;


@Service
public class BillingAddressServiceImpl implements BillingAddressService {

	@Override
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		
		
		
		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		billingAddress.setBillingAddressState(userBilling.getUserBillingState());
		billingAddress.setBillingAddressZipCode(userBilling.getUserBillingZipCode());
		billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
		
		// TODO Auto-generated method stub
		return billingAddress;
	}

}
