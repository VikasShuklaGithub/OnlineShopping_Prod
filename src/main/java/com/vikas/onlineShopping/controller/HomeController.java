package com.vikas.onlineShopping.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.DocumentException;
import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.model.security.PasswordResetToken;
import com.vikas.onlineShopping.model.security.Role;
import com.vikas.onlineShopping.model.security.UserRole;
import com.vikas.onlineShopping.repository.OrderRepository;
import com.vikas.onlineShopping.service.CartItemService;
import com.vikas.onlineShopping.service.OrderService;
import com.vikas.onlineShopping.service.ProductService;
import com.vikas.onlineShopping.service.UserService;
import com.vikas.onlineShopping.service.UserShippingService;
import com.vikas.onlineShopping.service.impl.UserSecurityService;
import com.vikas.onlineShopping.utilities.INConstants;
import com.vikas.onlineShopping.utilities.InvoicePDFGenerator;
import com.vikas.onlineShopping.utilities.MailConstructor;
import com.vikas.onlineShopping.utilities.SecurityUtility;



@Controller
public class HomeController {

	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	MailConstructor mailConstructor;
	
	@Autowired
	UserShippingService userShippingService;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderRepository orderRepository;



	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	
	@RequestMapping("/myAccount")
	public String myAccount() {
		return "myAccount";
	}
	
	@RequestMapping("/reportProduct")
	public String reportProduct() {
		return "reportProduct";
	}
	
	@RequestMapping("/reportOrder")
	public String reportOrder() {
		return "reportOrder";
	}
	
	@RequestMapping("/reportReturnOrder")
	public String reportReturnOrder() {
		return "reportReturnOrder";
	}
	 
	
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	@RequestMapping(value="/showUserList",method = RequestMethod.GET)
	public String userList(Model model)
	{
		List<User> userList = userService.findAllUser();
		model.addAttribute("userList", userList);
		return "userList";
		}
	
	@RequestMapping("/productInfo")
	public String productInfo(Model model,Principal principal) {
		
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		model.addAttribute("activeAll", true);
		return "productInfo";
	}
	
