package com.vikas.onlineShopping.service;

import java.util.List;

import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.Product;

import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;



public interface CartItemService {
	
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);

	CartItem addProductToCartItem(Product product, User user, int qty);

	CartItem findById(Long cartItemId);

	void removeCartItem(CartItem findById);

	CartItem save(CartItem cartItem);

	List<CartItem> findByOrder(Order order);


	CartItem addToReturnProductFromCartItem(Product product, User user, Order order, CartItem cartItem ,int qty);
	CartItem approveAddToReturnProductFromCartItem(User user,Product product,Order order,CartItem cartItem ,int qty);

}
