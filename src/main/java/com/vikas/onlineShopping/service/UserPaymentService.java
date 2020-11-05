package com.vikas.onlineShopping.service;

import com.vikas.onlineShopping.model.UserPayment;

public interface UserPaymentService {

	UserPayment findById(Long creditCardId);

	void removeById(Long creditCardId);

}
