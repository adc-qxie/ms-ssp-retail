package com.tenx.ms.retail.order.rest;

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

import com.tenx.ms.commons.rest.APIError;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.order.rest.dto.CreateOrder;
import com.tenx.ms.retail.order.rest.dto.Order;
import com.tenx.ms.retail.order.rest.dto.OrderItem;
import com.tenx.ms.retail.order.service.OrderService;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
import com.tenx.ms.retail.store.service.StoreService;

@Api(value = "orders", description = "Stock Management API")
@RestController("orderControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/orders")
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private StockService stockService;

	@ApiOperation(value = "Get order Information by store ID and order ID", notes = "The GET order endpoint returns information about the order placed on a store by given storeId and orderId.", response = Order.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of the order detail"),
			@ApiResponse(code = 404, message = "OrderEntity with given id does not exist"), @ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}/{orderId:\\d+}" }, method = RequestMethod.GET)
	public Order getOrdertById(@ApiParam(name = "storeId", value = "The id of the store the order placed on", required = true) @PathVariable long storeId,
			@ApiParam(name = "orderId", value = "The id of the order to get its details", required = true) @PathVariable long orderId) {

		LOGGER.debug("Fetching order with storeId {}, order Id {}", storeId, orderId);

		return orderService.getOrderByStoreIdAndOrderId(storeId, orderId);

	}

	@ApiOperation(value = "Create order in a store", notes = "The POST order endpoint creates the order placed on a store and returns the orderId.", response = ResourceCreated.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful create an order"), @ApiResponse(code = 412, message = "Validation failure"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@RequestMapping(value = { "/{storeId:\\d+}" }, method = RequestMethod.POST)
	public ResponseEntity<?> createOrder(@ApiParam(name = "storeId", value = "The id of the store", required = true) @PathVariable long storeId,
			@ApiParam(value = "request json payload to describe the order to be created", required = true) @Validated @RequestBody CreateOrder createOrder,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		try {
			storeService.getStoreByID(storeId).get();
		} catch (Exception e) {
			LOGGER.error("Store {} does not exist", storeId);
			return new ResponseEntity<>(new APIError("Validation failure", 412, "resource not exist", "store does not exist"), headers, HttpStatus.PRECONDITION_FAILED);
		}

		for (OrderItem item : createOrder.getItems()) {
			Stock stock = stockService.getStockByStoreIdAndProductId(storeId, item.getProductId()).get();
			if (stock.getQuantity() < item.getQuantity()) {
				LOGGER.error("not enough in stock for product {} in store {}", stock.getProductId(), storeId);
				return new ResponseEntity<>(new APIError("Validation failure", 412, "not enough in stock", "not enough in stock"), headers, HttpStatus.PRECONDITION_FAILED);
			}
		}

		Order order = orderService.createOrder(createOrder, storeId);

		Long orderId = order.getOrderId();
		headers.add(HttpHeaders.LOCATION, request.getRequestURL().append(orderId).toString());
		return new ResponseEntity<>(new ResourceCreated<>(orderId), headers, HttpStatus.OK);

	}
}