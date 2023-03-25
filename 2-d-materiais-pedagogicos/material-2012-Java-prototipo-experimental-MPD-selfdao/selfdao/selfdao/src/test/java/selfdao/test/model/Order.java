package selfdao.test.model;

import java.util.Date;


/**
 * @author  mcarvalho
 */
public class Order {
	
	private  int id;   
	private Date orderDate;
	private Date shippedDate;
	private double freight;
	private String shipName;
	
	private Order(Date orderDate, Date shippedDate, double freight, String shipName) {
		this.orderDate = orderDate;
		this.shippedDate = shippedDate;
		this.freight = freight;
		this.shipName = shipName;
	}
	
	private Order(int id, Date orderDate, Date shippedDate, double freight, String shipName) {
		this.id = id;
		this.orderDate = orderDate;
		this.shippedDate = shippedDate;
		this.freight = freight;
		this.shipName = shipName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getShippedDate() {
		return shippedDate;
	}
	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public String getShipName() {
		return shipName;
	}
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	
	
	
}
