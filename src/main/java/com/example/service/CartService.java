package com.example.service;

import com.example.dto.CartRequest;
import com.example.Entity.*;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

	@Autowired
    private  CartItemRepository cartItemRepository;
	@Autowired
    private  ProductRepository productRepository;
	@Autowired
    private  ProductVariantRepository variantRepository;
	@Autowired
    private  UserRepository userRepository;

    public List<CartItem> getCart(String email) {
        User user = getUserByEmail(email);
        return cartItemRepository.findByUserId(user.getId());
    }

    @Transactional
    public CartItem addToCart(String email, CartRequest req) {
        User user = getUserByEmail(email);
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductVariant variant = null;
        if (req.getVariantId() != null) {
            variant = variantRepository.findById(req.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));
        }

        Long variantId = (variant != null) ? variant.getId() : null;
        Optional<CartItem> existing =
                cartItemRepository.findByUserIdAndProductIdAndVariantId(
                        user.getId(), product.getId(), variantId);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + req.getQuantity());
            return cartItemRepository.save(item);
        }

        CartItem item = new CartItem();

        item.setUser(user);
        item.setProduct(product);
        item.setVariant(variant);
        item.setQuantity(req.getQuantity());

        return cartItemRepository.save(item);
    }

    @Transactional
    public CartItem updateQuantity(String email, Long cartItemId, int quantity) {
        User user = getUserByEmail(email);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (quantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        }
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeItem(String email, Long cartItemId) {
        User user = getUserByEmail(email);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
