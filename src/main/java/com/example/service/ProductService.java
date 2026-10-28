package com.example.service;



import com.example.dto.ProductRequest;
import com.example.Entity.*;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
	@Autowired
    private  ProductRepository productRepository;
	@Autowired
    private  CategoryRepository categoryRepository;
	@Autowired
    private  ProductVariantRepository variantRepository;

    public List<Product> getAllActive() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }
   
    public List<Product> getAllForAdmin() {
        return productRepository.findAll();
    }

    public Product reactivate(Long id) {
        Product product = getById(id);
        product.setActive(true);
        return productRepository.save(product);
    }
    
    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword);
    }

    @Transactional
    public Product create(ProductRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setCategory(category);
        product.setImageUrl(req.getImageUrl());
        product.setStock(req.getStock());
        product.setActive(true);

        Product saved = productRepository.save(product);

        if (req.getVariants() != null) {
            for (ProductRequest.VariantRequest v : req.getVariants()) {
            	ProductVariant variant = new ProductVariant();

            	variant.setLabel(v.getLabel());
            	variant.setPrice(v.getPrice());
            	variant.setStock(v.getStock());
            	variant.setProduct(saved);

            	variantRepository.save(variant);
            }
        }
        return saved;
    }

    @Transactional
    public Product update(Long id, ProductRequest req) {
        Product product = getById(id);
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setCategory(category);
        product.setImageUrl(req.getImageUrl());
        product.setStock(req.getStock());
        
        
        product.getVariants().clear();
        
        if (req.getVariants() != null) {
        	 for (ProductRequest.VariantRequest variantReq : req.getVariants()) {
            	ProductVariant variant = new ProductVariant();

            	variant.setLabel(variantReq.getLabel());
            	variant.setPrice(variantReq.getPrice());
            	variant.setStock(variantReq.getStock());
            	variant.setProduct(product);

            	product.getVariants().add(variant);
            }
    }
        return productRepository.save(product);
    }
    

    public void delete(Long id) {
        Product product = getById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
