package com.vikas.onlineShopping.service;

import com.vikas.onlineShopping.model.Payment;
import com.vikas.onlineShopping.model.UserPayment;

public interface PaymentService {

	Payment setByUserPayment(UserPayment userPayment, Payment payment);

}
