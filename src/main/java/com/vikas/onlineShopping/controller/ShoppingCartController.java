package com.vikas.onlineShopping.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.standard.expression.OrExpression;

import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.Product;

import com.vikas.onlineShopping.model.ReturnFromCartItem;
import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.repository.OrderRepository;
import com.vikas.onlineShopping.repository.ReturnFromCartItemRepository;
import com.vikas.onlineShopping.service.CartItemService;
import com.vikas.onlineShopping.service.OrderService;
import com.vikas.onlineShopping.service.ProductService;
import com.vikas.onlineShopping.service.ReturnFromCartItemService;
import com.vikas.onlineShopping.service.ShoppingCartService;
import com.vikas.onlineShopping.service.UserService;
import com.vikas.onlineShopping.utilities.INConstants;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	CartItem cartItem;

	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

		shoppingCartService.updateShoppingCart(shoppingCart);

		System.out.println("Shopping Cart : " + shoppingCart.toString());

		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);

		return "shoppingCart";
	}

	@RequestMapping(value ="/returnFromCartItemList",method = RequestMethod.GET)
	public String findPendingReturnOrder(Model model)
	{
		LOGGER.info("Inside returnFromCartItemList ");
		
		List <Order> orderList = orderService.findAllOrder().stream()
	            .filter((order) -> order.getOrderStatus().equalsIgnoreCase("Return Pending"))
	            .collect(Collectors.toList());
			orderList.forEach(System.out::println);
			model.addAttribute("orderList", orderList);
			
			return "returnList";
		
	}
		
	

	/*
	 * @RequestMapping(value ="/returnFromCartItemList",method = RequestMethod.GET)
	 * public String returnFromCartItemList(Model model, Principal
	 * principal,@ModelAttribute("order") Order order) {
	 * 
	 * LOGGER.info("Entered Inside returnFromCartItemList");
	 * 
	 * User user = userService.findByUsername(principal.getName());
	 * LOGGER.info("User Service is : "+principal.getName());
	 * 
	 * 
	 * 
	 * if (order.getOrderStatus().equalsIgnoreCase("Return Pending")) {
	 * 
	 * LOGGER.info("Inside Order Status");
	 * 
	 * List<CartItem> cartItemList = cartItemService.findByOrder(order);
	 * model.addAttribute("cartItemList", cartItemList); model.addAttribute("user",
	 * user); model.addAttribute("order", order); model.addAttribute("orderList",
	 * user.getOrderList()); LOGGER.info("Inside Middle Order Status");
	 * 
	 * 
	 * LOGGER.info("Return From Cart Item List User LOgged in is : " +
	 * user.getUsername()); ShoppingCart shoppingCart = user.getShoppingCart();
	 * 
	 * LOGGER.info("Return From Cart Item List Shopping Cart User Entry : " +
	 * shoppingCart.getUser());
	 * 
	 * List<CartItem> cartItemList =
	 * cartItemService.findByShoppingCart(shoppingCart);
	 * 
	 * LOGGER.info("Entry of returnFromCartItemList");
	 * 
	 * 
	 * List<Order> returnFromCartItemList = user.getOrderList().stream() .filter(s
	 * -> s.getOrderStatus().equalsIgnoreCase("Return Pending")).collect(Collectors.
	 * toList());
	 * 
	 * 
	 * 
	 * //System.out.println("Return Items are :" +
	 * returnFromCartItemList.toArray()); LOGGER.info("Return Items are :" +
	 * returnFromCartItemList);
	 * 
	 * 
	 * //model.addAttribute("shoppingCart", shoppingCart);
	 * //model.addAttribute("returnFromCartItemList", returnFromCartItemList);
	 * 
	 * LOGGER.info("Inside badRequestPage"); return "returnList";
	 * 
	 * } return "";
	 * 
	 * }
	 */

	@RequestMapping("/addCartItem")
	public String addCartItem(@ModelAttribute("product") Product product, @ModelAttribute("qty") String qty,
			Model model, Principal principal)

	{
		User user = userService.findByUsername(principal.getName());
		LOGGER.info("User is Add To CartItem :" + user);

		product = productService.findbyId(product.getId());
		LOGGER.info("Product is Add To CartItem :" + user);

		if (Integer.parseInt(qty) > product.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/productDetails?id=" + product.getId();
		}
		System.out.println("Go to Add Product Cart Item :");
		CartItem cartItem = cartItemService.addProductToCartItem(product, user, Integer.parseInt(qty));

		System.out.println("Go to After Add Product Cart Item :");
		model.addAttribute("addProductSuccess", true);
		return "forward:/productDetails?id=" + product.getId();
	}

	@RequestMapping(value = "/removeCartItem", method = RequestMethod.GET)
	public String removeCartItem(@RequestParam("id") Long id, @ModelAttribute("cartItem") CartItem cartItem,
			Model model)

	{

		Order order = orderService.findById(id);

		LOGGER.info("Order REMOVE ID is : " + id);

		Long userId = order.getUser().getId();

		User user = userService.findyById(userId);

		LOGGER.info("User REMOVE Order IDS : " + userId);

		model.addAttribute("user", user);

		model.addAttribute("order", order);
		model.addAttribute("cartItem", cartItem);
		System.out.println(order.getOrderDate());

		Date orderDate = order.getOrderDate();

		LOGGER.info("Order Date : " + orderDate);
		System.out.println("Order Date : " + orderDate);
		Date todayDate = new Date();

		long duration = todayDate.getTime() - orderDate.getTime();

		float days = (duration / (1000 * 60 * 60 * 24));
		System.out.println("duration Date : " + days);

		if (days > 100) {
			model.addAttribute("returnDateExceeded", true);
			return "forward:/returnProduct?id=" + cartItem.getProduct().getId();

		} else {
			System.out.println("Go to Remove Product Cart Item :");
			order.setOrderStatus("Return Pending");
			orderService.saveOrder(order);
			System.out.println("HELLO END");
		}
		return "forward:/returnProduct?id=" + cartItem.getProduct().getId();

	}

	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(@ModelAttribute("id") Long cartItemId, @ModelAttribute("qty") int qty)

	{
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		return "forward:/shoppingCart/cart";
	}

	@RequestMapping("/removeShoppingCart")
	public String removeShoppingCart(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		return "forward:/shoppingCart/cart";
	}

	@RequestMapping("/removeCartItemApproved")
	public String removeCartItemApproved(@ModelAttribute("product") Product product,

			@ModelAttribute("order") Order order, @ModelAttribute("user") User user,

			@ModelAttribute("cartItem") CartItem cartItem,

			Model model, Principal principal)

	{

		user = userService.findByUsername(principal.getName());
		System.out.println("User is remove To CartItem :" + principal.getName());

		product = productService.findbyId(product.getId());
		System.out.println("Product is Removed To CartItem :" + product.getId());

		order = orderService.findById(order.getId());
		System.out.println("Order Remove is : " + order.getId());

		cartItem = cartItemService.findById(cartItem.getId());
		System.out.println("Order Remove is : " + cartItem.getId());

		int qty = cartItem.getQty();

		model.addAttribute("user", user);
		model.addAttribute("product", product);
		model.addAttribute("order", order);
		model.addAttribute("cartItem", cartItem);

		System.out.println(order.getOrderDate());

		Date orderDate = order.getOrderDate();
		System.out.println("Order Date : " + orderDate);
		Date todayDate = new Date();

		long duration = todayDate.getTime() - orderDate.getTime();

		float days = (duration / (1000 * 60 * 60 * 24));
		System.out.println("duration Date : " + days);

		if (days > 1) {
			model.addAttribute("returnDateExceeded", true);
			return "returnList";

		}

		System.out.println("Go to Remove Product Cart Item :");

		cartItem = cartItemService.approveAddToReturnProductFromCartItem(user, product, order, cartItem, qty);
		System.out.println("Go to After Add Product Cart Item :");
		model.addAttribute("removeProduct", true);
		System.out.println("Returned");
		return "returnList";

	}

}
