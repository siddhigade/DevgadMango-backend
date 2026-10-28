package com.example.repository;



import com.example.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductIdAndVariantId(Long userId, Long productId, Long variantId);
    void deleteByUserId(Long userId);
}

