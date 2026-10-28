package com.example.service;

import com.example.Entity.Order;
import com.example.Entity.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

	@Autowired
    private JavaMailSender mailSender;

    @Value("${app.admin.notification-email}")
    private String adminEmail;

    public void sendNewOrderNotification(Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("New Order #" + order.getId() + " — Kokan Delights");
            message.setText(buildOrderSummary(order));

            mailSender.send(message);

            System.out.println("Order notification email sent for order #" + order.getId());
        } catch (Exception e) {
            // Never let an email failure break the payment/order flow.
            System.err.println("Failed to send order notification email for order #"
                    + order.getId() + ": " + e.getMessage());
            e.printStackTrace(); // Optional: prints the full exception stack trace
        }
    }

    private String buildOrderSummary(Order order) {
        StringBuilder sb = new StringBuilder();

        sb.append("A new order has been placed and paid.\n\n");
        sb.append("Order #: ").append(order.getId()).append("\n");
        sb.append("Placed on: ").append(
                order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        ).append("\n");
        sb.append("Customer: ").append(order.getUser().getName())
                .append(" (").append(order.getUser().getEmail()).append(")\n\n");

        sb.append("--- Items ---\n");
        for (OrderItem item : order.getItems()) {
            sb.append("- ").append(item.getProduct().getName());
            if (item.getVariantLabel() != null && !item.getVariantLabel().isBlank()) {
                sb.append(" (").append(item.getVariantLabel()).append(")");
            }
            sb.append(" x").append(item.getQuantity());
            sb.append(" — ₹").append(item.getPriceAtPurchase().multiply(
                    java.math.BigDecimal.valueOf(item.getQuantity())
            )).append("\n");
        }

        sb.append("\nTotal: ₹").append(order.getTotalAmount()).append("\n\n");

        sb.append("--- Deliver to ---\n");
        sb.append(order.getShippingAddress().getFullName()).append("\n");
        sb.append(order.getShippingAddress().getPhone()).append("\n");
        sb.append(order.getShippingAddress().getAddressLine1()).append("\n");
        if (order.getShippingAddress().getAddressLine2() != null
                && !order.getShippingAddress().getAddressLine2().isBlank()) {
            sb.append(order.getShippingAddress().getAddressLine2()).append("\n");
        }
        sb.append(order.getShippingAddress().getCity()).append(", ")
                .append(order.getShippingAddress().getState()).append(" - ")
                .append(order.getShippingAddress().getPincode()).append("\n");
        sb.append(order.getShippingAddress().getCountry()).append("\n");

        return sb.toString();
    }
}