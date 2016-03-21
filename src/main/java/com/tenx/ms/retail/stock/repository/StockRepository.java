package com.tenx.ms.retail.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.domain.StockEntityId;

@org.springframework.stereotype.Repository
public interface StockRepository extends JpaRepository<StockEntity, StockEntityId> {

	Optional<StockEntity> findOneByStoreIdAndProductId(final Long storeId, final Long productId);

}
