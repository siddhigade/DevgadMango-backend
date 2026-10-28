package com.example.dto;



import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    private Long categoryId;

    private String imageUrl;

    @Min(0)
    private Integer stock;

    private List<VariantRequest> variants;
    

    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public Long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public Integer getStock() {
		return stock;
	}


	public void setStock(Integer stock) {
		this.stock = stock;
	}


	public List<VariantRequest> getVariants() {
		return variants;
	}


	public void setVariants(List<VariantRequest> variants) {
		this.variants = variants;
	}

	

	@Data
    public static class VariantRequest {
        @NotBlank
        private String label;

        @NotNull @DecimalMin("0.0")
        private BigDecimal price;

        @Min(0)
        private Integer stock;

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public Integer getStock() {
			return stock;
		}

		public void setStock(Integer stock) {
			this.stock = stock;
		}
        
    }
}
