package com.vikas.onlineShopping.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.Product;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query("from Order where orderDate=:orderDate")
	List<Order> findOrder(@Param("orderDate")Date orderDate);
	

     

}
