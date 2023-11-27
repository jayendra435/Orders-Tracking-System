package ordersTrackingSystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.validation.Valid;
import ordersTrackingSystem.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	// 10
	List<Product> findByProductNameContaining(String string);

//	List<Product> findByProductProd_Name(@Valid String string);
}