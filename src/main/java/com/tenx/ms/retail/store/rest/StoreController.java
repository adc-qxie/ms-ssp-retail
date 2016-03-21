package com.tenx.ms.retail.store.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tenx.ms.commons.rest.APIError;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.store.rest.dto.CreateStore;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.service.StoreService;

@Api(value = "/stores", description = "Store Management API")
@RestController("storeControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stores")
public class StoreController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreController.class);

	@Autowired
	private StoreService storeService;

	@ApiOperation(value = "Get Store Information by ID", notes = "The GET store endpoint returns information about the store with the given storeId.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of the store detail"),
			@ApiResponse(code = 404, message = "StoreEntity with given id does not exist"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}" }, method = RequestMethod.GET)
	public Store getStoreById(@ApiParam(name = "storeId", value = "The id of the store to get its details", required = true) @PathVariable long storeId) {

		LOGGER.debug("Fetching store with id {}", storeId);

		return storeService.getStoreByID(storeId).get();

	}

	@ApiOperation(value = "Finds all stores or filter by store name", notes = "The GET store endpoint returns information of all the existing stores.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of stores"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(method = RequestMethod.GET)
	public Paginated<Store> getAllStores(Pageable pageable,
			@ApiParam(name = "storeName", value = "The name of the store") @RequestParam(required = false, value = "name") String storeName) {

		LOGGER.debug("Fetching all stores: {}", pageable);

		if (StringUtils.isNotBlank(storeName)) {
			return storeService.getStoreByName(pageable, RestConstants.VERSION_ONE + "/stores", storeName);
		} else {
			return storeService.getAllStores(pageable, RestConstants.VERSION_ONE + "/stores");
		}

	}

	@ApiOperation(value = "Creates a new Store", notes = "The POST store endpoint creates a new store and returns storeId", response = ResourceCreated.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful creation of the store"), @ApiResponse(code = 400, message = "If store exists"),
			@ApiResponse(code = 412, message = "Validation failure"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createStore(@ApiParam(value = "create store object", required = true) @Validated @RequestBody CreateStore createStore, HttpServletRequest request) {
		LOGGER.info("Creating a new store: {} ", createStore.getStoreName());
		HttpHeaders headers = new HttpHeaders();
		Store store = storeService.createStore(createStore);
		if (store == null)
			// TODO:create a generic error message
			return new ResponseEntity<>(new APIError("Store exception", 400, "resource exists", "store name already exists"), headers, HttpStatus.BAD_REQUEST);
		Long storeId = store.getStoreId();
		headers.add(HttpHeaders.LOCATION, request.getRequestURL().append(storeId).toString());
		return new ResponseEntity<>(new ResourceCreated<>(storeId), headers, HttpStatus.CREATED);

	}

}
