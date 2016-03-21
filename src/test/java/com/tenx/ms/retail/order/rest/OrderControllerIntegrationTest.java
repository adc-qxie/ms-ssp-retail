package com.tenx.ms.retail.order.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.TestConstants;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.stock.repository.StockRepository;
import com.tenx.ms.retail.store.repository.StoreRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class OrderControllerIntegrationTest extends AbstractIntegrationTest {

	private static final String BASE_REQUEST_URI = "%s/%s/orders/%s";
	private static final String STORE_BASE_REQUEST_URI = "%s/%s/stores";
	private static final String PRODUCT_BASE_REQUEST_URI = "%s/%s/products/%s";
	private static final String STOCK_BASE_REQUEST_URI = "%s/%s/stock/%s/%s";

	private static final String EMAIL = "abc@example.com";
	private static final String FIRST_NAME = "qing";
	private static final String LAST_NAME = "xie";
	private static final String PHONE = "1234567890";
	private static final long PRODUCT_ID = 1L;
	private static final long STORE_ID = 1L;
	private static final long ORDER_ID = 1L;
	private static final int ORDER_QUANTITY = 5;

	private static final Set<OrderItem> ITEMS = ImmutableSet.of(new OrderItem().setProductId(PRODUCT_ID).setQuantity(ORDER_QUANTITY).setIsBackOrdered(false));

	private final RestTemplate template = new TestRestTemplate();

	@Value("classpath:productTests/product-create-request.json")
	private File productCreateRequest;

	@Value("classpath:storeTests/store-create-request.json")
	private File storeCreateRequest;

	@Value("classpath:stockTests/stock-update-request.json")
	private File stockUpdateRequest;

	@Value("classpath:orderTests/order-create-request.json")
	private File orderCreateRequest;

	@Value("classpath:orderTests/order-create-not-enough-stock-request.json")
	private File orderCreateNotEnoughStockRequest;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ProductRepository productRepository;

	private Order createNewOrder() {
		return new Order().setEmail(EMAIL).setFirstName(FIRST_NAME).setLastName(LAST_NAME).setPhone(PHONE).setItems(ITEMS);
	}

	private void prepareTestWithStoreAndProductAndStock() throws IOException {
		String url = String.format(STORE_BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1);

		getJSONResponse(template, url, FileUtils.readFileToString(storeCreateRequest), HttpMethod.POST);

		url = String.format(PRODUCT_BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID);

		getJSONResponse(template, url, FileUtils.readFileToString(productCreateRequest), HttpMethod.POST);

		url = String.format(STOCK_BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID, PRODUCT_ID);

		getJSONResponse(template, url, FileUtils.readFileToString(stockUpdateRequest), HttpMethod.PUT);

	}

	@Test
	public void testCreateOrder() {

		try {
			prepareTestWithStoreAndProductAndStock();
			String url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1, STORE_ID);

			ResponseEntity<String> response = getJSONResponse(template, url, FileUtils.readFileToString(orderCreateRequest), HttpMethod.POST);

			ObjectMapper mapper = new ObjectMapper();
			Order received = mapper.readValue(response.getBody(), Order.class);

			long orderId = received.getOrderId();
			assertTrue("new order created with orderId: " + orderId, orderId > 0);

			boolean isBackOrdered = received.getItems().iterator().next().getIsBackOrdered();
			assertEquals("With enough stocks", isBackOrdered, false);

		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
