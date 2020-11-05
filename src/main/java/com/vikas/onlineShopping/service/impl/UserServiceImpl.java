package com.vikas.onlineShopping.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.model.UserBilling;
import com.vikas.onlineShopping.model.UserPayment;
import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.model.security.PasswordResetToken;
import com.vikas.onlineShopping.model.security.UserRole;
import com.vikas.onlineShopping.repository.PasswordResetTokenRepository;
import com.vikas.onlineShopping.repository.RoleRepository;
import com.vikas.onlineShopping.repository.UserPaymentRepository;
import com.vikas.onlineShopping.repository.UserRepository;
import com.vikas.onlineShopping.repository.UserShippingRepository;
import com.vikas.onlineShopping.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
private static final Logger LOG=LoggerFactory.getLogger(UserService.class);
	
@Autowired
PasswordResetTokenRepository  passwordResetTokenRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserPaymentRepository userPaymentRepository;
	
	@Autowired
	UserShippingRepository userShippingRepository;
	
	

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		// TODO Auto-generated method stub
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		// TODO Auto-generated method stub
		
		final PasswordResetToken myToken=new PasswordResetToken(token,user);
		passwordResetTokenRepository.save(myToken);
		
		
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		// TODO Auto-generated method stub
		
		User localUser=userRepository.findByUsername(user.getUsername());
		if(localUser!=null)
		{
			LOG.info("user {} already exists ",user.getUsername());
		}
		else
		{
			for(UserRole ur:userRoles) {
				roleRepository.save(ur.getRole());
				
			}
			
			user.getUserRoles().addAll(userRoles);
			
			ShoppingCart shoppingCart=new ShoppingCart();
			shoppingCart.setUser(user);
			
			System.out.println("Create User Method :"+user);
			user.setShoppingCart(shoppingCart);
			
			System.out.println("Create User Method : "+shoppingCart);
			
			
			  user.setUserShippingList(new ArrayList<UserShipping>());
			  user.setUserPaymentList(new ArrayList<UserPayment>());
			 
			
			localUser=userRepository.save(user);
		}
		return localUser;
	}
	
	
	


	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public User findyById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		// TODO Auto-generated method stub
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		
		userBilling.setUserPayment(userPayment);
		
		user.getUserPaymentList().add(userPayment);
		
		save(user);
		
	}

	@Override
	public void setDefaultPayment(Long userPaymentId, User user) {
		
		List<UserPayment> userPaymentList=(List<UserPayment>)userPaymentRepository.findAll(); 
		
		for(UserPayment userPayment:userPaymentList)
		{
			if(userPayment.getId()==userPaymentId)	
			{
				userPayment.setDefaultPayment(true);
				userPaymentRepository.save(userPayment);
			}
			else
			{
				userPayment.setDefaultPayment(false);
				userPaymentRepository.save(userPayment);
			}
				
				}
		
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
		
	}

	@Override
	public void setDefaultShipping(Long defaultShippingId, User user) {
		// TODO Auto-generated method stub
		List<UserShipping> userShippingList=(List<UserShipping>)userShippingRepository.findAll();
		
		for(UserShipping userShipping:userShippingList)
		{
			if(userShipping.getId()==defaultShippingId)	
			{
				userShipping.setUserShippingDefault(true);
				userShippingRepository.save(userShipping);
			}
			else
			{
				userShipping.setUserShippingDefault(false);
				userShippingRepository.save(userShipping);
			}
				
				}
		
	}

	@Override
	public List<User> findAllUser() {
		// TODO Auto-generated method stub
		
		List<User> userList = userRepository.findAll();
		return userList;
	}

	

	

}
