package com.vikas.onlineShopping.service;

import com.vikas.onlineShopping.model.ShoppingCart;

public interface ShoppingCartService {

	public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

	public void clearShoppingCart(ShoppingCart shoppingCart);
	

}
