package com.vikas.onlineShopping.service;


import com.vikas.onlineShopping.model.Order;

import com.vikas.onlineShopping.model.ShippingAddress;
import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, 
			//BillingAddress billingAddress,
			//Payment payment, 
			String shippingMethod, 
			User user);
	
	Order findById(Long id);

	Order returnOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, String shippingMethod, User user);

}
