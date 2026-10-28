package com.example.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CartRequest {
    @NotNull
    private Long productId;

    private Long variantId;  // optional

    @NotNull @Min(1)
    private Integer quantity;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getVariantId() {
		return variantId;
	}

	public void setVariantId(Long variantId) {
		this.variantId = variantId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public CartRequest(@NotNull Long productId, Long variantId, @NotNull @Min(1) Integer quantity) {
		super();
		this.productId = productId;
		this.variantId = variantId;
		this.quantity = quantity;
	}

	public CartRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
