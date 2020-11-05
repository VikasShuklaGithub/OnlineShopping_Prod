package com.vikas.onlineShopping.service;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
	
	

	void autoLogin(String email, String password, String role, HttpServletRequest request);

}
