package com.tenx.ms.retail.stock.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.SystemError;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.stock.exception.OutOfStockException;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.rest.dto.UpdateStock;
import com.tenx.ms.retail.stock.service.StockService;

@Api(value = "/stock", description = "Stock Management API")
@RestController("stockControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stock")
public class StockController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

	@Autowired
	private StockService stockService;

	@Autowired
	private ProductService productService;

	@ApiOperation(value = "Get Stock Information by Store ID and Product ID", notes = "The GET stock endpoint returns the quantity in stock of a product with the given storeId and productId. ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of the stock detail"),
			@ApiResponse(code = 404, message = "Product with given Store ID and Product ID does not exist"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}/{productId:\\d+}" }, method = RequestMethod.GET)
	public Stock getStockByStoreIdAndProductId(@ApiParam(name = "storeId", value = "The id of the store", required = true) @PathVariable long storeId,
			@ApiParam(name = "productId", value = "The id of the product", required = true) @PathVariable long productId) {

		LOGGER.debug("Fetching stock with storeId {} and productId {}", storeId, productId);

		return stockService.getStockByStoreIdAndProductId(storeId, productId).get();

	}

	@ApiOperation(value = "Update stock of the product", notes = "The PUT stock endpoint updates the stock of the product with increased/decreased quantity (count < 0 means decrease, count > 0 means increase)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful update of the stock"), @ApiResponse(code = 412, message = "Validation failure"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}/{productId:\\d+}" }, method = RequestMethod.PUT)
	public ResponseEntity<?> updateStock(@ApiParam(name = "storeId", value = "The id of the store the product belongs to", required = true) @PathVariable long storeId,
			@ApiParam(name = "productId", value = "The id of the product ", required = true) @PathVariable long productId,
			@ApiParam(value = "Update stock object", required = true) @Validated @RequestBody UpdateStock updateStock, HttpServletRequest request) {
		LOGGER.info("Updating the stock of product: {} ", productId);
		HttpHeaders headers = new HttpHeaders();
		try {
			productService.getProductByID(storeId, productId).get();
		} catch (Exception e) {
			LOGGER.error("Product {} in store {} does not exist", productId, storeId);
			return new ResponseEntity<>(new SystemError("Validation Error", HttpStatus.PRECONDITION_FAILED.value(), e), headers, HttpStatus.PRECONDITION_FAILED);
		}

		Stock stock = stockService.addStock(storeId, productId, updateStock.getQuantity());
		if (stock == null) {
			LOGGER.error("Product {} in store {} does not have enough stock", productId, storeId);
			return new ResponseEntity<>(new SystemError("Out Of Stock Error", HttpStatus.PRECONDITION_FAILED.value(), new OutOfStockException("Not enough stock for product:"
					+ productId)), headers, HttpStatus.PRECONDITION_FAILED);
		}
		headers.add(HttpHeaders.LOCATION, request.getRequestURL().toString());
		return new ResponseEntity<>(null, headers, HttpStatus.OK);

	}

}