package northwind.model;

import orm.Entity;
import orm.annotations.JdbcCol;
import orm.annotations.JdbcMapper;

public class Category implements Entity<Integer>{

	private int CategoryID;
	private String CategoryName;
	private String Description;
	private Iterable<Product> Products;
	
	@JdbcMapper(table="Categories")
	public Category(
			@JdbcCol(value="CategoryID", isIdentity = true, isPk = true) int categoryID, 
			@JdbcCol(value="CategoryName") String categoryName, 
			@JdbcCol(value="Description") String description,
			@JdbcCol(value="CategoryID") Iterable<Product> products
			) {
		CategoryID = categoryID;
		CategoryName = categoryName;
		Description = description;
		Products = products;
	}
	
	public int getCategoryID() {
		return CategoryID;
	}

	public void setCategoryID(int categoryID) {
		CategoryID = categoryID;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Iterable<Product> getProducts(){
		return Products;
	}

	@Override
	public Integer getId() {
		return CategoryID;
	}

	@Override
	public void setId(Integer id) {
		CategoryID = id;
	}

}
