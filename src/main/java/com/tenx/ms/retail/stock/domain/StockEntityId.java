package com.tenx.ms.retail.stock.domain;

import java.io.Serializable;

public class StockEntityId implements Serializable {

	private Long storeId;

	private Long productId;

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
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

		StockEntityId stockEntityId = (StockEntityId) o;

		return storeId.equals(stockEntityId.storeId) && productId.equals(stockEntityId.productId);
	}

	@Override
	public int hashCode() {
		return (int) (storeId + productId);
	}
}
