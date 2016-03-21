package com.tenx.ms.retail.store.rest;

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
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.Store;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class StoreControllerIntegrationTest extends AbstractIntegrationTest {

	private static final String BASE_REQUEST_URI = "%s/%s/stores";
	private static final String STORE_NAME = "my first store";
	private static final long ID = 1L;
	private final RestTemplate template = new TestRestTemplate();

	@Value("classpath:storeTests/store-get-response.json")
	private File storeGetResponse;

	@Value("classpath:storeTests/store-create-request.json")
	private File storeCreateRequest;

	@Autowired
	private StoreRepository storeRepository;

	private Store createNewStore() {

		return new Store().setStoreName(STORE_NAME).setStoreId(ID);
	}

	private void prepareTestWithNewStore() throws IOException {
		String url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1);

		getJSONResponse(template, url, FileUtils.readFileToString(storeCreateRequest), HttpMethod.POST);

	}

	@Test
	public void testCreateStore() {
		try {
			String url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1);

			ResponseEntity<String> response = getJSONResponse(template, url, FileUtils.readFileToString(storeCreateRequest), HttpMethod.POST);

			ObjectMapper mapper = new ObjectMapper();
			ResourceCreated received = mapper.readValue(response.getBody(), ResourceCreated.class);

			long storeId = ((Number) (received.getId())).longValue();
			assertTrue("new store created with storeId: " + storeId, storeId > 0);

			assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
			storeRepository.delete(storeId);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateStoreWithSameName() {
		try {

			prepareTestWithNewStore();
			String url = String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1);

			ResponseEntity<String> response = getJSONResponse(template, url, FileUtils.readFileToString(storeCreateRequest), HttpMethod.POST);

			assertEquals("HTTP Status code incorrect", HttpStatus.BAD_REQUEST, response.getStatusCode());

			storeRepository.deleteAll();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetStore() {
		Store expectedNewStore = createNewStore();

		try {

			prepareTestWithNewStore();
			String url = String.format(BASE_REQUEST_URI + "/%s", basePath(), TestConstants.API_VERSION_1, expectedNewStore.getStoreId());
			ResponseEntity<String> response = getJSONResponse(template, url, null, HttpMethod.GET);

			String expected = FileUtils.readFileToString(storeGetResponse);
			String received = response.getBody();

			JSONAssert.assertEquals(expected, received, false);

			storeRepository.delete(ID);

			assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
		} catch (JSONException | IOException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testStoreNotFound() {
		ResponseEntity<String> response = getJSONResponse(template, String.format(BASE_REQUEST_URI + "/%s", basePath(), TestConstants.API_VERSION_1, 200L), null, HttpMethod.GET);

		assertEquals("HTTP Status code incorrect", HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testGetAllStore() {
		ResponseEntity<String> response = getJSONResponse(template, String.format(BASE_REQUEST_URI, basePath(), TestConstants.API_VERSION_1), null, HttpMethod.GET);
		assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
	}
}
