package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel("Return the order information")
public class Order {

	@ApiModelProperty("The order ID - (readonly)")
	@JsonProperty("order_id")
	private Long orderId;

	@ApiModelProperty("The store ID - (readonly)")
	@JsonProperty("store_id")
	private Long storeId;

	@ApiModelProperty("The date/time the order is placed")
	@JsonProperty("order_date")
	private Date orderDate;

	@ApiModelProperty(value = "The status of the order", allowableValues = "ORDERED,PACKING,SHIPPED")
	private String status;

	@Pattern(regexp = "^[A-Za-z]+$")
	@JsonProperty("first_name")
	@NotEmpty
	@ApiModelProperty("The first name of the order purchaser")
	private String firstName;

	@Pattern(regexp = "^[A-Za-z]+$")
	@JsonProperty("last_name")
	@NotEmpty
	@ApiModelProperty("The last name of the order purchaser")
	private String lastName;

	@Email
	@NotEmpty
	@ApiModelProperty("The email of the order purchaser")
	private String email;

	@Pattern(regexp = "^[0-9]+$")
	@Size(min = 10, max = 10)
	@NotEmpty
	@ApiModelProperty("The phone number of the order purchaser")
	private String phone;

	@NotEmpty
	@ApiModelProperty("The list of items included in the order")
	private Set<OrderItem> items = new HashSet<>();

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Set<OrderItem> getItems() {
		return items;
	}

	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}

}
