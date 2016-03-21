package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel("Request payload to create order")
public class CreateOrder {

	@Pattern(regexp = "^[A-Za-z]+$")
	@JsonProperty("first_name")
	@NotEmpty(message = "first name is empty")
	@ApiModelProperty(value = "first name of the order purchaser", required = true)
	private String firstName;

	@ApiModelProperty(value = "last name of the order purchaser", required = true)
	@Pattern(regexp = "^[A-Za-z]+$")
	@JsonProperty("last_name")
	@NotEmpty(message = "last name is empty")
	private String lastName;

	@Email
	@NotEmpty(message = "email is empty")
	@ApiModelProperty(value = "email of the order purchaser", required = true)
	private String email;

	@Pattern(regexp = "^[0-9]+$")
	@Size(min = 10, max = 10)
	@NotEmpty(message = "phone is empty")
	@ApiModelProperty(value = "phone number of the order purchaser", required = true)
	private String phone;

	@NotEmpty(message = "order items are empty")
	@ApiModelProperty(value = "list of the items purchased", required = true)
	private Set<OrderItem> items;

	public Set<OrderItem> getItems() {
		return items;
	}

	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
