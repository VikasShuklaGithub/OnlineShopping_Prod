package com.vikas.onlineShopping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.vikas.onlineShopping.model.SpringSecurityAuditorAware;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.model.security.Role;
import com.vikas.onlineShopping.model.security.UserRole;
import com.vikas.onlineShopping.service.UserService;
import com.vikas.onlineShopping.utilities.SecurityUtility;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class OnlineShoppingApplication implements CommandLineRunner {
	
	@Autowired
	UserService userService;
	
	@Bean
	public AuditorAware<String> auditorAware()
	{
		return new SpringSecurityAuditorAware();
		
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlineShoppingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1=new User();
		user1.setFirstName("Arshi");
		user1.setLastName("Shukla");
		user1.setUsername("admin");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
		user1.setEmail("arshi.v.shukla12@gmail.com");
		
		Set<UserRole> userRoles=new HashSet<UserRole>();
		Role role1=new Role();
		role1.setRoleId((long) 0);
		role1.setName("ROLE_ADMIN");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
		
	}

}
