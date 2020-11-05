package com.vikas.onlineShopping.service;

import java.util.List;
import java.util.Set;

import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.model.UserBilling;
import com.vikas.onlineShopping.model.UserPayment;
import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.model.security.PasswordResetToken;
import com.vikas.onlineShopping.model.security.UserRole;


public interface UserService {
	

	PasswordResetToken getPasswordResetToken(final String token);
	void createPasswordResetTokenForUser(final User user, final String token);
	User findByUsername(String uasername);
	User findByEmail(String email);
	User createUser(User user, Set<UserRole> userRoles)  throws Exception;
	User save(User user);
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	void setDefaultPayment(Long defaultPaymentId, User user);
	void updateUserShipping(UserShipping userShipping, User user);
	void setDefaultShipping(Long defaultShippingId, User user);
	User findyById(Long id);
	List<User> findAllUser();

	
	

}
