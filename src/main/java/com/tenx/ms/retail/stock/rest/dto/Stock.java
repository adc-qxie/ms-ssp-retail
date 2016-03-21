package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel("Returns the stock information")
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

	public Stock setStoreId(long storeId) {
		this.storeId = storeId;
		return this;
	}

	public long getProductId() {
		return productId;
	}

	public Stock setProductId(long productId) {
		this.productId = productId;
		return this;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Stock setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

}