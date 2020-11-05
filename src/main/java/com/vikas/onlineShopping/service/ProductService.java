package com.vikas.onlineShopping.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vikas.onlineShopping.model.Product;

public interface ProductService {
	
	Product save(Product product);
	List<Product> findAll();
	boolean saveDataFromUploadFile(MultipartFile file);
	void removeById(long id);
	Product findbyId(Long id);
	List<Product> findByCategory(String category);

}
