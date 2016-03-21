package com.tenx.ms.retail.product.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel("Request payload to create product")
public class CreateProduct {

	@NotBlank(message = "product name is blank")
	@ApiModelProperty(value = "The product name", required = true)
	@JsonProperty("name")
	private String productName;

	@ApiModelProperty(value = "The product description", required = false)
	@JsonProperty("description")
	private String productDescription;

	@ApiModelProperty(value = "The product sku - alpha numeric with length 5-10", required = true)
	@JsonProperty("sku")
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "sku value should be alpha numeric with length 5-10")
	@Size(min = 5, max = 10, message = "sku length should be 5-10")
	@NotNull(message = "sku is null")
	private String sku;

	@NotNull(message = "price is null")
	@ApiModelProperty(value = "The product unit price - cannot be negative", required = true)
	@JsonProperty("price")
	@Min(value = 0, message = "price is negative")
	private Double price;

	@JsonIgnore
	private Long storeId;

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}