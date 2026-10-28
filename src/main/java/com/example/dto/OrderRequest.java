package com.example.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull
    private Long addressId;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public OrderRequest(@NotNull Long addressId) {
		super();
		this.addressId = addressId;
	}

	public OrderRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}

