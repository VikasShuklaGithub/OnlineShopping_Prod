package com.vikas.onlineShopping.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vikas.onlineShopping.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByCategory(String category);

	@Query("from Product where created_date=:created_date")
	List<Product> findProduct(@Param("created_date")Date from);
			

}
