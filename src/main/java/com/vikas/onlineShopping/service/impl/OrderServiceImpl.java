 package com.vikas.onlineShopping.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.BillingAddress;
import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.Payment;
import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.model.ShippingAddress;
import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.repository.OrderRepository;
import com.vikas.onlineShopping.service.CartItemService;
import com.vikas.onlineShopping.service.OrderService;



@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@Autowired
	private CartItemService cartItemService;

	@Override
	public synchronized Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress,
			//BillingAddress billingAddress,
			//Payment payment, 
			String shippingMethod, User user) {
		
		
		Order order=new Order();
		
		//order.setBillingAddress(billingAddress);
		order.setOrderStatus("created");
		//order.setPayment(payment);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);
		
		List<CartItem> cartItemList=cartItemService.findByShoppingCart(shoppingCart);
		
		
		for(CartItem cartItem:cartItemList)
		{
			Product product=cartItem.getProduct();
			cartItem.setOrder(order);
			product.setInStockNumber(product.getInStockNumber()-cartItem.getQty());
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());

		order.setOrderTotal(shoppingCart.getGrandTotal());
		System.out.println("Order Page :"+shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		
		System.out.println("Order is :"  +order);
		//billingAddress.setOrder(order);
		//payment.setOrder(order);
		order.setUser(user);
		
		order=orderRepository.save(order);
		
		return order;
		
	}
	
	
	
	@Override
	public synchronized Order returnOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress,
			//BillingAddress billingAddress,
			//Payment payment, 
			String shippingMethod, User user) {
		
		
		Order order=new Order();
		
		//order.setBillingAddress(billingAddress);
		order.setOrderStatus("returned");
		//order.setPayment(payment);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);
		
		List<CartItem> cartItemList=cartItemService.findByShoppingCart(shoppingCart);
		
		
		for(CartItem cartItem:cartItemList)
		{
			Product product=cartItem.getProduct();
			cartItem.setOrder(order);
			product.setInStockNumber(product.getInStockNumber()+cartItem.getQty());
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());

		order.setOrderTotal(shoppingCart.getGrandTotal());
		System.out.println("Order Page :"+shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		
		System.out.println("Order is :"  +order);
		//billingAddress.setOrder(order);
		//payment.setOrder(order);
		order.setUser(user);
		
		order=orderRepository.save(order);
		
		return order;
		
	}


	@Override
	public Order findById(Long id) {
		// TODO Auto-generated method stub
		return orderRepository.findById(id).orElse(null) ;
	}

}
