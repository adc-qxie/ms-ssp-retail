package com.tenx.ms.retail.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenx.ms.retail.product.domain.ProductEntity;

@org.springframework.stereotype.Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Optional<ProductEntity> findOneByStoreIdAndProductId(final Long storeId, final Long productId);

	Optional<ProductEntity> findOneByStoreIdAndProductName(final Long storeId, final String productName);
}
