package com.ibm.cdi.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.ibm.db2.jcc.am.Connection;
import com.ibm.db2.jcc.am.ResultSet;
import com.ibm.nosql.json.api.BasicDBList;
import com.ibm.nosql.json.api.BasicDBObject;
import com.ibm.nosql.json.util.JSON;


import org.apache.logging.log4j.Logger ;
import org.apache.logging.log4j.Level ;
import org.apache.logging.log4j.LogManager;


public class DashConn {

	static String databaseHost = null;
	static String databaseName = null;
	static int port = 0;
	static String user = null;
	static String password = null;
	static String url = null;
	static Logger logger = LogManager.getLogger(DashConn.class.getName());
	static Level level = Level.toLevel("Info");

	static Connection con = null;

	
	private static void connectDash() throws SQLException{	   
		// VCAP_SERVICES is a system environment variable
		// Parse it to obtain the DB2 connection info
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		logger.info("VCAP_SERVICES content: " + VCAP_SERVICES);
		 
		if (VCAP_SERVICES != null) {
		    // parse the VCAP JSON structure
		    BasicDBObject obj = (BasicDBObject) JSON.parse(VCAP_SERVICES);
		    String thekey = null;
		    Set<String> keys = obj.keySet();
		    logger.info("Searching through VCAP keys");
		    // Look for the VCAP key that holds the SQLDB information
		    for (String eachkey : keys) {
		        logger.info("Key is: " + eachkey);
		        // Just in case the service name gets changed to lower case in
		        // the future, use toUpperCase
		        if (eachkey.startsWith("dashDB")) {
		            thekey = eachkey;
		        }
		    }
		    if (thekey == null) {
		    	logger.error("Cannot find any DashDB service in the VCAP; exiting");
		    }
		    BasicDBList list = (BasicDBList) obj.get(thekey);
		    obj = (BasicDBObject) list.get("0");
		    logger.info("Service found: " + obj.get("name"));
		    // parse all the credentials from the vcap env variable
		    obj = (BasicDBObject) obj.get("credentials");
		    databaseHost = (String) obj.get("host");
		    databaseName = (String) obj.get("db");
		    port = (int) obj.get("port");
		    user = (String) obj.get("username");
		    password = (String) obj.get("password");
		    url = (String) obj.get("jdbcurl");
		    
			try {
				con = (Connection) DriverManager.getConnection(url,user,password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("connected to dash sydney successfully");

			Statement stmt = null;
			String sqlStatement = "";

			stmt = con.createStatement();

			sqlStatement = "SELECT metric_ID, Metric_Desc FROM metric_types";
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlStatement);

			while (rs.next()) {
				System.out.print("syndey results: "+rs.getString(2) );
			}
			stmt.close();
		}	
	}

	public static void main(String args[]) throws SQLException {
		
		connectDash();
		connectTest();
	}
	private static void connectTest() throws SQLException{
		Connection con = null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			String url = "jdbc:db2://awh-yp-small02.services.dal.bluemix.net:50000/BLUDB";
			String user = "dash111017";
			String password="XqYWp3utPxej";
			try {
				con = (Connection) DriverManager.getConnection(url,user,password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("connected to dash dallas successfully");

			Statement stmt = null;
			String sqlStatement = "";

			stmt = con.createStatement();

			sqlStatement = "SELECT metric_ID, Metric_Desc FROM metric_types";
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlStatement);

			while (rs.next()) {
				System.out.print("syndey results: "+ rs.getString(2) );
			}
			stmt.close();
		}

		catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound Exception: " + e);
			return;
		}

	}

}
