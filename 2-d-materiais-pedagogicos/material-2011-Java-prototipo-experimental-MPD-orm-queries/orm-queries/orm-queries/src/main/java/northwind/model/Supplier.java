package northwind.model;

import orm.annotations.JdbcCol;
import orm.annotations.JdbcMapper;
import orm.Entity;


/**
 * @author  mcarvalho
 */
public class Supplier implements Entity<Integer>{
	private int supplierID;
	private String companyName;
	private String contactName;
	private String contactTitle;
	private String address;
	private String city;
	private String region;
	private String postalCode;
	private String country;
	private String phone;
	private String fax;
	private String homePage;
	
	@JdbcMapper(table="Suppliers")
	public Supplier(
			@JdbcCol(value="SupplierID", isPk = true, isIdentity = true) int supplierID, 
			@JdbcCol("CompanyName") String companyName, 
			@JdbcCol("ContactName") String contactName,
			@JdbcCol("ContactTitle") String contactTitle, 
			@JdbcCol("Address") String address, 
			@JdbcCol("City") String city, 
			@JdbcCol("Region") String region,
			@JdbcCol("PostalCode") String postalCode, 
			@JdbcCol("Country") String country, 
			@JdbcCol("Phone") String phone, 
			@JdbcCol("Fax") String fax,
			@JdbcCol("HomePage") String homePage
	) {
		this.supplierID = supplierID;
		this.companyName = companyName;
		this.contactName = contactName;
		this.contactTitle = contactTitle;
		this.address = address;
		this.city = city;
		this.region = region;
		this.postalCode = postalCode;
		this.country = country;
		this.phone = phone;
		this.fax = fax;
		this.homePage = homePage;
	}
	public Integer getId() {
		return supplierID;
	}
	public void setId(Integer supplierID) {
		this.supplierID = supplierID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
}
