package com.tenx.ms.retail.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "order_item")
@IdClass(OrderItemId.class)
public class OrderItemEntity {

	@Id
	private Long orderId;

	@Id
	private Long productId;

	@Column(name = "quantity")
	private Integer quantity;

	@ManyToOne
	@PrimaryKeyJoinColumn(name = "order_id", referencedColumnName = "order_id")
	private OrderEntity purchaseOrder;

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
		return purchaseOrder;
	}

	public void setOrder(OrderEntity purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

}
