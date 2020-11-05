package com.vikas.onlineShopping.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.vikas.onlineShopping.repository.UserRepository;
import com.vikas.onlineShopping.service.UserService;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
	
	@Autowired
	private UserService userService;




	
	@Override
	public Optional<String> getCurrentAuditor() {
		//return Optional.ofNullable("admin").filter(s -> !s.isEmpty());
		return Optional.of(System.getProperty("user.name"));
	}
	 

}
