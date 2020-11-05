package com.vikas.onlineShopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.ShoppingCart;



@Repository
@Transactional
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	

	public List<CartItem> findByShoppingCart(ShoppingCart shopping_cart_id);

	
	public List<CartItem> findByOrder(Order order);

	

}
