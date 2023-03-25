package selfdao.test.model;


/**
 * @author  mcarvalho
 */
public class Supplier {
	private int supplierID;
	private String companyName;
	private String contactName;
	private String contactTitle;
	private String address;
	private String city;
	
	public Supplier(
			int supplierID, 
			String companyName, 
			String contactName,
			String contactTitle, 
			String address, 
			String city
	) {
		this.supplierID = supplierID;
		this.companyName = companyName;
		this.contactName = contactName;
		this.contactTitle = contactTitle;
		this.address = address;
		this.city = city;
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

}
