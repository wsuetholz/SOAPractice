/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suetholz.net.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import suetholz.net.dba.DB_Accessor;
import suetholz.net.dba.DB_MySQL;
import suetholz.net.entities.Hotel;

/**
 *
 * @author wsuetholz
 */
public class HotelDAO implements IHotelDAO {
    private DB_Accessor db;
    
    public static final String driverClass = "com.mysql.jdbc.Driver";
    public static final String url = "jdbc:mysql://localhost:3306/hotel";
    public static final String userName = "root";
    public static final String password = "admin";
    
    public HotelDAO () {
	db = new DB_MySQL();
    }
    
    @Override
    public List<Hotel> getAllHotels() throws ClassNotFoundException, SQLException {
	List<Hotel> hotels = new ArrayList<>();
	
	db.openConnection(driverClass, url, userName, password);
	List<Map<String,Object>> rawData = db.getAllRecords ("hotel");
	
	for ( Map<String,Object> record : rawData ) {
	    Hotel hotel = new Hotel();
	    hotel.setHotelId(Integer.parseInt(record.get("hotel_id").toString()));
	    hotel.setHotelName(record.get("hotel_name").toString());
	    hotel.setStreetAddress(record.get("street_address").toString());
	    hotel.setCity(record.get("city").toString());
	    hotel.setState(record.get("state").toString());
	    hotel.setPostalCode(record.get("postal_code").toString());
	    hotel.setNotes(record.get("notes").toString());
	    hotels.add(hotel);
	}
	return hotels;
    }
    
    public static void main(String[] args) throws Exception {
	IHotelDAO hotelDao = new HotelDAO();
	
	List<Hotel> hotels = hotelDao.getAllHotels();
	System.out.println(hotels.toString());
    }
}
