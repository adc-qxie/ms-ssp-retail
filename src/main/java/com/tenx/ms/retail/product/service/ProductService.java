package com.tenx.ms.retail.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.CreateProduct;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;

@Service
public class ProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private StockRepository stockRepository;

	public Optional<Product> getProductByID(Long storeId, Long productId) {
		return productRepository.findOneByStoreIdAndProductId(storeId, productId).map(product -> convertToDTO(product));
	}

	public Optional<Product> getProductByName(Long storeId, String productName) {
		return productRepository.findOneByStoreIdAndProductName(storeId, productName).map(product -> convertToDTO(product));
	}

	/**
	 * Retrieves All products for a store
	 * 
	 * @param pageable
	 *            the pageable options used for paginated results
	 * @param baseLinkPath
	 *            the base path to the resource used for pagination link generation
	 * @return Paginated Iterable of Stores
	 */
	@SuppressWarnings("unchecked")
	public Paginated<Product> getAllProductsByStoreId(Long storeId, Pageable pageable, String baseLinkPath) {
		StoreEntity store = storeRepository.findOneByStoreId(storeId).get();
		LOGGER.info("store {} with {}", store.getStoreName(), store.getProducts());

		List<ProductEntity> products = new ArrayList<>(store.getProducts());
		Page<ProductEntity> page = new PageImpl<ProductEntity>(products, pageable, products.size());

		List<Product> prods = page.getContent().stream().map(product -> convertToDTO(product)).collect(Collectors.toList());

		return Paginated.wrap(page, prods, baseLinkPath);
	}

	@Transactional
	public Product createProduct(CreateProduct createProduct) {
		Optional<Product> existingProduct = getProductByName(createProduct.getStoreId(), createProduct.getProductName());
		if (existingProduct.isPresent())
			return null;
		ProductEntity product = convertFromDTO(createProduct);
		product = productRepository.save(product);

		StoreEntity store = storeRepository.findOneByStoreId(createProduct.getStoreId()).get();
		store.getProducts().add(product);
		storeRepository.save(store);

		StockEntity stock = new StockEntity();
		stock.setProductId(product.getProductId());
		stock.setStoreId(product.getStoreId());
		stock.setQuantity(0);
		stockRepository.save(stock);

		LOGGER.info("store {} with product {}", store.getStoreId(), store.getProducts());
		return convertToDTO(product);
	}

	private Product convertToDTO(ProductEntity product) {
		Product p = new Product();
		p.setProductId(product.getProductId());
		p.setStoreId(product.getStoreId());
		p.setProductName(product.getProductName());
		p.setPrice(product.getPrice());
		p.setProductDescription(product.getProductDescription());
		p.setSku(product.getProductSku());
		return p;
	}

	private ProductEntity convertFromDTO(CreateProduct product) {
		ProductEntity p = new ProductEntity();
		p.setStoreId(product.getStoreId());
		p.setProductName(product.getProductName());
		p.setPrice(product.getPrice());
		p.setProductDescription(product.getProductDescription());
		p.setProductSku(product.getSku());
		return p;
	}

}
