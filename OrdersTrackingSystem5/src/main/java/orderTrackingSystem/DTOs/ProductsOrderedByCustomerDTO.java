package orderTrackingSystem.DTOs;

// 13
public interface ProductsOrderedByCustomerDTO {
	Integer getCustomerId();
	String getCustomerName();
	Integer getOrderId();
	Integer getProductId();
	String getProductName();
	String getTotalPrice();
}