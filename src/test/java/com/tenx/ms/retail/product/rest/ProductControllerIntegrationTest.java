package com.tenx.ms.retail.product.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.TestConstants;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.store.repository.StoreRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class ProductControllerIntegrationTest extends AbstractIntegrationTest {

	private static final String BASE_REQUEST_URI = "%s/%s/products/%s";
	private static final String STORE_BASE_REQUEST_URI = "%s/%s/stores";
	private static final double PRICE = 100.2;
	private static final String PRODUCT_NAME = "shoe2";
	private static final String PRODUCT_DESCRIPTION = "a pair of boots";
	private static final String SKU = "ssbc134";
	private static final long PRODUCT_ID = 1L;
	private static final long STORE_ID = 1L;
	private final RestTemplate template = new TestRestTemplate();

	@Value("classpath:productTests/product-get-response.json")
	private File productGetResponse;

	@Value("classpath:productTests/product-create-request.json")
	private File productCreateRequest;

	@Value("classpath:storeTests/store-create-request.json")
	private File storeCreateRequest;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ProductRepository productRepository;

	private Product createNewProduct() {
		return new Product().setPrice(PRICE).setProductName(PRODUCT_NAME).setSku(SKU).setProductDescription(PRODUCT_DESCRIPTION).setStoreId(STORE_ID).setProductId(PRODUCT_ID);
	}

	private void prepareTestWithStore() throws IOException {
		String url = String.format(STORE_BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1);

		getJSONResponse(template, url, FileUtils.readFileToString(storeCreateRequest), HttpMethod.POST);

	}

	private void prepareTestWithStoreAndProduct() throws IOException {
		String url = String.format(STORE_BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1);

		getJSONResponse(template, url, FileUtils.readFileToString(storeCreateRequest), HttpMethod.POST);

		url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID);

		getJSONResponse(template, url, FileUtils.readFileToString(productCreateRequest), HttpMethod.POST);

	}

	@Test
	public void testCreateProduct() {
		Product expectedProduct = createNewProduct();

		try {
			prepareTestWithStore();
			String url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID);

			ResponseEntity<String> response = getJSONResponse(template, url, FileUtils.readFileToString(productCreateRequest), HttpMethod.POST);

			ObjectMapper mapper = new ObjectMapper();
			ResourceCreated received = mapper.readValue(response.getBody(), ResourceCreated.class);

			long productId = ((Number) (received.getId())).longValue();
			assertTrue("new product created with productId: " + productId, productId > 0);

			assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
			storeRepository.delete(productId);

		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testProductNotFound() {
		ResponseEntity<String> response = getJSONResponse(template, String.format(BASE_REQUEST_URI + "/%s", basePath(), TestConstants.API_VERSION_1, 200L, 1L), null,
				HttpMethod.GET);

		assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, response.getStatusCode());

		response = getJSONResponse(template, String.format(BASE_REQUEST_URI + "%s", basePath(), TestConstants.API_VERSION_1, 1L, 200L), null, HttpMethod.GET);

		assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void testGetProduct() {
		Product expectedNewProduct = createNewProduct();

		try {

			prepareTestWithStoreAndProduct();
			String url = String.format(BASE_REQUEST_URI + "/%s", basePath(), TestConstants.API_VERSION_1, STORE_ID, PRODUCT_ID);
			ResponseEntity<String> response = getJSONResponse(template, url, null, HttpMethod.GET);

			String expected = FileUtils.readFileToString(productGetResponse);
			String received = response.getBody();

			JSONAssert.assertEquals(expected, received, false);

			storeRepository.deleteAll();

			assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
		} catch (JSONException | IOException e) {
			fail(e.getMessage());
		}

	}
}
