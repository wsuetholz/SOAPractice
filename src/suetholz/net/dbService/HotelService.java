/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suetholz.net.dbService;

import java.util.List;
import suetholz.net.dao.HotelDAO;
import suetholz.net.dao.IHotelDAO;
import suetholz.net.entities.Hotel;

/**
 *
 * @author wsuetholz
 */
public class HotelService {
    IHotelDAO hotelDao;
    
    public HotelService () {
	hotelDao = new HotelDAO();
    }
    
    public List<Hotel> getAllHotels() throws Exception {
	return hotelDao.getAllHotels();
    }
    
    public static void main(String[] args) throws Exception {
	HotelService hotelService = new HotelService();
	
	System.out.println(hotelService.getAllHotels().toString());
    }
}
