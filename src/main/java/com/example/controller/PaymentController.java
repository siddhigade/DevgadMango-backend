package com.example.controller;



import com.example.dto.PaymentVerifyRequest;
import com.example.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	@Autowired
    private  OrderService orderService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PaymentVerifyRequest req) {
    	 System.out.println("=== VERIFY ENDPOINT CALLED: " + req);  
        return ResponseEntity.ok(
                orderService.verifyAndConfirmPayment(userDetails.getUsername(), req));
    }
}

