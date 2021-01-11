package com.vikas.onlineShopping.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.onlineShopping.model.BookToCartItem;
import com.vikas.onlineShopping.model.CartItem;
import com.vikas.onlineShopping.model.Order;
import com.vikas.onlineShopping.model.Product;

import com.vikas.onlineShopping.model.ReturnFromCartItem;
import com.vikas.onlineShopping.model.ShoppingCart;
import com.vikas.onlineShopping.model.User;
import com.vikas.onlineShopping.repository.BookToCartItemRepository;
import com.vikas.onlineShopping.repository.CartItemRepository;
import com.vikas.onlineShopping.repository.OrderRepository;
import com.vikas.onlineShopping.repository.ReturnFromCartItemRepository;
import com.vikas.onlineShopping.service.CartItemService;
import com.vikas.onlineShopping.service.OrderService;
import com.vikas.onlineShopping.service.UserService;

@Service
public class CartItemServiceImpl implements CartItemService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private BookToCartItemRepository bookToCartItemRepository;

	@Autowired
	private ReturnFromCartItemRepository returnFromCartItemRepository;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getProduct().getProductPriceAfterdiscount())
				.multiply(new BigDecimal(cartItem.getQty()));

		LOGGER.info("Cart Item Update Big Decdimal 2 : " + bigDecimal);

		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println("Cart Item Update Big Decdimal 1 : " + bigDecimal);

		cartItem.setSubtotal(bigDecimal);

		cartItemRepository.save(cartItem);

		LOGGER.info("Cart Item Update Data : " + cartItem);

		return cartItem;

	}

	@Override
	public CartItem addProductToCartItem(Product product, User user, int qty) {

		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());

		LOGGER.info("cartItemList : :" + cartItemList);
		LOGGER.info("user.getShoppingCart() _ 1 : : " + user.getShoppingCart());

		for (CartItem cartItem : cartItemList) {
			System.out.println("ONE");
			if (product.getId() == cartItem.getProduct().getId()) {
				System.out.println("TWO");

				cartItem.setQty(cartItem.getQty() + qty);

				System.out.println("THREE");

				cartItem.setSubtotal(
						new BigDecimal(product.getProductPriceAfterdiscount()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;

			}
		}

		CartItem cartItem = new CartItem();

		cartItem.setShoppingCart(user.getShoppingCart());
		LOGGER.info("user.getShoppingCart() : " + user.getShoppingCart());
		cartItem.setProduct(product);
		LOGGER.info("Product is : " + product);
		cartItem.setQty(qty);

		LOGGER.info("Quantity is : " + qty);
		cartItem.setSubtotal(new BigDecimal(product.getProductPriceAfterdiscount()).multiply(new BigDecimal(qty)));
		LOGGER.info("SUBTOTAL : " + product.getProductPriceAfterdiscount());
		cartItem = cartItemRepository.save(cartItem);

		BookToCartItem bookToCartItem = new BookToCartItem();
		bookToCartItem.setProduct(product);
		LOGGER.info("BooktoCartItem Product : " + product);
		bookToCartItem.setCartItem(cartItem);
		LOGGER.info("BooktoCArtItem cartItem : " + cartItem);
		bookToCartItemRepository.save(bookToCartItem);

		return cartItem;

	}

	/*
	 * @Override public CartItem addToReturnProductFromCartItem(Product product,
	 * User user, Order order,CartItem cartItem, int qty) {
	 * 
	 * List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
	 * 
	 * LOGGER.info("cartItemList : :" + cartItemList);
	 * LOGGER.info("user.getShoppingCart() _ 1 : : " + user.getShoppingCart());
	 * 
	 * 
	 * ReturnFromCartItem returnFromCartItem = new ReturnFromCartItem();
	 * 
	 * returnFromCartItem.setProduct(product);
	 * System.out.println("removeFromCartItem Product : " + product);
	 * 
	 * returnFromCartItem.setOrder(order);
	 * System.out.println("removeFromCartItem Order : " + order);
	 * 
	 * returnFromCartItem.setCartItem(cartItem);
	 * System.out.println("removeFromCartItem cartItem : " + cartItem);
	 * 
	 * returnFromCartItem.setUser(user);
	 * System.out.println("removeFromCartItem User : " + user);
	 * 
	 * returnFromCartItem.setReturnStatus("Pending Return Process");
	 * 
	 * returnFromCartItemRepository.save(returnFromCartItem); return cartItem;
	 * 
	 * }
	 */

	@Override
	public CartItem changeOrderStatus(Product product, User user, Order order, CartItem cartItem) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		order = cartItem.getOrder();
		product = cartItem.getProduct();
		user = order.getUser();

		order.setOrderStatus("Return Pending");

		return cartItem;
	}

	@Override
	public CartItem findById(Long cartItemId) {
		// TODO Auto-generated method stub
		return cartItemRepository.findById(cartItemId).orElse(null);
	}

	@Override
	public void removeCartItem(CartItem cartItem) {

		bookToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
		// TODO Auto-generated method stub

	}

	@Override
	public CartItem save(CartItem cartItem) {
		// TODO Auto-generated method stub
		return cartItemRepository.save(cartItem);
	}

	@Override
	public List<CartItem> findByOrder(Order order) {
		// TODO Auto-generated method stub
		return cartItemRepository.findByOrder(order);
	}

	@Override
	public CartItem approveAddToReturnProductFromCartItem(User user, Product product, Order order, CartItem cartItem,
			int qty) {

		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());

		System.out.println("approveAddToReturnProductFromCartItem : " + cartItemList);

		ReturnFromCartItem returnFromCartItem = new ReturnFromCartItem();

		returnFromCartItem.setProduct(product);
		returnFromCartItem.setOrder(order);

		returnFromCartItem.setCartItem(cartItem);
		returnFromCartItem.setUser(user);
		returnFromCartItem.setReturnStatus("Approved");

		returnFromCartItemRepository.save(returnFromCartItem);
		// TODO Auto-generated method stub
		return cartItem;
	}

}
