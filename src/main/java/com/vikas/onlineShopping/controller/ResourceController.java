package com.vikas.onlineShopping.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vikas.onlineShopping.service.ProductService;

@RestController
public class ResourceController {
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value="/removeList",method = RequestMethod.POST)
	public String removeList(@RequestBody ArrayList<String> productIdlist , Model model)
	{
		for(String id:productIdlist)
		{
			String productId=id.substring(8);
			productService.removeById(Long.parseLong(productId));		
		}
		
		return "delete success";
	}

}
