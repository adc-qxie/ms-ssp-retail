package com.tenx.ms.retail.store.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tenx.ms.retail.store.domain.StoreEntity;

@org.springframework.stereotype.Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

	Optional<StoreEntity> findOneByStoreId(final Long storeId);

	Page<StoreEntity> findByStoreName(final String storeName, Pageable pageable);
}
