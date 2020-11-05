package com.vikas.onlineShopping.service;

import com.vikas.onlineShopping.model.UserShipping;

public interface UserShippingService {
	
	UserShipping findById(Long id);

	void removeById(Long shippingAddressId);

}
