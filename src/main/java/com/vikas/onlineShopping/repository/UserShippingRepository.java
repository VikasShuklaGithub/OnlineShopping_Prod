package com.vikas.onlineShopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.UserShipping;

@Repository
@Transactional
public interface UserShippingRepository extends JpaRepository<UserShipping, Long> {

}
