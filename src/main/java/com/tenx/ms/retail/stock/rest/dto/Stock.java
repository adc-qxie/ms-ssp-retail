package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stock {

	@ApiModelProperty("The store ID - (readonly)")
	@JsonProperty("store_id")
	private long storeId;

	@ApiModelProperty("The product ID - (readonly)")
	@JsonProperty("product_id")
	private long productId;

	@ApiModelProperty("The quantity of the product in stock")
	@JsonProperty("count")
	private Integer quantity;

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}