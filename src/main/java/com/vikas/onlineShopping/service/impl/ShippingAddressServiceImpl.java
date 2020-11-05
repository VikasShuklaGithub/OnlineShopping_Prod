package com.vikas.onlineShopping.service.impl;

import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.ShippingAddress;
import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.service.ShippingAddressService;



@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

	@Override
	public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
		
		shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
		shippingAddress.setShippingAddressStreet1(userShipping.getUserShippingStreet1());
		shippingAddress.setShippingAddressStreet2(userShipping.getUserShippingStreet2());
		shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
		shippingAddress.setShippingAddressState(userShipping.getUserShippingState());
		shippingAddress.setShippingAddressZipCode(userShipping.getUserShippingZipCode());
		shippingAddress.setShippingAddressCountry(userShipping.getUserShippingCountry());
		
		return shippingAddress;


	}

}
