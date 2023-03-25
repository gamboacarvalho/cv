package northwind.model;

import orm.annotations.JdbcCol;
import orm.annotations.JdbcMapper;
import orm.types.ValueHolder;
import orm.Entity;

/**
 * @author  mcarvalho
 */
public class OrderDetails implements Entity<OrderDetails.Key>{
	
	public static class Key{
		public final int OrderId; // The name must be equals to the corresponding Property bellow  
		public final int ProductId; // The name must be equals to the corresponding Property bellow
		public Key(int orderId, int productId) {
			this.OrderId = orderId;
			this.ProductId = productId;
		}
	}
	

	private Key id;
	private double unitPrice;
	private int quantity;
	private double discount;
	private ValueHolder<Order> order;
	private ValueHolder<Product> product;
	
	@JdbcMapper(table = "Order Details")
	public OrderDetails(
			@JdbcCol(value = "OrderId", isPk = true, isIdentity = false) int orderId,
			@JdbcCol(value = "ProductId", isPk = true, isIdentity = false) int productId,
			@JdbcCol("UnitPrice") double unitPrice, 
			@JdbcCol("Quantity") int quantity, 
			@JdbcCol("Discount") double discount,
			@JdbcCol(value="OrderId", referencedKeyClass=Integer.class) ValueHolder<Order> order,
			@JdbcCol(value="ProductId", referencedKeyClass=Integer.class) ValueHolder<Product> product) {
		this.id = new Key(orderId, productId);
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.discount = discount;
		this.order = order;
		this.product = product;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Order getOrder() {
		return order.get();
	}

	public void setOrder(final Order order) {
		this.order = new ValueHolder<Order>() {
			@Override
			public Order get() {
				return order;
			}
		};
	}

	public Product getProduct() {
		return product.get();
	}

	public void setProduct(final Product product) {
		this.product = new ValueHolder<Product>() {

			@Override
			public Product get() {
				return product;
			}
			
		};
	}
	
}
