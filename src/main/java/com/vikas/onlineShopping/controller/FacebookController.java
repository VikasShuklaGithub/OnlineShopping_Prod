
package com.vikas.onlineShopping.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.UserPayment;
import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.service.FacebookService;
import com.vikas.onlineShopping.service.SecurityService;
import com.vikas.onlineShopping.service.UserService;

@Controller
public class FacebookController {

	@Autowired
	private FacebookService facebookService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService securityService;

	@GetMapping(value = "/facebookLogin")
	public RedirectView facebookLogin() {
		
		System.out.println("Facebook1");
		RedirectView redirectView = new RedirectView();
		String url = facebookService.facebookLogin();
		System.out.println("URL is : "+url);
		redirectView.setUrl(url);
		return redirectView;

	}

	@GetMapping("/facebook")
	public String facebook(@RequestParam("code") String code) {
		String accessToken = facebookService.getFacebookAccessToken(code);
		System.out.println("accessToken : "+accessToken);
		System.out.println("Facebook3");
		return "redirect:/facebookprofiledata/" + accessToken;
	}

	@GetMapping(value = "/facebookprofiledata/{accessToken:.+}")
	public String facebookprofiledata(@PathVariable String accessToken, Model model, HttpServletRequest request) {
		User user = facebookService.getFacebookUserProfile(accessToken);
		com.vikas.onlineShopping.model.User dbUser = userService.findByEmail(user.getEmail());
		System.out.println("Facebook4");
		String role = "USER";

		if (dbUser != null) {
			dbUser.setFirstName(user.getFirstName());
			dbUser.setLastName(user.getLastName()); // userService.update(dbUser);
			role = dbUser.getUsername();
			ShoppingCart shoppingCart=new ShoppingCart();
			
			shoppingCart.setUser(dbUser);
			
			dbUser.setShoppingCart(shoppingCart);
			
			
			dbUser.setUserShippingList(new ArrayList<UserShipping>());
			dbUser.setUserPaymentList(new ArrayList<UserPayment>());
			
			userService.save(dbUser);
			model.addAttribute("userInfo", dbUser);
		} else {
			com.vikas.onlineShopping.model.User userInfo = new com.vikas.onlineShopping.model.User();

			userInfo.setFirstName(user.getFirstName());
			userInfo.setLastName(user.getLastName());
			userInfo.setEmail(user.getEmail());
			userInfo.isEnabled();
			ShoppingCart shoppingCart=new ShoppingCart();
				shoppingCart.setUser(userInfo);
			
				userInfo.setShoppingCart(shoppingCart);
			
			
				userInfo.setUserShippingList(new ArrayList<UserShipping>());
				userInfo.setUserPaymentList(new ArrayList<UserPayment>());
			
			userService.save(userInfo);
			model.addAttribute("userInfo", userInfo);
		}

		securityService.autoLogin(user.getEmail(), null, role, request);
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();

		Iterator<? extends GrantedAuthority> iterator = grantedAuthorities.iterator();

		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println(name);

		return "redirect:/";
	}
}
