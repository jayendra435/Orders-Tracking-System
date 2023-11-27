package ordersTrackingSystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import orderTrackingSystem.DTOs.AllProductDetailsDTO;
import orderTrackingSystem.DTOs.OrderDTO;
import orderTrackingSystem.DTOs.OrderItemDTO;
import orderTrackingSystem.DTOs.OrderItemsDTO;
import ordersTrackingSystem.entities.Order;
import ordersTrackingSystem.entities.OrderItem;
import ordersTrackingSystem.entities.OrderItemCompositeKey;
import ordersTrackingSystem.repositories.CustomerRepo;
import ordersTrackingSystem.repositories.OrderItemRepo;
import ordersTrackingSystem.repositories.OrderRepo;
import ordersTrackingSystem.repositories.ProductRepo;

@CrossOrigin
@RestController
public class OrderItemsController {
	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	OrderItemRepo orderItemRepo;
	
	
		// 3. 1
		@Transactional
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(summary = "add order", description = "adds an order and updates order item table by taking request bodies")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "order added"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@PostMapping("/orders/add")
		public void addOrder(@Valid @RequestBody OrderDTO orderDTO) {
			try {
				Order order = new Order();
				order.setOrderDate(orderDTO.getOrderDate());
				order.setCustomerId(orderDTO.getCustomerId());
				order.setStatus(orderDTO.getStatus());
				order.setExpectedDeliveryDate(orderDTO.getExpectedDeliveryDate());

				Order savedOrder = orderRepo.save(order);

				for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
					OrderItem orderItem = new OrderItem();
					OrderItemCompositeKey orderItemCK = new OrderItemCompositeKey();

					orderItemCK.setOrderId(savedOrder.getOrderId());
					orderItemCK.setProductId(orderItemDTO.getProductId());

					orderItem.setOrderItemCK(orderItemCK);

					orderItem.setQuantity(orderItemDTO.getQuantity());
					orderItem.setTotalPrice(orderItemDTO.getTotalPrice());

					/* @ManyToOne */
					orderItem.setOrder(savedOrder);

					/* @OneToMany */
					savedOrder.getOrderItems().add(orderItem);
				}
				orderRepo.save(savedOrder);
			} catch (DataAccessException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}

		

		

		// 9
		@CrossOrigin
		@Operation(summary = "gets orders in the specified order")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "retrieved orders in specified order"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@GetMapping("/orders/inorder")
		public List<OrderItemsDTO> getOrdersInOrder() {
			return orderRepo.getOrderItems();
		}

		
		

		// 11
		@CrossOrigin
		@Operation(summary = "gets product details", 
				description = "retrieves product(s) with a given product id")
		@Parameter(description = "enter product id")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "retrieved all product details"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@GetMapping("/products/product-details/{productId}")
		public Object getProductDetailsRest
							(@PathVariable("productId") Integer productId) {
			try {
				var list = getProductDetails(productId);
				return list;
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		public List<AllProductDetailsDTO> getProductDetails (Integer productId) {
			var optionalProduct = productRepo.findById(productId);
			if (optionalProduct.isPresent()) {
				return orderItemRepo.getAllProductSaleDetails(productId);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product id not found");
			}
		}
		
//		public List<AllProductDetailsDTO> getProductDetails
//							(@Valid @PathVariable("productId") Integer productId) {
//			var optionalProduct = productRepo.findById(productId);
//			if (optionalProduct.isPresent()) {
//				return orderItemRepo.getAllProductSaleDetails(productId);
//			} else {
//				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product id not found");
//			}
//		}

}