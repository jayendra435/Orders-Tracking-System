package ordersTrackingSystem.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
import orderTrackingSystem.DTOs.OrderItemDetailsDTO;
import orderTrackingSystem.DTOs.OrderItemsDTO;
import orderTrackingSystem.DTOs.ProductsOrderedByCustomerDTO;
import ordersTrackingSystem.entities.Customer;
import ordersTrackingSystem.entities.Order;
import ordersTrackingSystem.entities.OrderItem;
import ordersTrackingSystem.entities.OrderItemCompositeKey;
import ordersTrackingSystem.entities.Product;
import ordersTrackingSystem.repositories.CustomerRepo;
import ordersTrackingSystem.repositories.OrderItemRepo;
import ordersTrackingSystem.repositories.OrderRepo;
import ordersTrackingSystem.repositories.ProductRepo;

@RestController
@CrossOrigin
public class CustomerController {
	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	OrderItemRepo orderItemRepo;

	// 1. 1
	@PreAuthorize("hasRole('ADMIN')") // method level security configuration
	@Operation(summary = "add customer", description = "adds a new customer by taking a request body")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "customer added"),
			@ApiResponse(responseCode = "400", description = "bad request"),
			@ApiResponse(responseCode = "500", description = "internal server error") })
	@PostMapping("/customers/add")
	public Customer addCustomer(@Valid @RequestBody Customer customer) {
		try {
			return customerRepo.save(customer);
		} catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// 1. 2
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "update customer", description = "updates name of the custpmer for the given customer id")
	@Parameter(description = "enter customer id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "customer details updated"),
			@ApiResponse(responseCode = "400", description = "bad request"),
			@ApiResponse(responseCode = "500", description = "internal server error") })
	@PutMapping("/customers/update/{customerId}")
	public Customer updateCustomer(@Valid @PathVariable("customerId") Integer customerId,
			@RequestParam("name") String name) {
		try {
			var optionalCustomer = customerRepo.findById(customerId);
			if (optionalCustomer.isPresent()) {
				var customer = optionalCustomer.get();
				customer.setName(name);
				return customerRepo.save(customer);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "customer id not found");
			}
		} catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

//		 1. 3
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "delete a customer", description = "deletes a customer with the given customer id")
	@Parameter(description = "enter")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "customer deleted"),
			@ApiResponse(responseCode = "400", description = "bad request"),
			@ApiResponse(responseCode = "500", description = "internal server error") })
	@DeleteMapping("/customers/delete/{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer customerId) {
		Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			customerRepo.delete(customer);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "customer id not found");
		}
	}

	// 5
	@CrossOrigin
	@Operation(summary = "list customers")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "customers retrieved"),
			@ApiResponse(responseCode = "400", description = "bad request"),
			@ApiResponse(responseCode = "500", description = "internal server error") 
			})
	@GetMapping("/customers")
	public List<Customer> getCustomers() {
		return customerRepo.findAll();
	}

	/* 5. pagination */
	@CrossOrigin
	@Operation(summary = "list customers by page number", description = "list customers by page number")
	@Parameter(description = "enter page number")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "retrieved customer details"),
			@ApiResponse(responseCode = "400", description = "bad request"),
			@ApiResponse(responseCode = "500", description = "internal server error") })
	@GetMapping("/customers/{pageno}")
	public Page<Customer> getCustomerFromPage(@PathVariable("pageno") Integer pageno) {
		return customerRepo.findAll(PageRequest.of(pageno, 3));
	}
}
