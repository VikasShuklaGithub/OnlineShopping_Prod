package com.vikas.onlineShopping.service;

import com.vikas.onlineShopping.model.BillingAddress;
import com.vikas.onlineShopping.model.UserBilling;

public interface BillingAddressService {

	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);

}
