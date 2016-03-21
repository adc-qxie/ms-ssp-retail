package com.tenx.ms.retail.stock.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;

@Service
public class StockService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

	@Autowired
	private StockRepository stockRepository;

	public Optional<Stock> getStockByStoreIdAndProductId(Long storeId, Long productId) {
		return stockRepository.findOneByStoreIdAndProductId(storeId, productId).map(stock -> convertToDTO(stock));
	}

	public Stock addStock(Long storeId, Long productId, Integer count) {

		StockEntity existingStock = stockRepository.findOneByStoreIdAndProductId(storeId, productId).get();

		int quantity = existingStock.getQuantity() + count;

		if (quantity < 0)
			return null;

		StockEntity stock = new StockEntity();
		stock.setProductId(productId);
		stock.setStoreId(storeId);
		stock.setQuantity(quantity);

		return convertToDTO(stockRepository.save(stock));
	}

	private Stock convertToDTO(StockEntity stock) {
		Stock s = new Stock();
		s.setStoreId(stock.getStoreId());
		s.setProductId(stock.getProductId());
		s.setQuantity(stock.getQuantity());
		return s;
	}
}