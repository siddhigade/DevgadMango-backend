package com.example.service;

import com.example.Entity.Address;
import com.example.Entity.User;
import com.example.dto.AddressRequest;
import com.example.repository.AddressRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

	@Autowired
    private  AddressRepository addressRepository;
	@Autowired
    private  UserRepository userRepository;

    public List<Address> getAddresses(String username) {
        User user = resolveUser(username);
        return addressRepository.findByUserId(user.getId());
    }

    public Address createAddress(String username, AddressRequest req) {
        User user = resolveUser(username);

        Address address = new Address();

        address.setFullName(req.getFullName());
        address.setPhone(req.getPhone());
        address.setAddressLine1(req.getAddressLine1());
        address.setAddressLine2(req.getAddressLine2());
        address.setCity(req.getCity());
        address.setState(req.getState());
        address.setPincode(req.getPincode());
        address.setCountry(
                req.getCountry() != null && !req.getCountry().isBlank()
                        ? req.getCountry()
                        : "India"
        );
        address.setUser(user);

        return addressRepository.save(address);
    }

    private User resolveUser(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}