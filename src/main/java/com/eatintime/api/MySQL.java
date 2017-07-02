package com.eatintime.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.LinkedList;

import com.eatintime.model.*;


public class MySQL {

    private Connection connect;
    private String username;
    private String password;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;


    public MySQL(String connectionString) throws Exception {
    	// This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");

        String[] terms = connectionString.split("[\\s@&.?$+-]+");

        // Setup the connection with the DB
        connect = DriverManager.getConnection(connectionString, terms[4], terms[5]);

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();        
    }

    public UserInfo retrieveUserAndDevice(int user_key) throws Exception {
    	/*
    	* retrieveDeviceKey uses the primary key for the table User
    	* in order to retrieve the list of the primary keys for the table Device
    	* 
    	* parameter
    	* user_key (int) : the primary key for the table User
    	* 
    	* returns
    	* userInfo (UserInfo) : the 
    	*/

    	preparedStatement = connect.prepareStatement("SELECT * FROM Device NATURAL JOIN User WHERE User.user_key = ?");
    	preparedStatement.setInt(1, user_key);

    	resultSet = preparedStatement.executeQuery();

    	UserInfo userInfo = null;

    	while (resultSet.next()){
    		if (userInfo == null){
    			userInfo = new UserInfo();
	    		userInfo.userKey  = resultSet.getInt("user_key");
	    		userInfo.userEmail = resultSet.getString("email");
	    		userInfo.firstName = resultSet.getString("firstname");
	    		userInfo.lastName = resultSet.getString("lastname");

	    		userInfo.devices = new LinkedList<Device>();
	    		Device retrievedDevice = new Device();
	    		retrievedDevice.device_key = resultSet.getInt("device_key");
	    		retrievedDevice.device_id = resultSet.getString("device_id");
	    		userInfo.devices.add(retrievedDevice);

    		}else{
    			Device retrievedDevice = new Device();
	    		retrievedDevice.device_key = resultSet.getInt("device_key");
	    		retrievedDevice.device_id = resultSet.getString("device_id");
	    		userInfo.devices.add(retrievedDevice);
    		}


    	}


    	return userInfo;


    }
}