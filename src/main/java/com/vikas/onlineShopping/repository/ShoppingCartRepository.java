package com.vikas.onlineShopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.ShoppingCart;

@Repository
@Transactional
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

}
