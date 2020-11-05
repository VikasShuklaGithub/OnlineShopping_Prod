package com.vikas.onlineShopping.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vikas.onlineShopping.model.CartItem;

import com.vikas.onlineShopping.model.ReturnFromCartItem;

@Repository
@Transactional
public interface ReturnFromCartItemRepository extends JpaRepository<ReturnFromCartItem, Long> {

	ReturnFromCartItem save(CartItem cartItem);
	
	
	/*
	 * @Query("SELECT id FROM RemoveFromCartItem rcartItem JOIN CartItem cartItem ON cartItem.id = rcartItem.cartItemId"
	 * ) List<RemoveFromCartItem> findByRemoveCartId(@Param("id") Long cartItemId);
	 */

}
