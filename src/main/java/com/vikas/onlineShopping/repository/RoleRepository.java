package com.vikas.onlineShopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vikas.onlineShopping.model.security.Role;



public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByName(String name);

}
