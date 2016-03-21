package com.tenx.ms.retail.order.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenx.ms.retail.order.domain.OrderItemEntity;
import com.tenx.ms.retail.order.domain.OrderItemId;

@org.springframework.stereotype.Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemId> {

	public Set<OrderItemEntity> findAllByOrderId(Long orderId);
}