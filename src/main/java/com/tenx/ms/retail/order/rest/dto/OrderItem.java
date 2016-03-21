package com.tenx.ms.retail.order.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel("Ordered product information")
public class OrderItem {

	@NotNull(message = "product id is null")
	@JsonProperty("product_id")
	@ApiModelProperty("product id of the purchased product")
	private Long productId;

	@NotNull(message = "ordered quantity is null")
	@Min(value = 1, message = "the minimum number of ordered quantity cannot be less than 1")
	@JsonProperty("count")
	@ApiModelProperty("quantity of the purchased product - should be greater than 0")
	private Integer quantity;

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

}
