package com.vikas.onlineShopping.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.repository.ShoppingCartRepository;
import com.vikas.onlineShopping.service.CartItemService;
import com.vikas.onlineShopping.service.ShoppingCartService;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	

	@Override
	public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
		
		
		
		BigDecimal cartTotal=new BigDecimal(0);
		
		List<CartItem> CartItemList=cartItemService.findByShoppingCart(shoppingCart);
		
		

		for(CartItem cartItem:CartItemList)
		{
			if(cartItem.getProduct().getInStockNumber()>0)
			{
				cartItemService.updateCartItem(cartItem);
				cartTotal=cartTotal.add(cartItem.getSubtotal());
				System.out.println("Card Total Inside Loop: "+cartTotal);
			}
		}
		System.out.println("Card Total Ouside Total : 1 "+cartTotal);
		shoppingCart.setGrandTotal(cartTotal);
		shoppingCartRepository.save(shoppingCart);
		System.out.println("Card Total Ouside Total : 2 "+cartTotal);
		System.out.println("Card Total Ouside Grnd Total Total : 2 "+shoppingCart.getGrandTotal());
		return shoppingCart;
		
		
		
	}



	public void clearShoppingCart(ShoppingCart shoppingCart) {
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for (CartItem cartItem : cartItemList) {
			cartItem.setShoppingCart(null);
			cartItemService.save(cartItem);
		}
		
		shoppingCart.setGrandTotal(new BigDecimal(0));
		
		shoppingCartRepository.save(shoppingCart);
	}



}
