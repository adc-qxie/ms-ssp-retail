package com.tenx.ms.retail.order.service;

import java.util.Date;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tenx.ms.retail.order.domain.OrderEntity;
import com.tenx.ms.retail.order.domain.OrderEntity.OrderStatus;
import com.tenx.ms.retail.order.domain.OrderItemEntity;
import com.tenx.ms.retail.order.repository.OrderItemRepository;
import com.tenx.ms.retail.order.repository.OrderRepository;
import com.tenx.ms.retail.order.rest.dto.CreateOrder;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
import com.tenx.ms.retail.store.repository.StoreRepository;

@Service
public class OrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private StockService stockService;

	public Order getOrderByStoreIdAndOrderId(Long storeId, Long orderId) {
		Set<OrderItemEntity> items = orderItemRepository.findAllByOrderId(orderId);
		OrderEntity order = orderRepository.findOneByStoreIdAndOrderId(storeId, orderId).get();
		order.setItems(items);
		return convertToDTO(order);
	}

	@Transactional
	public Order createOrder(CreateOrder createOrder, long storeId) {
		OrderEntity order = convertFromDTO(createOrder);
		order.setStoreId(storeId);

		order = orderRepository.save(order);

		for (OrderItem item : createOrder.getItems()) {
			OrderItemEntity orderItem = covertOrderItemFromDTO(item);

			Stock stock = stockService.addStock(storeId, item.getProductId(), 0 - item.getQuantity());
			if (stock == null)
				orderItem.setIsBackOrdered(true);

			orderItem.setOrderId(order.getOrderId());
			orderItem = orderItemRepository.save(orderItem);

			order.getItems().add(orderItem);
		}

		order = orderRepository.save(order);
		return convertToDTO(order);
	}

	private OrderEntity convertFromDTO(CreateOrder order) {
		OrderEntity o = new OrderEntity();
		o.setEmail(order.getEmail());
		o.setFirstName(order.getFirstName());
		o.setLastName(order.getLastName());
		o.setPhone(order.getPhone());
		Date now = new Date();
		o.setOrderDate(now);
		o.setStatus(OrderStatus.ORDERED);
		return o;
	}

	private OrderItemEntity covertOrderItemFromDTO(OrderItem item) {
		OrderItemEntity oi = new OrderItemEntity();
		oi.setProductId(item.getProductId());
		oi.setQuantity(item.getQuantity());
		oi.setIsBackOrdered(item.getIsBackOrdered());
		return oi;
	}

	private Order convertToDTO(OrderEntity order) {
		Order o = new Order();
		o.setEmail(order.getEmail());
		o.setFirstName(order.getFirstName());
		o.setLastName(order.getLastName());
		o.setOrderDate(order.getOrderDate());
		o.setPhone(order.getPhone());
		o.setStoreId(order.getStoreId());
		o.setOrderId(order.getOrderId());
		o.setStatus(order.getStatus().name());
		for (OrderItemEntity item : order.getItems()) {
			o.getItems().add(convertOrderItemToDTO(item));
		}

		return o;
	}

	private OrderItem convertOrderItemToDTO(OrderItemEntity item) {
		OrderItem oi = new OrderItem();
		oi.setProductId(item.getProductId());
		oi.setQuantity(item.getQuantity());
		oi.setIsBackOrdered(item.getIsBackOrdered());
		return oi;
	}
}
