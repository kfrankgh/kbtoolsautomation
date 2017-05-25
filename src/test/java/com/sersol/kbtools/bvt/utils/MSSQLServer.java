package com.sersol.kbtools.bvt.utils;

import com.sersol.kbtools.bvt.configuration.DBEnvironment;
import java.sql.*;

public class MSSQLServer {

    private Connection conn = null;
    private String dbURL;
    private String username;
    private String password;

    /**
     * Connects to an MSSQL database.
     *
     * @param  dbName  the name of the database to connect to
     */
    public void setConnection(String dbName) {
        dbURL = ("jdbc:sqlserver://" + DBEnvironment.msSqlInstance + ";databaseName=" + dbName);
        username = DBEnvironment.msSqlUsername;
        password = DBEnvironment.msSqlPassword;
        try {
            conn = DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the MSSQL database.
     */
    public void closeConnection(){
        try{
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries the database that is currently being connected to.
     *
     * @param  the query to execute
     * @return    the result of the query
     */
    public ResultSet executeQuery(String sqlQuery){
        ResultSet rs = null;
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            if (stmt != null){
                try {
                    stmt.close();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
        }
        return rs;
    }
}
