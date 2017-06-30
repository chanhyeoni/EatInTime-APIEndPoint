package com.eatintime.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.eatintime.model.*;

public class MongoDB {
   // MongoDB Client 
    private MongoClientURI connectionString;
    private MongoClient mongo;
    private MongoDatabase database;
    private String message;

    // JSON object
    private JSONParser parser = new JSONParser();

    /**
    * MongoDB constructor takes connection uri (connString) as well as the name of the databse (dbName)
    * to initialize the MongoDB instance
    * by means of using Java Client Library, the constructor utilizes MongoClientURI with the connection uri
    * to get the connectionString of type MongoClientURI, initializes MongoDatabase of type MongoClient, 
    * and get the MongoDatabase object by using the method getDatabase of the MongoClient instance,
    * using the parameter dbName
    *
    * parameters
    * connString (String) : connection uri (if local, it would be localhost)
    * dbName (String) : the name of the database to be accessed
    **/
   public MongoDB(String connString, String dbName){
      connectionString = new MongoClientURI(connString);
      mongo = new MongoClient(connectionString);
      database = mongo.getDatabase(dbName);
   }

   /**
   * getAllDataforUser joins all the data for a particular user using user_key
   * and returns the data aggregar
   * 
   **/
   // public MongoCollection<Document> getAllDataforUser(int device_key){

   //  // for test --> THIS MUST CHANGE!!!!
   //  // if device_key is zero, grab the MongoDB data that does not have device_key data
   //  // otherwise, use that key in order to retrieve the data 
   //  // whose device_key corresponds to the parameter
   //  boolean device_key_exists = true;
   //  if (device_key == 0){
   //    device_key_exists = true;
   //  }

   //  Document query = new Document("$device_key", new Document("$exists", device_key_exists));    

   //  FindIterable<Document> result = database.getCollection(tableName).find(query);




   // }

   /**
   * getByDateRange gets you the data whose range of date lies between the current date
   * and the date before the current date (startDate). startDate can be differed depending
   * on the type of date (day, week, month)
   *
   * parameters
   * dateType (DateType) : the enum type that helps set the date range
   * ex. if dateType is W (week), then the start date will be one week before the current date
   * tableName (String) : the name of the table or collection you would like to access and retrieve the data from
   *
   * returns
   * docs (ArrayList<Document>) : the list of Document objects that are retrieved as a result of MongoDB query
   **/
   public ArrayList<Document> getDataByDateRange(DateType dateType, String tableName){

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // get the current date (endDate)
    Date endDate = new Date();

    // use dateType of enum DateType to set the date range
    Calendar cal = Calendar.getInstance();
    switch (dateType){
      case D:
        cal.add(Calendar.DATE, -1);
        break;
      case W:
        cal.add(Calendar.DATE, -7);
        break;
      case M:
        cal.add(Calendar.MONTH, -1);
        break;
    }
    Date startDate = cal.getTime();

    String endDateStr = df.format(endDate);
    String startDateStr = df.format(startDate);

    // filter out the date range and place the result into ArrayList of type Document
    Document query = new Document("date", new Document("$gte", startDateStr).append("$lte", endDateStr));
    FindIterable<Document> result = database.getCollection(tableName).find(query);
    ArrayList<Document> docs = new ArrayList();
    result.into(docs);

    return docs;
   }

   public String insertRawData(RawData rawData){
      // insert the document based on the name of the table

        // initialize the date and convert it to the string format
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(currentDate);

        try{

           // solution 1 : store the data in the database
           // send the data to the database
           // 1. heroku mongodb database
           // access the collection
           MongoCollection<Document> collection = database.getCollection("rawData");

           if (collection != null){

              // if the table is rawData
              // create a new document
              Document doc = new Document("date", strDate)
                              .append("device_key", rawData.device_key)
                              .append("NH3", rawData.nh3)
                              .append("CO", rawData.co)
                              .append("NO2", rawData.no2)
                              .append("C3H8", rawData.c3h8)
                              .append("C4H10", rawData.c4h10)
                              .append("CH4", rawData.ch4)
                              .append("H2", rawData.h2)
                              .append("C2H5OH", rawData.c2h5oh);            
              // insert the new document into the collection
              collection.insertOne(doc);

           }
           return "success"; 
        }catch(Exception ex){
          String msg = "Exception in the function insert in MongoDB.java -->" + ex.getMessage();
          return msg;

        }
   }


   public String insertResult(String filePath, String uid){

        // initialize the date and convert it to the string format
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(currentDate);

        try{

           MongoCollection<Document> collection = database.getCollection("result");

           if (collection != null){
              // find the document that matches the type
              Document whereQuery = new Document();
              whereQuery.put("filePath", filePath);

              UpdateResult result = collection.updateOne(whereQuery, new Document("$set", new Document("uid", uid).append("date", strDate)));
              long nDocUpdate = result.getModifiedCount();

              if (nDocUpdate  <= 0){
                // insert
                // create a new document
                Document doc = new Document("date", strDate)
                                .append("filePath", filePath)
                                .append("uid", uid);         
                // insert the new document into the collection
                collection.insertOne(doc);

              }

              // if the table is rawData

           }
           return "success"; 
        }catch(Exception ex){
          String msg = "Exception in the function insert in MongoDB.java -->" + ex.getMessage();
          return msg;

        }
    }

    public String insertStatus(String type,  String msg){

        // initialize the date and convert it to the string format
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(currentDate);

        try{

           MongoCollection<Document> collection = database.getCollection("status");

           if (collection != null){
              // find the document that matches the type
              Document whereQuery = new Document();
              whereQuery.put("type", type);

              UpdateResult result = collection.updateOne(whereQuery, new Document("$set", new Document("msg", msg)));
              long nDocUpdate = result.getModifiedCount();

              if (nDocUpdate  <= 0){
                // insert
                // create a new document
                Document doc = new Document("date", strDate)
                                .append("type", type)
                                .append("msg", msg);         
                // insert the new document into the collection
                collection.insertOne(doc);

              }

           }
           return "success"; 
        }catch(Exception ex){
          String exception_msg = "Exception in the function insert in MongoDB.java -->" + ex.getMessage();
          return exception_msg;

        }
    }    


 }


