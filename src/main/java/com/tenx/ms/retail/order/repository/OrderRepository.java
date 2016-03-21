package com.tenx.ms.retail.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenx.ms.retail.order.domain.OrderEntity;

@org.springframework.stereotype.Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
	public Optional<OrderEntity> findOneByStoreIdAndOrderId(Long storeId, Long orderId);
}
