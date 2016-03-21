package com.tenx.ms.retail.stock.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateStock {

	@ApiModelProperty("The quantity of the product to be added into/deleted from (negative number) the stock ")
	@JsonProperty("count")
	@NotNull
	private Integer quantity;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
