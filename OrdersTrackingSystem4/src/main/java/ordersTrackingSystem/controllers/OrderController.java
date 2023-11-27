package ordersTrackingSystem.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import orderTrackingSystem.DTOs.OrderItemDetailsDTO;
import ordersTrackingSystem.entities.Order;
import ordersTrackingSystem.repositories.CustomerRepo;
import ordersTrackingSystem.repositories.OrderItemRepo;
import ordersTrackingSystem.repositories.OrderRepo;
import ordersTrackingSystem.repositories.ProductRepo;


@RestController
@CrossOrigin
public class OrderController {
	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	OrderItemRepo orderItemRepo;
	
	

		

		// 3. 2
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(summary = "delete order", description = "deletes order and updates order item table")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "order deleted"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@DeleteMapping("/orders/delete/{orderId}")
		public void deleteOrder(@PathVariable("orderId") Integer orderId) {
			Optional<Order> optionalOrder = orderRepo.findById(orderId);
			if (optionalOrder.isPresent()) {
				Order order = optionalOrder.get();
				orderRepo.delete(order);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order id not found");
			}
		}
	    
		@GetMapping("/orders")
		public List<Order> getOrders() {
			return orderRepo.findAll();
		}

		// 4
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(summary = "update status of an order", description = "updates status of an order")
		@Parameter(description = "enter order id and give status parameter")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "order status updated"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@PutMapping("/orders/update-status/{orderId}")
		public Order updateOrderStatus(@PathVariable("orderId") Integer orderId, @RequestParam("status") Character status) {
			try {
				var optionalOrder = orderRepo.findById(orderId);
				if (optionalOrder.isPresent()) {
					var order = optionalOrder.get();
					if (order.getStatus() == 'd') {
						throw new 
						ResponseStatusException(HttpStatus.ALREADY_REPORTED, "order already delivered");
					}
					if (order.getStatus() == 'c') {
						throw new 
						ResponseStatusException(HttpStatus.LOCKED, "cannot update cancelled orders");
					}
					if (status == 'c') {
						order.setStatus(status);
						order.setExpectedDeliveryDate(null);
						orderRepo.save(order);
					} else {
						order.setStatus(status);
						orderRepo.save(order);
					}
					return order;
				} else {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "order id not found");
				}
			} catch (DataAccessException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}

		


		// 7
		@Operation(summary = "get order", description = "gets order(s) by customer")
		@Parameter(description = "enter customer id")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "retrieved orders by customer"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@GetMapping("/orders-by-customer/{customerId}")
		public List<Order> getOrdersByCustomerId(@PathVariable("customerId") Integer customerId) {
			return orderRepo.findByCustomerId(customerId);
		}

		// 8
		@CrossOrigin
		@Operation(summary = "orders by status", description = "gets order(s) by status")
		@Parameter(description = "enter status")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "retrieved orders by status"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@GetMapping("/orders-by-status/{status}")
		public List<Order> getOrdersByStatus(@PathVariable("status") Character status) {
			return orderRepo.findAllByStatus(status);
		}

		

		// 12
		@Operation(summary = "all order details", 
				description = "retrieves all the order details for the given order id")
		@Parameter(description = "enter order id")
		@ApiResponses(value = {
				@ApiResponse(responseCode = "200", description = "retrieved all order details"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
		@GetMapping("orders/all-order-details/{orderId}")
		public List<OrderItemDetailsDTO> getAllOrderDetails(@Valid @PathVariable("orderId") Integer orderId) {
			return orderItemRepo.findByOrderId(orderId);
		}


		// 14
		@Operation(summary = "orders between a specified period", 
				description = "retrieves all the orders between a specific period")
		@ApiResponses(value = {
				@ApiResponse(responseCode = "200", 
						description = "retrieved orders between a specified period"),
				@ApiResponse(responseCode = "404", description = "order(s) not found"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
			@GetMapping("/orders/between-dates")
			public List<Order> getOrdersBetweenDates(@Valid @RequestParam("startDate") String startDate,
					@RequestParam("endDate") String endDate) {
				LocalDate parseStartDate = LocalDate.parse(startDate);
				LocalDate parseEndDate = LocalDate.parse(endDate);
		
				return orderRepo.getOrdersBetween(parseStartDate, parseEndDate);
			}

}
