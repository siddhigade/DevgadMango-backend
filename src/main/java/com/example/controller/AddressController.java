package com.example.controller;

import com.example.Entity.Address;
import com.example.dto.AddressRequest;
import com.example.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

	@Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(addressService.getAddresses(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest req) {
        return ResponseEntity.ok(addressService.createAddress(userDetails.getUsername(), req));
    }
}