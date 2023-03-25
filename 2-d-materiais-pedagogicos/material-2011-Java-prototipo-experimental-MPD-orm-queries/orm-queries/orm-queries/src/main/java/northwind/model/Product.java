package northwind.model;

import orm.annotations.JdbcCol;
import orm.annotations.JdbcMapper;
import orm.types.ValueHolder;

import java.sql.SQLException;
import orm.Entity;

/**
 * @author  mcarvalho
 */
public class Product implements Entity<Integer>{
	
	@JdbcMapper(table="Products")
	public Product(
			@JdbcCol(value = "ProductId", isPk=true, isIdentity = true) int productID, 
			@JdbcCol("ProductName") String productName, 
			@JdbcCol("UnitPrice") double unitPrice,
			@JdbcCol("UnitsInStock") int unitsInStock,
			@JdbcCol(value = "SupplierId", referencedKeyClass=Integer.class) ValueHolder<Supplier> supplier,
			@JdbcCol(value = "ProductId") Iterable<OrderDetails> orders,
			@JdbcCol(value = "CategoryID", referencedKeyClass=Integer.class) ValueHolder<Category> category) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.unitsInStock = unitsInStock;
		this.supplier = supplier;
		this.orders = orders;
		this.category = category;
	}
	public Product(String productName, double unitPrice, int unitsInStock) {
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.unitsInStock = unitsInStock;
	}
	int productID;
	String productName;
	double unitPrice;
	int unitsInStock;
	ValueHolder<Supplier> supplier;
	int quantityPerUnit;
	int unitsOnOrder;
	int reorderLevel;
	boolean discontinued;
	Iterable<OrderDetails> orders;
	ValueHolder<Category> category;
	
	
	public Category getCategory() {
		return category.get();
	}
	public void setCategory(final Category category) {
		this.category = new ValueHolder<Category>() {
			@Override
			public Category get() {
				return category;
			}
			
		};
	}
	public Iterable<OrderDetails> getOrders() {
		return orders;
	}
	public void setOrders(Iterable<OrderDetails> orders) {
		this.orders = orders;
	}
	public Integer getId() {
		return productID;
	}
	public void setId(Integer productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getUnitsInStock() {
		return unitsInStock;
	}

	public void setUnitsInStock(int unitsInStock) {
		this.unitsInStock = unitsInStock;
	}

	public Supplier getSupplier() throws SQLException{
		return supplier.get();
	}

	public void setSupplier(final Supplier s){
		supplier = new ValueHolder<Supplier>(){
			@Override
			public Supplier get(){
				return s;
			}
		};
	}
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName="
				+ productName + ", unitPrice=" + unitPrice + ", unitsInStock="
				+ unitsInStock + ", supplier=" + supplier.get().getId() + ", orders="
				+ orders + "]";
	}
}
