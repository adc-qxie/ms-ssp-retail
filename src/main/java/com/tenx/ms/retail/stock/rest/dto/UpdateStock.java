package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel("Request payload to update stock quantity")
public class UpdateStock {

	@ApiModelProperty("The quantity of the product to be added into (positive number) / deleted from (negative number) the stock ")
	@JsonProperty("count")
	@NotNull(message = "quantity is null")
	private Integer quantity;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
