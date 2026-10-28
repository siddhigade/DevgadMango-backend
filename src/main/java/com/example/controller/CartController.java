package com.example.controller;



import com.example.dto.CartRequest;
import com.example.Entity.CartItem;
import com.example.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	@Autowired
    private  CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartRequest req) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUsername(), req));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        CartItem updated = cartService.updateQuantity(userDetails.getUsername(), itemId, quantity);
        return ResponseEntity.ok(updated != null ? updated : "Item removed");
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        cartService.removeItem(userDetails.getUsername(), itemId);
        return ResponseEntity.ok("Item removed");
    }
}

