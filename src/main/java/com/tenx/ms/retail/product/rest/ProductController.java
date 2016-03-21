package com.tenx.ms.retail.product.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RestController;

import com.tenx.ms.commons.rest.APIError;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.rest.dto.CreateProduct;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.service.StoreService;

@Api(value = "product", description = "Product Management API")
@RestController("productControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/products/")
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private StoreService storeService;

	@ApiOperation(value = "Get Product Information by store ID and product ID", notes = "The GET product endpoint returns information about the product by given storeId and productId.", response = Product.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of the store detail"),
			@ApiResponse(code = 404, message = "UserEntity with given id does not exist"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}/{productId:\\d+}" }, method = RequestMethod.GET)
	public Product getProductById(@ApiParam(name = "storeId", value = "The id of the store the product belongs to", required = true) @PathVariable long storeId,
			@ApiParam(name = "productId", value = "The id of the product to get its details", required = true) @PathVariable long productId) {

		LOGGER.debug("Fetching product with storeId {}, product Id {}", storeId, productId);

		return productService.getProductByID(storeId, productId).get();

	}

	@ApiOperation(value = "Finds all products within a store", notes = "The GET product endpoint returns information of all the products in a store with the given storeId.", response = Paginated.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of products"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}" }, method = RequestMethod.GET)
	public Paginated<Product> getAllProductsByStoreId(
			@ApiParam(name = "storeId", value = "The id of the store the product belongs to", required = true) @PathVariable long storeId, Pageable pageable) {

		LOGGER.debug("Fetching all stores: {}", pageable);

		return productService.getAllProductsByStoreId(storeId, pageable, RestConstants.VERSION_ONE + "/products/{storeId}");
	}

	@ApiOperation(value = "Creates a new Product", notes = "The POST product endpoint creates the product in a store with the given storeId and returns the productId", response = ResourceCreated.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful creation of the product"), @ApiResponse(code = 400, message = "If product exists"),
			@ApiResponse(code = 412, message = "Validation failure"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}" }, method = RequestMethod.POST)
	public ResponseEntity<?> createProduct(@ApiParam(name = "storeId", value = "The id of the store the product belongs to", required = true) @PathVariable long storeId,
			@ApiParam(value = "request json payload to describe the product to be created", required = true) @Validated @RequestBody CreateProduct createProduct,
			HttpServletRequest request) {
		LOGGER.info("Creating a new product: {} for store: {}", createProduct.getProductName(), storeId);
		createProduct.setStoreId(storeId);
		HttpHeaders headers = new HttpHeaders();
		try {
			Store store = storeService.getStoreByID(storeId).get();
		} catch (Exception e) {
			LOGGER.warn("Store ID {} does not exist ", createProduct.getStoreId());
			return new ResponseEntity<>(new APIError("Validation failure", 412, "resource not exist", "store not exist"), headers, HttpStatus.PRECONDITION_FAILED);
		}

		Product product = productService.createProduct(createProduct);
		if (product == null)
			return new ResponseEntity<>(new APIError("Validation failure", 400, "product already exists", "product already exists"), headers, HttpStatus.BAD_REQUEST);

		Long productId = product.getProductId();
		headers.add(HttpHeaders.LOCATION, request.getRequestURL().append(productId).toString());
		return new ResponseEntity<>(new ResourceCreated<>(productId), headers, HttpStatus.OK);

	}
}