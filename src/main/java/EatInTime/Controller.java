package EatInTime;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

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
	public @ResponseBody String  getDataByDateRange(@RequestParam(value="dateType", required=true, defaultValue="D") String dateType){

		ArrayList<Document> documents = new ArrayList<Document>();
		switch(dateType){
			case "D":
				documents = dbObjMongoDb.getByDateRange(DateType.D, "rawData");
				break;
			case "W":
				documents = dbObjMongoDb.getByDateRange(DateType.W, "rawData");
				break;
			case "M":
				documents = dbObjMongoDb.getByDateRange(DateType.M, "rawData");
				break;			
		}

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
	* tableName (String) : the name of the table (collection) you would like to insert the data into
	* inputLine (String) : the data in JSON
	* 
	* returns
	* successMsg (String) : the integer result that tells you whether the insertion is successful
	* - 'success' if the insertion is successful
	* - the error message if the insertion fails
	**/
	@RequestMapping(value="/insertNewData", method=RequestMethod.POST)
	public @ResponseBody String insertNewData(@RequestParam(value="inputLine", required=true) JsonData inputLine){
		inputLine.print();
		//String successMsg = dbObjMongoDb.insert(inputLine);
		return inputLine;
	}




}