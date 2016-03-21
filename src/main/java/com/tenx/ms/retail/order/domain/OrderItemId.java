package com.tenx.ms.retail.order.domain;

import java.io.Serializable;

public class OrderItemId implements Serializable {

	private Long orderId;

	private Long productId;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		OrderItemId orderItem = (OrderItemId) o;

		return orderId.equals(orderItem.orderId) && productId.equals(orderItem.productId);
	}

	@Override
	public int hashCode() {
		return (int) (orderId + productId);
	}
}
