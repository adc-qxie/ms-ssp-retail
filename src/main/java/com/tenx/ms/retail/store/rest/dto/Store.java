package com.tenx.ms.retail.store.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Store {

	@ApiModelProperty("The store ID - (readonly)")
	@JsonProperty("store_id")
	private long storeId;

	@ApiModelProperty("The store name")
	@JsonProperty("name")
	private String storeName;

	public long getStoreId() {
		return storeId;
	}

	public Store setStoreId(long storeId) {
		this.storeId = storeId;
		return this;
	}

	public String getStoreName() {
		return storeName;
	}

	public Store setStoreName(String storeName) {
		this.storeName = storeName;
		return this;
	}
}