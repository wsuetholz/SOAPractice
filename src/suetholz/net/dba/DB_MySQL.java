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
    public void openConnection(String driverClass, String url,
			       String userName, String password)
	    throws ClassNotFoundException, SQLException {
	Class.forName(driverClass);
	conn = DriverManager.getConnection(url, userName, password);
    }

    @Override
    public void closeConnection() throws SQLException {
	conn.close();
    }

    public Map getColumnTypes(String tableName) throws SQLException {
	final Map<String, Integer> columns = new HashMap<>();
	String sql = "select * from " + tableName + " where 1 = 0";

	PreparedStatement pstmt = null;
	try {
	    pstmt = conn.prepareStatement(sql);
	    ResultSet rs = pstmt.executeQuery();

	    ResultSetMetaData metaData = rs.getMetaData();
	    metaData.getColumnCount();
	    final int fields = metaData.getColumnCount();

	    for (int i = 1; i <= fields; i++) {
		String colName = metaData.getColumnName(i);
		Integer colType = metaData.getColumnType(i);
		columns.put(colName, colType);
	    }
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (pstmt != null) {
		pstmt.close();
	    }
	}

	return columns;
    }

    @Override
    public Map getRecordById(String tableName, String keyName, Object keyValue) throws SQLException {
	String sql = "select * from " + tableName + " where " + keyName + " = ?";
	int index = 1;
	final Map<String, Object> record = new HashMap<>();
	ResultSetMetaData metaData = null;

	PreparedStatement pstmt = null;
	try {
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setObject(index++, keyValue);
	    ResultSet rs = pstmt.executeQuery();

	    metaData = rs.getMetaData();
	    metaData.getColumnCount();
	    final int fields = metaData.getColumnCount();

	    // Retrieve the raw data from the ResultSet and copy the values into a Map
	    // with the keys being the column names of the table.
	    if (rs.next()) {
		for (int i = 1; i <= fields; i++) {
		    record.put(metaData.getColumnName(i), rs.getObject(i));
		}
	    }
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (pstmt != null) {
		pstmt.close();
	    }
	}

	return record;
    }

    @Override
    public List<Map<String, Object>> getAllRecords(String tableName) throws SQLException {
	List<Map<String, Object>> records = new ArrayList<>();
	String sql = "select * from " + tableName;
	Statement stmt = null;
	ResultSet rs = null;
	ResultSetMetaData metaData = null;

	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(sql);
	    metaData = rs.getMetaData();
	    final int fields = metaData.getColumnCount();

	    while (rs.next()) {
		Map<String, Object> record = new HashMap();
		for (int i = 1; i <= fields; i++) {
		    try {
			record.put(metaData.getColumnName(i), rs.getObject(i));
		    } catch (NullPointerException npe) {
			// no need to do anything... if it fails, just ignore it and continue
		    }
		} // end for
		records.add(record);
	    } // end while
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (stmt != null) {
		stmt.close();
	    }
	}

	return records;
    }

    @Override
    public long updateRecords(String tableName, Map<String, Object> keyValueSet, Map<String, Object> updateValueSet) throws SQLException {
	long ret = 0;

	if (updateValueSet.isEmpty()) {
	    throw new IllegalArgumentException("Need something to update!");
	}

	//Map<String,Integer>columns = this.getColumnTypes(tableName);
	StringBuilder sqlBuilder = new StringBuilder("update " + tableName + " set ");
	boolean firstOne = true;

	for (String colName : updateValueSet.keySet()) {
	    if (!firstOne) {
		sqlBuilder.append(", ");
	    } else {
		firstOne = false;
	    }
	    sqlBuilder.append(colName);
	    sqlBuilder.append(" = ?");
	}

	if (!keyValueSet.isEmpty()) {
	    sqlBuilder.append(" where ");
	    firstOne = true;
	    for (String colName : keyValueSet.keySet()) {
		if (!firstOne) {
		    sqlBuilder.append(" and ");
		} else {
		    firstOne = false;
		}
		sqlBuilder.append(colName);
		sqlBuilder.append(" = ?");
	    }
	}

	String sql = sqlBuilder.toString();
	ResultSetMetaData metaData = null;
	int index = 1;

	PreparedStatement pstmt = null;
	try {
	    pstmt = conn.prepareStatement(sql);
	    for (Object newValue : updateValueSet.values()) {
		pstmt.setObject(index++, newValue);
	    }
	    for (Object whereValue : keyValueSet.values()) {
		pstmt.setObject(index++, whereValue);
	    }
	    ret = pstmt.executeUpdate();
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (pstmt != null) {
		pstmt.close();
	    }
	}

	return ret;
    }

    @Override
    public long insertRecord(String tableName, Map<String, Object> insertValueSet) throws SQLException {
	long ret = 0;

	if (insertValueSet.isEmpty()) {
	    throw new IllegalArgumentException("Need something to insert!");
	}

	//Map<String,Integer>columns = this.getColumnTypes(tableName);
	StringBuilder sqlBuilder = new StringBuilder("insert into " + tableName + " ( ");
	StringBuilder valuesBuilder = new StringBuilder("values ( ");

	boolean firstOne = true;

	for (String colName : insertValueSet.keySet()) {
	    if (!firstOne) {
		sqlBuilder.append(", ");
		valuesBuilder.append(", ");
	    } else {
		firstOne = false;
	    }
	    sqlBuilder.append(colName);
	    valuesBuilder.append("?");
	}
	sqlBuilder.append(" ) ");
	sqlBuilder.append(valuesBuilder.toString());
	sqlBuilder.append(" ) ");

	String sql = sqlBuilder.toString();
	int index = 1;

	PreparedStatement pstmt = null;
	try {
	    pstmt = conn.prepareStatement(sql);
	    for (Object newValue : insertValueSet.values()) {
		pstmt.setObject(index++, newValue);
	    }
	    ret = pstmt.executeUpdate();
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (pstmt != null) {
		pstmt.close();
	    }
	}

	return ret;
    }

    @Override
    public long deleteRecords(String tableName, Map<String, Object> keyValueSet) throws SQLException {
	long ret = 0;

	if (keyValueSet.isEmpty()) {
	    throw new IllegalArgumentException("Cowardly refusing to allow deletion of all rows in a table!");
	}

	//Map<String,Integer>columns = this.getColumnTypes(tableName);
	StringBuilder sqlBuilder = new StringBuilder("delete from " + tableName + " where ");
	boolean firstOne = true;
	for (String colName : keyValueSet.keySet()) {
	    if (!firstOne) {
		sqlBuilder.append(" and ");
	    } else {
		firstOne = false;
	    }
	    sqlBuilder.append(colName);
	    sqlBuilder.append(" = ?");
	}

	String sql = sqlBuilder.toString();
	int index = 1;

	PreparedStatement pstmt = null;
	try {
	    pstmt = conn.prepareStatement(sql);
	    for (Object newValue : keyValueSet.values()) {
		pstmt.setObject(index++, newValue);
	    }
	    ret = pstmt.executeUpdate();
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (pstmt != null) {
		pstmt.close();
	    }
	}

	return ret;
    }

    public static void main(String[] args) throws Exception {
	DB_Accessor db = new DB_MySQL();
	db.openConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/hotel", "root", "admin");
	long retVal = 0;
	
	Map<String, Object> insRec = new HashMap<>();
	insRec.put("hotel_id", new Integer(0));
	insRec.put("hotel_name", "Hotel Number 3");
	insRec.put("street_address", "3 Hotel Street");
	insRec.put("city", "City of Hotels");
	insRec.put("state", "Florida");
	insRec.put("postal_code", "55555-5555");
	insRec.put("notes", "Some Notes about Hotel 3");
	retVal = db.insertRecord("hotel", insRec);
	System.out.println("db.insert returns " + retVal);

	Map rec = db.getRecordById("hotel", "hotel_id", 1);
	System.out.println(rec.toString());
	rec = db.getRecordById("hotel", "hotel_id", 2);
	System.out.println(rec.toString());
	rec = db.getRecordById("hotel", "hotel_id", 3);
	System.out.println(rec.toString());
	rec = db.getRecordById("hotel", "hotel_id", 4);
	System.out.println(rec.toString());
	rec = db.getRecordById("hotel", "hotel_id", 5);
	System.out.println(rec.toString());
	Map<String, Object> delRec = new HashMap<>();
	delRec.put("hotel_name", "Hotel Number 3");
	retVal = db.deleteRecords("hotel", delRec);
	System.out.println("db.delete returns " + retVal);
	rec = db.getRecordById("hotel", "hotel_id", 2);
	System.out.println(rec.toString());
	rec = db.getRecordById("hotel", "hotel_id", 3);
	System.out.println(rec.toString());
	rec = db.getRecordById("hotel", "hotel_id", 4);
	System.out.println(rec.toString());
    }
}
/*
    SELECT `hotel`.`hotel_id`,
    `hotel`.`hotel_name`,
    `hotel`.`street_address`,
    `hotel`.`city`,
    `hotel`.`state`,
    `hotel`.`postal_code`,
    `hotel`.`notes`
FROM `hotel`.`hotel`;

insert into hotel.hotel values (0, "hot
*/
