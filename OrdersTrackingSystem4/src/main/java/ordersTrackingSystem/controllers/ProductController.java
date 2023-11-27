package ordersTrackingSystem.controllers;

import java.util.List;
import java.util.Optional;

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
import jakarta.validation.Valid;
import orderTrackingSystem.DTOs.ProductsOrderedByCustomerDTO;
import ordersTrackingSystem.entities.Product;
import ordersTrackingSystem.repositories.CustomerRepo;
import ordersTrackingSystem.repositories.OrderItemRepo;
import ordersTrackingSystem.repositories.OrderRepo;
import ordersTrackingSystem.repositories.ProductRepo;

@RestController
@CrossOrigin
public class ProductController {
	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	OrderItemRepo orderItemRepo;

	// 2. 1
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(summary = "add product", description = "adds a product by taking a request body")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "product added"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
	@PostMapping("/products/add")
	public Product addProduct(@Valid @RequestBody Product product) {
		try {
			return productRepo.save(product);
		} catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// 2. 2
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(summary = "update product", description = "updates product for the given product id")
		@Parameter(description = "enter product id")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "product details updated"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
	@PutMapping("/products/update/{productId}")
	public Product updateProduct(@Valid @PathVariable("productId") Integer productId,
			@RequestParam("name") String name) {
		try {
			var optionalProduct = productRepo.findById(productId);
			if (optionalProduct.isPresent()) {
				var product = optionalProduct.get();
				product.setProductName(name);
				productRepo.save(product);
				return product;
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product id not found");
			}
		} catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// 2. 3
		@PreAuthorize("hasRole('ADMIN')")
		@Operation(summary = "delete a product")
		@Parameter(description = "enter product id")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "product deleted"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
	@DeleteMapping("/products/delete/{productId}")
	public void deleteProduct(@PathVariable("productId") Integer productId) {
		Optional<Product> optionalProduct = productRepo.findById(productId);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			productRepo.delete(product);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product id not found");
		}
	}

	// 6
		@CrossOrigin
		@Operation(summary = "list products")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "products retrieved"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
	@GetMapping("/products")
	public List<Product> getProducts() {
		return productRepo.findAll();
	}

	/* 6. pagination */
		@CrossOrigin
		@Operation(summary = "list products by page number")
		@Parameter(description = "enter page number")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "products retrieved"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
	@GetMapping("/products/{pageno}")
	public Page<Product> getProductOfPage(@PathVariable("pageno") Integer pageno) {
		Page<Product> page = productRepo.findAll(PageRequest.of(pageno, 3));
		return page;
	}

	// 10
		@CrossOrigin
		@Operation(summary = "gets products that contains a string", description = "gets products containing the given string")
		@Parameter(description = "enter string")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "retrieved products containing a string"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
	@GetMapping("/products-with-string/{string}")
	public List<Product> getProductsWithGivenString(@Valid @PathVariable("string") String string) {
		return productRepo.findByProductNameContaining(string);
	}

	// 13
		@Operation(summary = "products ordered by customer", 
				description = "retrieves all the products ordered by a customer")
		@Parameter(description = "enter customer id")
		@ApiResponses(value = { 
				@ApiResponse(responseCode = "200", description = "retrived products ordered by a customer"),
				@ApiResponse(responseCode = "400", description = "bad request"),
				@ApiResponse(responseCode = "500", description = "internal server error") })
	@GetMapping("/products/products-ordered-by-customer/{customerId}")
	public List<ProductsOrderedByCustomerDTO> getProductsOrderedByCustomer(
			@Valid @PathVariable("customerId") Integer customerId) {
		return orderItemRepo.findAllProductsByCustomerId(customerId);
	}

}
