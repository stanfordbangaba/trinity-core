package zw.co.bangsoft.trinity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import zw.co.bangsoft.trinity.annotation.Email;

@Embeddable
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(length = 80)
	private String street;
	
	@Column(length = 50)
	private String suburb;
	
	@Column(length = 50)
	private String city;
	
	@Column(length = 80)
	private String country;
	
	@Column(length = 20)
	private String zipCode;

	@Column(length = 150)
	private String postalAddress;
	
	@Column(length = 50)
	private String contactPerson;
	
	@Column(length = 20)
	private String contactPhone;
	
	@Column(length = 100)
	@Email
	private String email;
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (street != null)
			result += "street: " + street;
		return result;
	}
}