	@RequestMapping("/forgetPassword")
	public String forgetPassword(HttpServletRequest request, @ModelAttribute("email") String email, Model model) {

		model.addAttribute("classActiveForgetPassword", true);

		User user = userService.findByEmail(email);

		if (user == null) {
			model.addAttribute("emailNotExists", true);
			return "myAccount";
		}

		// Set the Password in DB in encypted Format : START
		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		// Set the Password in DB in encypted Format: END

		userService.save(user);

		// Create a Token for every Registration : START
		String token = UUID.randomUUID().toString();
		// Create a Token for every Registration : END

		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(newEmail);
		model.addAttribute("forgetPasswordEmailSent", "true");

		return "myAccount";

	}
	
	
	

	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);

		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);

			return "myAccount";
		}

		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);

			return "myAccount";
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		Role role = new Role();

		role.setRoleId((long) 1);

		role.setName("ROLE_USER");

		Set<UserRole> userRoles = new HashSet<UserRole>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();

		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(email);
		model.addAttribute("emailSent", "true");

		model.addAttribute("orderList", user.getOrderList());

		return "myAccount";

	}
	
	
	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}
		User user = passToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		model.addAttribute("classActiveEdit", true);
		model.addAttribute("orderList", user.getOrderList());
		return "myProfile";
	}

	

	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword,
			Model model) throws Exception {
		User currentUser = userService.findyById(user.getId());
		if (currentUser == null) {
			throw new Exception("User Not found");
		}

		// Check If Email Exist

		if (userService.findByEmail(user.getEmail()) != null) {
			if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				return "myProfile";
			}
		}

		// Check if UserName Exists

		if (userService.findByUsername(user.getUsername()) != null) {
			if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				return "myProfile";
			}
		}

		// Update Password

		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("inCorrectPassword", true);

				return "myProfile";
			}
		}

		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUsername(user.getUsername());
		currentUser.setPhone(user.getPhone());
		currentUser.setEmail(user.getEmail());

		userService.save(currentUser);

		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);
		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";

	}

	
	@RequestMapping("productDetails")
	public String productDetails(@PathParam("id") Long id, Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		Product product = productService.findbyId(id);

		model.addAttribute("product", product);
		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);

		return "productDetails";
	}
	
	
	
	
	@RequestMapping("productInfoData")
	public String productInfoData(@PathParam("id") Long id, Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		Product product = productService.findbyId(id);

		model.addAttribute("product", product);
		

		return "productInfo";
	}
	
	
	
	@RequestMapping("userInfoData")
	public String userInfoData(@PathParam("id") Long id, Model model, Principal principal) {
		
		
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		 
		 
		User user = userService.findyById(id);
		model.addAttribute("user", user);
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		model.addAttribute("userShippingList", userShippingList);
		
		
		return "userInfo";
	}
	
	
	
	
	@RequestMapping(value="/showOrderList", method = RequestMethod.GET)
	public String orderList(@PathParam("id") Long id,Model model,Principal principal)
	{
		
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		User user = userService.findyById(id);
		model.addAttribute("user", user);
		System.out.println("principal.getName(ORDER ) : "+user.getId());
		
		model.addAttribute("orderList", user.getOrderList());
		
		System.out.println("SHow Order List : "+user.getOrderList());
		return "showOrderList";
	}
	
	
	
	@RequestMapping("/userOrderDetail")
	public String userOrderDetail(@RequestParam("orderId") Long orderId, Model model) {
		
		
		Order order = orderService.findById(orderId);

		System.out.println("Order ID is : " + orderId);
		
		Long id = order.getUser().getId();
		
		User user = userService.findyById(id);
		System.out.println("User Order IDS : "+id);
		model.addAttribute("user", user);
		
		



		if (order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);

			
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);

			List<String> stateList = INConstants.listOfINStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("displayOrderDetail", true);

	

			return "orderDetails";
		}

	}
	
	
	

	@RequestMapping(value = "/showMenPage")
	public String showMenPage(Model model, Principal principal) {
		
		if (principal != null) {
			String email = principal.getName();
			User userInfo = userService.findByEmail(email);
			model.addAttribute("userInfo", userInfo);
		}
		 

		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		model.addAttribute("activeAll", true);
		return "menPage";
	}

	@RequestMapping(value = "/showWomenPage", method = RequestMethod.GET)
	public String showWomenPage(Model model, Principal principal) {
		
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		model.addAttribute("activeAll", true);
		return "womenPage";
	}

	@RequestMapping(value = "/showKidPage", method = RequestMethod.GET)
	public String showKidPage(Model model, Principal principal) {
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		model.addAttribute("activeAll", true);
		return "kidPage";
	}



	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		

		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);

		List<String> stateList = INConstants.listOfINStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveEdit", true);

		return "myProfile";

	}
	
	@RequestMapping("/listOfShippingAddress")
	public String listOfShippingAddress(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());

		model.addAttribute("user", user);

		//model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddress", true);

		//model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		

		return "myProfile";

	}

	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		model.addAttribute("user", user);

		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		
		
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);

		List<String> stateList = INConstants.listOfINStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";
	}
	

	@RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
	public String addNewShippingAddressPost(@ModelAttribute("userShipping") UserShipping userShipping,

			Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		userService.updateUserShipping(userShipping, user);
		
		model.addAttribute("user", user);
		
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";
	}
	

	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(@ModelAttribute("id") Long shippingAddressId, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		UserShipping userShipping = userShippingService.findById(shippingAddressId);

		if (user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);

			model.addAttribute("userShipping", userShipping);

			List<String> stateList = INConstants.listOfINStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);
			
			
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			return "myProfile";

		}

	}
	@RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
	public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId") Long defaultShippingId,
			Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());

		userService.setDefaultShipping(defaultShippingId, user);

		model.addAttribute("user", user);
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddress", true);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		
		return "myProfile";

	}
	
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(@ModelAttribute("id") Long shippingAddressId, Model model, Principal principal) {

		User user = userService.findByUsername(principal.getName());

		UserShipping userShipping = userShippingService.findById(shippingAddressId);

		if (user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);

			userShippingService.removeById(shippingAddressId);

			model.addAttribute("listOfShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}

	}
	
	@RequestMapping("/orderDetail")
	public String orderDetail(@RequestParam("id") Long orderId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());

		System.out.println("User Name is : " + principal.getName());
		Order order = orderService.findById(orderId);

		System.out.println("Order Name is : " + orderId);

		if (order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);

			List<String> stateList = INConstants.listOfINStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("listOfShippingAddress", true);
			model.addAttribute("classActiveOrders", true);
			//model.addAttribute("listOfCreditCards", true);
			model.addAttribute("displayOrderDetail", true);

			return "orderDetails";
		}

	}
	
	
	
	@RequestMapping("/returnProduct")
	public String returnProduct(
			
			@RequestParam("id") Long orderId,
			@RequestParam("id") Long cartItemId,
			@RequestParam("id") Long productId,
			Principal principal, Model model) {
		/*
		 * User user = userService.findByUsername(principal.getName());
		 * 
		 * System.out.println("User Name is : " + principal.getName());
		 */
		
		
		Order order = orderService.findById(orderId);		
		System.out.println("Return Product : "+orderId);
		
		Long id =order.getUser().getId();
		
		User user = userService.findyById(id);
		System.out.println("User Order RETURN IDS : "+id);
		model.addAttribute("user", user);
		
		
		CartItem cartItem=cartItemService.findById(cartItemId);	
		System.out.println("Return Cart Item : "+cartItemId);
		
		Product product=productService.findbyId(productId);
		System.out.println("Return Product ID : "+productId);
		
		


		if (order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("product", product);
			List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
			model.addAttribute("qtyList", qtyList);
			model.addAttribute("qty", 1);

			
			
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);
			
			

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);

			List<String> stateList = INConstants.listOfINStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("listOfShippingAddress", true);
			model.addAttribute("classActiveOrders", true);
			//model.addAttribute("listOfCreditCards", true);
			model.addAttribute("displayOrderDetail", true);

			return "returnProduct";
		}

	}
	
	
	
	
	@RequestMapping(value = "findOrders", method = RequestMethod.POST)
	public String findOrders(

			@RequestParam("orderDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date orderDate,

			Model model)

	{

		List<Order> orderList = orderRepository.findOrder(orderDate);

		System.out.println("From : " + orderDate);

		System.out.println("Display orderDate : " + orderList);

		model.addAttribute("orderList", orderList);
		return "displayOrders";
	}

	@GetMapping("/generateInvoice")
	public void generateInvoice(HttpServletResponse response,@RequestParam("id") Long orderId, Principal principal,Model model) throws DocumentException, IOException
	{
		response.setContentType("application/pdf");
		
		
		DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		
		String currentDateTime=dateFormatter.format(new Date());
		String headerKey="Content-Disposition";
		String headerValue="attachment;filename=invoice_"+currentDateTime+".pdf";
		
		response.setHeader(headerKey, headerValue);
		
		User user = userService.findByUsername(principal.getName());
		System.out.println("/generateInvoice : "+principal.getName());
		Order order = orderService.findById(orderId);
		System.out.println("/generateInvoice : "+orderId);
		List<CartItem> cartItemList = cartItemService.findByOrder(order);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		
		
		
		InvoicePDFGenerator invoice=new InvoicePDFGenerator(cartItemList);
		
		invoice.generateInvoice(response);
		
		
	}
	
}

