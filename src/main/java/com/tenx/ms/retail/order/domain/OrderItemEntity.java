package com.tenx.ms.retail.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_item")
@IdClass(OrderItemId.class)
public class OrderItemEntity {

	@Id
	@Column(name = "order_id")
	private Long orderId;

	@Id
	@Column(name = "product_id")
	private Long productId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "is_backordered", columnDefinition = "TINYINT")
	private boolean isBackOrdered;

	@ManyToOne
	@JoinColumn(name = "order_id", insertable = false, updatable = false)
	private OrderEntity order;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public boolean getIsBackOrdered() {
		return isBackOrdered;
	}

	public void setIsBackOrdered(Boolean isBackOrdered) {
		this.isBackOrdered = isBackOrdered;
	}

}
