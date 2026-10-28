package com.example.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public RegisterRequest(@NotBlank String name, @NotBlank @Email String email,
			@NotBlank @Size(min = 6) String password,
			@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number") String phone) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}

	public RegisterRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}

