package com.tenx.ms.retail.product.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "product")
public class ProductEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private Long productId;

	@NotNull
	@Column(name = "store_id")
	private Long storeId;

	@NotBlank
	@Size(max = 50)
	@Column(name = "name", length = 50)
	private String productName;

	@Size(max = 255)
	@Column(name = "description", length = 255)
	private String productDescription;

	@Pattern(regexp = "^[A-Za-z0-9]+$")
	@Size(min = 5, max = 10)
	@Column(name = "sku")
	private String productSku;

	@NotNull
	@Column(name = "price", columnDefinition = "Decimal(10,2)")
	@Min(value = 0, message = "price shohuld be positive number")
	private Double price;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

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

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ProductEntity product = (ProductEntity) o;

		return productId.equals(product.productId);
	}

	@Override
	public int hashCode() {
		if (productId != null)
			return productId.hashCode();
		else
			return storeId.hashCode() + productName.hashCode();
	}
}