package orm.northwind;

import orm.uow.Entity;
import orm.uow.Entity.EntityState;

public class Customer extends Entity<String>{
	private String _companyName, _contactName, _contactTitle, _address, _city, _region, _postalCode, _country, _phone, _fax;

	public Customer(String customerId, String companyName, String contactName, String contactTitle,
      String address, String city, String region, String postalCode,
      String country, String phone, String fax) {
	  super(customerId);
	  _companyName = companyName;
	  _contactName = contactName;
	  _contactTitle = contactTitle;
	  _address = address;
	  _city = city;
	  _region = region;
	  _postalCode = postalCode;
	  _country = country;
	  _phone = phone;
	  _fax = fax;
  }

	public Customer(String companyName, String contactName, String contactTitle,
      String address, String city, String region, String postalCode,
      String country, String phone, String fax) {
	  super();
	  _companyName = companyName;
	  _contactName = contactName;
	  _contactTitle = contactTitle;
	  _address = address;
	  _city = city;
	  _region = region;
	  _postalCode = postalCode;
	  _country = country;
	  _phone = phone;
	  _fax = fax;
	  onUpdate(this, EntityState.New);
  }

	public String getCompanyName() {
  	return _companyName;
  }

	public void setCompanyName(String companyName) {
  	_companyName = companyName;
  }

	public String getContactName() {
  	return _contactName;
  }

	public void setContactName(String contactName) {
  	_contactName = contactName;
  }

	public String getContactTitle() {
  	return _contactTitle;
  }

	public void setContactTitle(String contactTitle) {
  	_contactTitle = contactTitle;
  }

	public String getAddress() {
  	return _address;
  }

	public void setAddress(String address) {
  	_address = address;
  }

	public String getCity() {
  	return _city;
  }

	public void setCity(String city) {
  	_city = city;
  }

	public String getRegion() {
  	return _region;
  }

	public void setRegion(String region) {
  	_region = region;
  }

	public String getPostalCode() {
  	return _postalCode;
  }

	public void setPostalCode(String postalCode) {
  	_postalCode = postalCode;
  }

	public String getCountry() {
  	return _country;
  }

	public void setCountry(String country) {
  	_country = country;
  }

	public String getPhone() {
  	return _phone;
  }

	public void setPhone(String phone) {
  	_phone = phone;
  }

	public String getFax() {
  	return _fax;
  }

	public void setFax(String fax) {
  	_fax = fax;
  }
}
