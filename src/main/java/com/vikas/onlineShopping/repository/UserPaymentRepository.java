package com.vikas.onlineShopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.UserPayment;

@Repository
@Transactional
public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {

}
