package com.tenx.ms.retail.store.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the request payload used in creating a new store
 */
@ApiModel("Request payload to create store")
public class CreateStore {

	@NotBlank(message = "Store name is blank")
	@ApiModelProperty(value = "The store name - must be unique", required = true)
	@JsonProperty("name")
	private String storeName;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

}
