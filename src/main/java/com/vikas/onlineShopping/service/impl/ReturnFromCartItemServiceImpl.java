package com.vikas.onlineShopping.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.model.ReturnFromCartItem;
import com.vikas.onlineShopping.repository.ReturnFromCartItemRepository;
import com.vikas.onlineShopping.service.ReturnFromCartItemService;

@Service
@Transactional
public class ReturnFromCartItemServiceImpl implements ReturnFromCartItemService {
	
	@Autowired
	ReturnFromCartItemRepository returnFromCartItemRepository;

	@Override
	public ReturnFromCartItem findById(Long id) {
		// TODO Auto-generated method stub
		 return returnFromCartItemRepository.findById(id).orElse(null);
	}

	@Override
	public List<ReturnFromCartItem> findAll() {
		// TODO Auto-generated method stub
		
		
		List<ReturnFromCartItem> returnFromCartItemList=returnFromCartItemRepository.findAll();
		
		return returnFromCartItemList;
	}

}
