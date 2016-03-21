package com.tenx.ms.retail.product.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel("Returns the product information")
public class Product {

	@ApiModelProperty("The product ID - (readonly)")
	@JsonProperty("product_id")
	private long productId;

	@NotBlank
	@ApiModelProperty("The product name")
	@JsonProperty("name")
	private String productName;

	@ApiModelProperty("The store ID - (readonly)")
	@JsonProperty("store_id")
	private long storeId;

	@ApiModelProperty("The product description")
	@JsonProperty("description")
	private String productDescription;

	@ApiModelProperty("The product sku - alpha numeric with length 5-10")
	@JsonProperty("sku")
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	@Size(min = 5, max = 10)
	private String sku;

	@NotNull
	@ApiModelProperty("The product unit price")
	@JsonProperty("price")
	private Double price;

	public long getProductId() {
		return productId;
	}

	public Product setProductId(long productId) {
		this.productId = productId;
		return this;
	}

	public String getProductName() {
		return productName;
	}

	public Product setProductName(String productName) {
		this.productName = productName;
		return this;
	}

	public long getStoreId() {
		return storeId;
	}

	public Product setStoreId(long storeId) {
		this.storeId = storeId;
		return this;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public Product setProductDescription(String productDescription) {
		this.productDescription = productDescription;
		return this;
	}

	public String getSku() {
		return sku;
	}

	public Product setSku(String sku) {
		this.sku = sku;
		return this;
	}

	public Double getPrice() {
		return price;
	}

	public Product setPrice(Double price) {
		this.price = price;
		return this;
	}
}