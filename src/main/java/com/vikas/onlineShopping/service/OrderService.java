package com.vikas.onlineShopping.service;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vikas.onlineShopping.model.Order;

import com.vikas.onlineShopping.model.ShippingAddress;
import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.utilities.Condition;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, 
			//BillingAddress billingAddress,
			//Payment payment, 
			String shippingMethod, 
			User user);
	
	Order findById(Long id);

	Order returnOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, String shippingMethod, User user);
	
	Order saveOrder(Order order);
	
	List<Order> findAllOrder();
	


}
