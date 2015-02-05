/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suetholz.net.dba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wsuetholz
 */
public class DB_MySQL implements DB_Accessor {
    private Connection conn;
    
    @Override
    public void openConnection( String driverClass, String url, 
				String userName, String password) 
	    throws ClassNotFoundException, SQLException {
	Class.forName(driverClass);
	conn = DriverManager.getConnection(url, userName, password);
    }
    
    @Override
    public void closeConnection() throws SQLException {
	conn.close();
    }
    
    @Override
    public Map getRecordById(String tableName, String keyName, Object keyValue) throws SQLException {
	String sql = "select * from " + tableName + " where " + keyName + " = ?";
	int index = 1;
	final Map<String, Object> record = new HashMap<>();
	ResultSetMetaData metaData = null;
	
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setObject(index++, keyValue);
	ResultSet rs = pstmt.executeQuery();
	
        metaData = rs.getMetaData();
	metaData.getColumnCount();
	final int fields=metaData.getColumnCount();

	// Retrieve the raw data from the ResultSet and copy the values into a Map
	// with the keys being the column names of the table.
	if(rs.next() ) {
		for( int i=1; i <= fields; i++ ) {
			record.put( metaData.getColumnName(i), rs.getObject(i) );
		}
	}


	return record;
    }
    
    @Override
    public List<Map<String,Object>> getAllRecords (String tableName) throws SQLException {
	List<Map<String,Object>> records = new ArrayList<>();
	String sql = "select * from " + tableName;
	Statement stmt = null;
	ResultSet rs = null;
	ResultSetMetaData metaData = null;

	stmt = conn.createStatement();
	rs = stmt.executeQuery(sql);
	metaData = rs.getMetaData();
	final int fields = metaData.getColumnCount();

	while( rs.next() ) {
		Map<String,Object> record = new HashMap();
		for( int i=1; i <= fields; i++ ) {
			try {
				record.put( metaData.getColumnName(i), rs.getObject(i) );
			} catch(NullPointerException npe) { 
				// no need to do anything... if it fails, just ignore it and continue
			}
		} // end for
		records.add(record);
	} // end while

	return records;
    }
    
    @Override
    public long updateRecords(String tableName, Map<String, Object> keyValueSet, Map<String, Object> updateValueSet) throws SQLException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long insertRecord(String tableName, Map<String, Object> insertValueSet) throws SQLException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long deleteRecords(String tableName, Map<String, Object> keyValueSet) throws SQLException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) throws Exception {
	DB_Accessor db = new DB_MySQL();
	db.openConnection("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/hotel", "root", "admin");
	Map rec = db.getRecordById("hotel", "hotel_id", 1);
    }

}
