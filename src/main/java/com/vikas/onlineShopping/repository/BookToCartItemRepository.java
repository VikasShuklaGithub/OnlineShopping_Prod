package com.vikas.onlineShopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.BookToCartItem;
import com.vikas.onlineShopping.model.CartItem;


@Repository
@Transactional
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {

	
		void deleteByCartItem(CartItem cartItem);
	
	


}
