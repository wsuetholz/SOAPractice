/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suetholz.net.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public long updateHotel(Hotel hotel) throws ClassNotFoundException, SQLException {
	Map<String, Object> updKeyRec = new HashMap<>();
	updKeyRec.put("hotel_id", hotel.getHotelId());
	Map<String, Object> updValRec = new HashMap<>();
	updValRec.put("hotel_name", hotel.getHotelName());
	updValRec.put("street_address", hotel.getStreetAddress());
	updValRec.put("city", hotel.getCity());
	updValRec.put("state", hotel.getState());
	updValRec.put("postal_code", hotel.getPostalCode());
	updValRec.put("notes", hotel.getNotes());

	db.openConnection(driverClass, url, userName, password);
	long retVal = db.updateRecords("hotel", updKeyRec, updValRec);
	
	return retVal;
    }

    @Override
    public long deleteHotel(Hotel hotel) throws ClassNotFoundException, SQLException {
	Map<String, Object> delKeyRec = new HashMap<>();
	delKeyRec.put("hotel_id", hotel.getHotelId());

	db.openConnection(driverClass, url, userName, password);
	long retVal = db.deleteRecords("hotel", delKeyRec);
	
	return retVal;
    }

    @Override
    public long insertHotel(Hotel hotel) throws ClassNotFoundException, SQLException {
	Map<String, Object> insRec = new HashMap<>();
	insRec.put("hotel_id", 0);
	insRec.put("hotel_name", hotel.getHotelName());
	insRec.put("street_address", hotel.getStreetAddress());
	insRec.put("city", hotel.getCity());
	insRec.put("state", hotel.getState());
	insRec.put("postal_code", hotel.getPostalCode());
	insRec.put("notes", hotel.getNotes());

	db.openConnection(driverClass, url, userName, password);
	long retVal = db.insertRecord("hotel", insRec);
	
	return retVal;
    }
    
    public static void main(String[] args) throws Exception {
	IHotelDAO hotelDao = new HotelDAO();
	
	List<Hotel> hotels = hotelDao.getAllHotels();
	for ( Hotel hotel: hotels) {
	    System.out.println(hotel);
	}
    }
}
