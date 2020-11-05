package com.vikas.onlineShopping.service;

import java.util.List;

import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.model.ReturnFromCartItem;

public interface ReturnFromCartItemService {
	
	ReturnFromCartItem findById(Long id);
	
	List<ReturnFromCartItem> findAll();
}
