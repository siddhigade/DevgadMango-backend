// CategoryController.java — TEMPORARY debug version
// Drop this in over src/main/java/com/kokandelights/controller/CategoryController.java,
// restart the backend, try creating a category again, and check the backend console
// (not the browser) for the "Received category:" line.

package com.example.controller;

import com.example.Entity.Category;
import com.example.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

	@Autowired
    private  CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> create(@RequestBody Category category) {

        System.out.println("Name: " + category.getName());
        System.out.println("Description: " + category.getDescription());
        System.out.println("Image URL: " + category.getImageUrl());

        return ResponseEntity.ok(categoryRepository.save(category));
    }
}