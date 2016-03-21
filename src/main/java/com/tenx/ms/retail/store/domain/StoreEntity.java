package com.tenx.ms.retail.store.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.tenx.ms.retail.product.domain.ProductEntity;

@Entity
@Table(name = "store")
public class StoreEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "store_id")
	private Long storeId;

	@NotBlank
	@Size(max = 50)
	@Column(name = "name", length = 50)
	private String storeName;

	@ManyToMany
	@JoinTable(name = "store_product", joinColumns = { @JoinColumn(name = "store_id", referencedColumnName = "store_id") }, inverseJoinColumns = { @JoinColumn(name = "product_id", referencedColumnName = "product_id") })
	private Set<ProductEntity> products = new HashSet<>();

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Set<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductEntity> products) {
		this.products = products;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		StoreEntity store = (StoreEntity) o;

		return storeId.equals(store.storeId);
	}

	@Override
	public int hashCode() {
		if (storeId != null)
			return storeId.hashCode();
		else
			return storeName.hashCode();
	}
}