package com.vikas.onlineShopping.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vikas.onlineShopping.model.BillingAddress;
import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.Payment;
import com.vikas.onlineShopping.model.Product;
import com.vikas.onlineShopping.model.ShippingAddress;
import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.model.UserBilling;
import com.vikas.onlineShopping.model.UserPayment;
import com.vikas.onlineShopping.model.UserShipping;
import com.vikas.onlineShopping.service.BillingAddressService;
import com.vikas.onlineShopping.service.CartItemService;
import com.vikas.onlineShopping.service.OrderService;
import com.vikas.onlineShopping.service.PaymentService;
import com.vikas.onlineShopping.service.ProductService;
import com.vikas.onlineShopping.service.ShippingAddressService;
import com.vikas.onlineShopping.service.ShoppingCartService;
import com.vikas.onlineShopping.service.UserPaymentService;
import com.vikas.onlineShopping.service.UserService;
import com.vikas.onlineShopping.service.UserShippingService;
import com.vikas.onlineShopping.utilities.INConstants;
import com.vikas.onlineShopping.utilities.MailConstructor;

@Controller
public class CheckoutController {

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private UserPaymentService userPaymentService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private ProductService productService;

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());

		if (cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
		}

		for (CartItem cartItem : cartItemList) {
			if (cartItem.getProduct().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}

		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();

		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);

		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}

		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}

		ShoppingCart shoppingCart = user.getShoppingCart();

		for (UserShipping userShipping : userShippingList) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}

		/*
		 * for (UserPayment userPayment : userPaymentList) {
		 * if(userPayment.isDefaultPayment()) {
		 * paymentService.setByUserPayment(userPayment, payment);
		 * billingAddressService.setByUserBilling(userPayment.getUserBilling(),
		 * billingAddress); } }
		 */
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());

		List<String> stateList = INConstants.listOfINStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveShipping", true);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return "checkout";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(

			@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			// @ModelAttribute("billingAddress") BillingAddress billingAddress,
			// @ModelAttribute("payment") Payment payment,
			// @ModelAttribute("billingSameAsShipping") String billingSameAsShipping,

			@ModelAttribute("shippingMethod") String shippingMethod, Model model, Principal principal) {
		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		/*
		 * if(billingSameAsShipping.equals("true")) {
		 * billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName()
		 * ); billingAddress.setBillingAddressStreet1(shippingAddress.
		 * getShippingAddressStreet1());
		 * billingAddress.setBillingAddressStreet2(shippingAddress.
		 * getShippingAddressStreet2());
		 * billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity()
		 * );
		 * billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState
		 * ()); billingAddress.setBillingAddressZipCode(shippingAddress.
		 * getShippingAddressZipCode());
		 * billingAddress.setBillingAddressCountry(shippingAddress.
		 * getShippingAddressCountry()); }
		 */

		if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressZipCode().isEmpty()
		/*
		 * || payment.getCardNumber().isEmpty() || payment.getCvc() == 0 ||
		 * billingAddress.getBillingAddressStreet1().isEmpty() ||
		 * billingAddress.getBillingAddressCity().isEmpty() ||
		 * billingAddress.getBillingAddressState().isEmpty() ||
		 * billingAddress.getBillingAddressName().isEmpty() ||
		 * billingAddress.getBillingAddressZipCode().isEmpty()
		 */)
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";

		User user = userService.findByUsername(principal.getName());
		System.out.println("User SED: " + user);
		Order order = orderService.createOrder(shoppingCart, shippingAddress,
				// billingAddress,payment,
				shippingMethod, user);
		System.out.println("Order VED: " + order);
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
		System.out.println(user + " , " + "Order OK , " + order + " OK" + "Shoopping Cart :" + shoppingCart);
		shoppingCartService.clearShoppingCart(shoppingCart);

		LocalDate today = LocalDate.now();

		System.out.println("DATE IS VIKAS : " + today);
		LocalDate estimatedDeliveryDate;

		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}

		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		return "orderSubmittedPage";

	}

	/*
	 * //Return Product
	 * 
	 * @RequestMapping(value="/returnProduct",method = RequestMethod.POST) public
	 * String returnProductPost(
	 * 
	 * @ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
	 * 
	 * @ModelAttribute("id") String id, //@ModelAttribute("billingAddress")
	 * BillingAddress billingAddress, //@ModelAttribute("payment") Payment payment,
	 * //@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
	 * 
	 * @ModelAttribute("shippingMethod") String shippingMethod, Model
	 * model,Principal principal ) {
	 * 
	 * List<Product> productList = productService.findAll(); ShoppingCart
	 * shoppingCart=userService.findByUsername(principal.getName()).getShoppingCart(
	 * ); List<CartItem>
	 * cartItemList=cartItemService.findByShoppingCart(shoppingCart);
	 * model.addAttribute("productList", productList);
	 * model.addAttribute("cartItemList", cartItemList);
	 * 
	 * 
	 * 
	 * if(billingSameAsShipping.equals("true")) {
	 * billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName()
	 * ); billingAddress.setBillingAddressStreet1(shippingAddress.
	 * getShippingAddressStreet1());
	 * billingAddress.setBillingAddressStreet2(shippingAddress.
	 * getShippingAddressStreet2());
	 * billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity()
	 * );
	 * billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState
	 * ()); billingAddress.setBillingAddressZipCode(shippingAddress.
	 * getShippingAddressZipCode());
	 * billingAddress.setBillingAddressCountry(shippingAddress.
	 * getShippingAddressCountry()); }
	 * 
	 * 
	 * if (shippingAddress.getShippingAddressStreet1().isEmpty() ||
	 * shippingAddress.getShippingAddressCity().isEmpty() ||
	 * shippingAddress.getShippingAddressState().isEmpty() ||
	 * shippingAddress.getShippingAddressName().isEmpty() ||
	 * shippingAddress.getShippingAddressZipCode().isEmpty()
	 * 
	 * || payment.getCardNumber().isEmpty() || payment.getCvc() == 0 ||
	 * billingAddress.getBillingAddressStreet1().isEmpty() ||
	 * billingAddress.getBillingAddressCity().isEmpty() ||
	 * billingAddress.getBillingAddressState().isEmpty() ||
	 * billingAddress.getBillingAddressName().isEmpty() ||
	 * billingAddress.getBillingAddressZipCode().isEmpty() ) return
	 * "redirect:/checkout?id="+shoppingCart.getId()+"&missingRequiredField=true";
	 * 
	 * User user=userService.findByUsername(principal.getName());
	 * System.out.println("User SED: "+user); Order
	 * order=orderService.returnOrder(shoppingCart, shippingAddress, shippingMethod,
	 * user);
	 * 
	 * System.out.println("Order VED: "+order);
	 * mailSender.send(mailConstructor.returnOrderConfirmationEmail(user,order,
	 * Locale.ENGLISH)); System.out.println(user+" , "+"Order OK , "+order+
	 * " OK"+"Shoopping Cart :"+shoppingCart);
	 * shoppingCartService.clearShoppingCart(shoppingCart);
	 * 
	 * Date orderDate=order.getOrderDate();
	 * 
	 * //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * Date todayDate = new Date();
	 * 
	 * 
	 * long duration = orderDate.getTime() - todayDate.getTime();
	 * 
	 * if(duration>7) { System.out.println("Item Not Returned"); } else {
	 * System.out.println("Returned"); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return "returnProduct";
	 * 
	 * 
	 * }
	 */

	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(

			@RequestParam("userShippingId") Long userShippingId, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			BillingAddress billingAddress = new BillingAddress();

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = INConstants.listOfINStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActiveShipping", true);

			/*
			 * if(userPaymentList.size()==0) { model.addAttribute("emptyPaymentList", true);
			 * } else { model.addAttribute("emptyPaymentList", false); }
			 */

			model.addAttribute("emptyShippingList", false);

			return "checkout";

		}

	}

	@RequestMapping("/setPaymentMethod")
	private String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();

		if (userPayment.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			paymentService.setByUserPayment(userPayment, payment);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			billingAddressService.setByUserBilling(userBilling, billingAddress);

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = INConstants.listOfINStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActivePayment", true);

			model.addAttribute("emptyPaymentList", false);

			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}

			return "checkout";
		}
	}

}
