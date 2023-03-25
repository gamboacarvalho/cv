package orm.northwind;

import orm.uow.Entity;
import orm.uow.Entity.EntityState;

public class Product extends Entity<Integer>{
	
	private String _productName, _quantityPerUnit, _categoryName;
  private double _unitPrice;
  private short _unitsInStock, _unitsOnOrder, _reorderLevel;
  private boolean _discontinued;
  
  
	public Product(Integer id, String _productName, String _quantityPerUnit,
      String _categoryName, double _unitPrice, short _unitsInStock,
      short _unitsOnOrder, short _reorderLevel, boolean _discontinued) {
	  super(id);
	  this._productName = _productName;
	  this._quantityPerUnit = _quantityPerUnit;
	  this._categoryName = _categoryName;
	  this._unitPrice = _unitPrice;
	  this._unitsInStock = _unitsInStock;
	  this._unitsOnOrder = _unitsOnOrder;
	  this._reorderLevel = _reorderLevel;
	  this._discontinued = _discontinued;
  }
	public Product(String _productName, String _quantityPerUnit,
      String _categoryName, double _unitPrice, short _unitsInStock,
      short _unitsOnOrder, short _reorderLevel, boolean _discontinued) {
	  super();
	  this._productName = _productName;
	  this._quantityPerUnit = _quantityPerUnit;
	  this._categoryName = _categoryName;
	  this._unitPrice = _unitPrice;
	  this._unitsInStock = _unitsInStock;
	  this._unitsOnOrder = _unitsOnOrder;
	  this._reorderLevel = _reorderLevel;
	  this._discontinued = _discontinued;
	  onUpdate(this, EntityState.New);
  }
	
	public String get_productName() {
  	return _productName;
  }
	public void set_productName(String _productName) {
		onUpdate(this, EntityState.Updated);
  	this._productName = _productName;
  }
	public String get_quantityPerUnit() {
  	return _quantityPerUnit;
  }
	public void set_quantityPerUnit(String _quantityPerUnit) {
		onUpdate(this, EntityState.Updated);
  	this._quantityPerUnit = _quantityPerUnit;
  }
	public String get_categoryName() {
  	return _categoryName;
  }
	public void set_categoryName(String _categoryName) {
		onUpdate(this, EntityState.Updated);
  	this._categoryName = _categoryName;
  }
	public double get_unitPrice() {
  	return _unitPrice;
  }
	public void set_unitPrice(double _unitPrice) {
		onUpdate(this, EntityState.Updated);
  	this._unitPrice = _unitPrice;
  }
	public short get_unitsInStock() {
  	return _unitsInStock;
  }
	public void set_unitsInStock(short _unitsInStock) {
		onUpdate(this, EntityState.Updated);
  	this._unitsInStock = _unitsInStock;
  }
	public short get_unitsOnOrder() {
  	return _unitsOnOrder;
  }
	public void set_unitsOnOrder(short _unitsOnOrder) {
		onUpdate(this, EntityState.Updated);
  	this._unitsOnOrder = _unitsOnOrder;
  }
	public short get_reorderLevel() {
  	return _reorderLevel;
  }
	public void set_reorderLevel(short _reorderLevel) {
		onUpdate(this, EntityState.Updated);
  	this._reorderLevel = _reorderLevel;
  }
	public boolean is_discontinued() {
  	return _discontinued;
  }
	public void set_discontinued(boolean _discontinued) {
		onUpdate(this, EntityState.Updated);
  	this._discontinued = _discontinued;
  }
}
