package EatInTime;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.Long;
import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
* @RestController annotation tells you that this is a RESTful Web service component
**/
@RestController
public class Controller {

	// initialize the database with mongodb client
    private static MongoDB dbObjMongoDb = new MongoDB("mongodb://heroku_tw4s316k:knsfmmk94vnt3onv3a3n88b5hq@ds145188.mlab.com:45188/heroku_tw4s316k", "heroku_tw4s316k");
	
	/**
	* the index method is used to ensure the project is running correctly
	**/
	@RequestMapping("/")
	@ResponseBody
	public String index(){
		return "Index page rendered";
	}

	/**
	* the function to retreive all the data
	* --> will be deprecated
	*
	* returns
	* the stringified data in JSON
	**/
	@RequestMapping("/getData")
	public @ResponseBody String  getData(){
		MongoCollection<Document> documents = dbObjMongoDb.get("rawData");
		String str = "";
		for(Document doc : documents.find()){
			str = str.concat(doc.toJson());
		}
		return str;

	}

	/**
	* the function that retrieves the data within a specific date range
	* to do
	* 1. add the parameters (day, month, year, ...) --> will use switch method
	*
	* returns
	* the stringified data in JSON
	**/
	@RequestMapping("/getDataByDateRange")
	public @ResponseBody String  getDataByDateRange(){

		ArrayList<Document> documents = dbObjMongoDb.getByDateRange("rawData");
		String str = "";
		for(Document doc : documents){
			str = str.concat(doc.toJson());
		}
		return str;
	}


	/**
	* the function that gets the name of the table and the actual data from the parameters and inserts the data into the corresponding table (or collection)
	*
	* parameters
	* tableName : the name of the table (collection) you would like to insert the data into
	* inputLine : the data in JSON
	* 
	* returns
	* isSuccess : the integer result that tells you whether the insertion is successful
	* - 0 if the insertion is successful
	* - -1 if the insertion failed
	**/
	@RequestMapping("/insertNewData")
	public @ResponseBody int insertNewData(@RequestParam(value="tableName", required=true), @RequestParam(value="inputLine", required=true)){
		int isSuccess = dbObjMongoDb.insert(tableName, inputLine);
		return isSuccess;
	}




}