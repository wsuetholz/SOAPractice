/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suetholz.net.dba;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wsuetholz
 */
public interface DB_Accessor {

    public void closeConnection() throws SQLException;

    public void openConnection(String driverClass, String url, String userName, String password) throws ClassNotFoundException, SQLException;
    
    public List<Map<String,Object>> getAllRecords (String tableName) throws SQLException;
    
    public Map<String,Object> getRecordById(String tableName, String keyName, Object keyValue) throws SQLException;

    public long updateRecords (String tableName, Map<String, Object> keyValueSet, Map<String, Object> updateValueSet) throws SQLException;
    
    public long insertRecord (String tableName, Map<String, Object>insertValueSet) throws SQLException;
    
    public long deleteRecords (String tableName, Map<String, Object>keyValueSet) throws SQLException;
}
