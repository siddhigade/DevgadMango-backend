package com.example.dto;


import lombok.*;

@Data
@AllArgsConstructor
public class RazorpayOrderResponse {
    private String razorpayOrderId;
    private Long amount;       // in paise
    private String currency;
    private Long dbOrderId;    // our internal order ID
	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}
	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Long getDbOrderId() {
		return dbOrderId;
	}
	public void setDbOrderId(Long dbOrderId) {
		this.dbOrderId = dbOrderId;
	}
	public RazorpayOrderResponse(String razorpayOrderId, Long amount, String currency, Long dbOrderId) {
		super();
		this.razorpayOrderId = razorpayOrderId;
		this.amount = amount;
		this.currency = currency;
		this.dbOrderId = dbOrderId;
	}
	public RazorpayOrderResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}