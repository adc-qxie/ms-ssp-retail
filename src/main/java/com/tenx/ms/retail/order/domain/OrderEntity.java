package com.tenx.ms.retail.order.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "purchase_order")
public class OrderEntity implements Serializable {

	public enum OrderStatus {
		ORDERED, PACKING, SHIPPED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private Long orderId;

	@NotNull
	@Column(name = "store_id")
	private Long storeId;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "order_date")
	private Date orderDate;

	@NotNull
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private OrderStatus status;

	@OneToMany(mappedBy = "order")
	private Set<OrderItemEntity> items = new HashSet<>();

	@Pattern(regexp = "^[A-Za-z]+$")
	@Size(max = 50)
	@Column(name = "first_name", length = 50)
	private String firstName;

	@Pattern(regexp = "^[A-Za-z]+$")
	@Size(max = 50)
	@Column(name = "last_name", length = 50)
	private String lastName;

	@Email
	@Column(name = "email")
	private String email;

	@Pattern(regexp = "^[0-9]+$")
	@Size(min = 10, max = 10)
	@Column(name = "phone")
	private String phone;

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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
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

	public Set<OrderItemEntity> getItems() {
		return items;
	}

	public void setItems(Set<OrderItemEntity> items) {
		this.items = items;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		OrderEntity order = (OrderEntity) o;

		return orderId.equals(order.orderId);
	}

	@Override
	public int hashCode() {
		if (orderId != null)
			return orderId.hashCode();
		else
			return storeId.hashCode() + orderDate.hashCode() + email.hashCode();
	}
}
