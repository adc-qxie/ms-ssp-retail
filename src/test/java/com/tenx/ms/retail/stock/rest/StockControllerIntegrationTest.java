package com.tenx.ms.retail.stock.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.TestConstants;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.store.repository.StoreRepository;

@SuppressWarnings("PMD")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class StockControllerIntegrationTest extends AbstractIntegrationTest {

	private static final String BASE_REQUEST_URI = "%s/%s/stock/%s/%s";
	private static final String PRODUCT_BASE_REQUEST_URI = "%s/%s/products/%s";
	private static final String STORE_BASE_REQUEST_URI = "%s/%s/stores";

	private static final int STOCK_COUNT = 10;
	private static final long STORE_ID = 1L;
	private static final long PRODUCT_ID = 1L;
	private final RestTemplate template = new TestRestTemplate();

	@Value("classpath:stockTests/stock-get-response.json")
	private File stockGetResponse;

	@Value("classpath:stockTests/stock-update-request.json")
	private File stockUpdateRequest;

	@Value("classpath:stockTests/stock-update-invalid-request.json")
	private File stockUpdateInvalidRequest;

	@Value("classpath:storeTests/store-create-request.json")
	private File storeCreateRequest;

	@Value("classpath:productTests/product-create-request.json")
	private File productCreateRequest;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ProductRepository productRepository;

	private Stock createNewStock() {
		return new Stock().setQuantity(STOCK_COUNT).setStoreId(STORE_ID).setProductId(PRODUCT_ID);
	}

	private void prepareTestWithStoreAndProduct() throws IOException {
		String url = String.format(STORE_BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1);

		getJSONResponse(template, url, FileUtils.readFileToString(storeCreateRequest), HttpMethod.POST);

		url = String.format(PRODUCT_BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID);

		getJSONResponse(template, url, FileUtils.readFileToString(productCreateRequest), HttpMethod.POST);

	}

	@Test
	public void testUpdateStock() {
		Stock expectedStock = createNewStock();

		try {
			prepareTestWithStoreAndProduct();

			String url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID, PRODUCT_ID);

			ResponseEntity<String> response = getJSONResponse(template, url, FileUtils.readFileToString(stockUpdateRequest), HttpMethod.PUT);

			assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
			StockEntity received = stockRepository.findOneByStoreIdAndProductId(STORE_ID, PRODUCT_ID).get();
			assertEquals("remaining quantity does not match", expectedStock.getQuantity(), received.getQuantity());

			stockRepository.deleteAll();
			storeRepository.deleteAll();
			productRepository.deleteAll();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUpdateStockWithInsufficientStock() {

		try {
			prepareTestWithStoreAndProduct();

			String url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID, PRODUCT_ID);

			ResponseEntity<String> response = getJSONResponse(template, url, FileUtils.readFileToString(stockUpdateInvalidRequest), HttpMethod.PUT);

			assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());

			stockRepository.deleteAll();
			storeRepository.deleteAll();
			productRepository.deleteAll();

		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testStockNotFound() {
		ResponseEntity<String> response = getJSONResponse(template, String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, 200L, 1L), null, HttpMethod.GET);

		assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, response.getStatusCode());

		response = getJSONResponse(template, String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, 1L, 200L), null, HttpMethod.GET);

		assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, response.getStatusCode());

	}

}
