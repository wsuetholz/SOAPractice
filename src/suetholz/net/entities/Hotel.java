/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suetholz.net.entities;

/**
 *
 * @author wsuetholz
 */
public class Hotel {
    private int hotelId;
    private String hotelName;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String notes;
    
    public Hotel() {
	
    }

    public int getHotelId() {
	return hotelId;
    }

    public void setHotelId(int hotelId) {
	this.hotelId = hotelId;
    }

    public String getHotelName() {
	return hotelName;
    }

    public void setHotelName(String hotelName) {
	this.hotelName = hotelName;
    }

    public String getStreetAddress() {
	return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
	this.streetAddress = streetAddress;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public String getPostalCode() {
	return postalCode;
    }

    public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }
    
    
}
