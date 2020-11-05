package com.vikas.onlineShopping.service;

import com.vikas.onlineShopping.model.ShippingAddress;
import com.vikas.onlineShopping.model.UserShipping;

public interface ShippingAddressService {

	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
	
	

}
