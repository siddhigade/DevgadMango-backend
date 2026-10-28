package com.example.service;



import com.example.dto.OrderRequest;
import com.example.dto.PaymentVerifyRequest;
import com.example.dto.RazorpayOrderResponse;
import com.example.Entity.*;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.*;
import com.razorpay.Order;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

	@Autowired
    private  OrderRepository orderRepository;
	@Autowired
    private CartItemRepository cartItemRepository;
	@Autowired
    private  AddressRepository addressRepository;
	@Autowired
    private  UserRepository userRepository;
	@Autowired
    private  RazorpayService razorpayService;
	@Autowired
    private  CartService cartService;
	@Autowired
	private  EmailService emailService; 

    @Transactional
    public RazorpayOrderResponse createOrder(String email, OrderRequest req) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressRepository.findById(req.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total
        BigDecimal total = cartItems.stream()
                .map(item -> {
                    BigDecimal price = (item.getVariant() != null)
                            ? item.getVariant().getPrice()
                            : item.getProduct().getPrice();
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create Razorpay order
        long amountInPaise = total.multiply(BigDecimal.valueOf(100)).longValue();
        com.example.Entity.Order dbOrder = new com.example.Entity.Order();

        dbOrder.setUser(user);
        dbOrder.setShippingAddress(address);
        dbOrder.setTotalAmount(total);
        dbOrder.setStatus(com.example.Entity.Order.OrderStatus.PENDING);
        dbOrder.setPaymentStatus(com.example.Entity.Order.PaymentStatus.PENDING);

        dbOrder = orderRepository.save(dbOrder);

        Order rzpOrder = razorpayService.createOrder(amountInPaise, "rcpt_" + dbOrder.getId());

        dbOrder.setRazorpayOrderId(rzpOrder.get("id").toString());
        orderRepository.save(dbOrder);

        // Create order items
        final com.example.Entity.Order finalDbOrder = dbOrder;
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

            BigDecimal price = (cartItem.getVariant() != null)
                    ? cartItem.getVariant().getPrice()
                    : cartItem.getProduct().getPrice();

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(finalDbOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(price);
            orderItem.setVariantLabel(
                    cartItem.getVariant() != null
                            ? cartItem.getVariant().getLabel()
                            : null
            );

            return orderItem;

        }).collect(Collectors.toList());

        dbOrder.setItems(orderItems);
        orderRepository.save(dbOrder);

        return new RazorpayOrderResponse(
                rzpOrder.get("id").toString(),
                amountInPaise,
                "INR",
                finalDbOrder.getId()
        );
    }

    public String verifyAndConfirmPayment(String email, PaymentVerifyRequest req) {
        boolean valid = razorpayService.verifySignature(
                req.getRazorpayOrderId(),
                req.getRazorpayPaymentId(),
                req.getRazorpaySignature()
        );

        if (!valid) {
            throw new RuntimeException("Invalid payment signature");
        }

        com.example.Entity.Order order = orderRepository
                .findByRazorpayOrderId(req.getRazorpayOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setPaymentStatus(com.example.Entity.Order.PaymentStatus.PAID);
        order.setStatus(com.example.Entity.Order.OrderStatus.CONFIRMED);
        order.setRazorpayPaymentId(req.getRazorpayPaymentId());
        orderRepository.save(order);

        // Clear cart after successful payment
        cartService.clearCart(order.getUser().getId());

        // Notify admin by email
        emailService.sendNewOrderNotification(order);

        return "Payment verified. Order confirmed!";
    }

    public List<com.example.Entity.Order> getUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public List<com.example.Entity.Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public String updateOrderStatus(Long orderId, String status) {
    	com.example.Entity.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(com.example.Entity.Order.OrderStatus.valueOf(status));
        orderRepository.save(order);
        return "Status updated to " + status;
    }
}

