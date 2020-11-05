package com.vikas.onlineShopping.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.service.ProductService;

@Controller
public class SearchController {
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping("/searchByCategoryByMen")
	public String searchByCategoryByMen(
			@RequestParam("category") String category,
			Model model, Principal principal)
			
	{
		
		String classActiveCategory="active"+category;
		
		classActiveCategory=classActiveCategory.replaceAll("\\s+","");
		classActiveCategory=classActiveCategory.replaceAll("&","");
		model.addAttribute(classActiveCategory, true);
		
		List<Product> productList=productService.findByCategory(category);
		
		if(productList.isEmpty())
		{
			model.addAttribute("emptyList", true);
			return "menPage";
		}
		model.addAttribute("productList", productList);
		return "menPage";
		


		
	}
	
	
	@RequestMapping("/searchByCategoryByWomen")
	public String searchByCategoryByWomen(
			@RequestParam("category") String category,
			Model model, Principal principal)
			
	{
		
		String classActiveCategory="active"+category;
		
		classActiveCategory=classActiveCategory.replaceAll("\\s+","");
		classActiveCategory=classActiveCategory.replaceAll("&","");
		model.addAttribute(classActiveCategory, true);
		
		List<Product> productList=productService.findByCategory(category);
		
		if(productList.isEmpty())
		{
			model.addAttribute("emptyList", true);
			return "womenPage";
		}
		model.addAttribute("productList", productList);
		return "womenPage";
		


		
	}
	
	
	@RequestMapping("/searchByCategoryByKids")
	public String searchByCategoryByKids(
			@RequestParam("category") String category,
			Model model, Principal principal)
			
	{
		
		String classActiveCategory="active"+category;
		
		classActiveCategory=classActiveCategory.replaceAll("\\s+","");
		classActiveCategory=classActiveCategory.replaceAll("&","");
		model.addAttribute(classActiveCategory, true);
		
		List<Product> productList=productService.findByCategory(category);
		
		if(productList.isEmpty())
		{
			model.addAttribute("emptyList", true);
			return "kidPage";
		}
		model.addAttribute("productList", productList);
		return "kidPage";
		


		
	}


}
