package com.example.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginRequest(@NotBlank @Email String email, @NotBlank String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public LoginRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    
}

