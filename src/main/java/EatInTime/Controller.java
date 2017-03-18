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

/**
* @RestController annotation tells you that this is a RESTful Web service component
**/
@RestController
public class Controller {

	// initialize the database with mongodb client
    private static MongoDB dbObjMongoDb = new MongoDB("mongodb://heroku_tw4s316k:knsfmmk94vnt3onv3a3n88b5hq@ds145188.mlab.com:45188/heroku_tw4s316k", "heroku_tw4s316k");
	
	@RequestMapping("/")
	@ResponseBody
	public String index(){
		return "Hello World from Greeter" + "\n";
	}

	@RequestMapping("/getData")
	public @ResponseBody String  getData(){
		MongoCollection<Document> documents = dbObjMongoDb.get("rawData");

		String str = "";
		for(Document doc : documents.find()){
			str = str.concat(doc.toJson());
		}

		return str;

	}

	@RequestMapping("/getDataByDateRange")
	public @ResponseBody String  getDataByDateRange(){
		ArrayList<Document> documents = dbObjMongoDb.getByDateRange("rawData");

		String str = "";
		for(Document doc : documents){
			str = str.concat(doc.toJson());
		}

		return str;

	}


	@RequestMapping("/getDateRange")
	public @ResponseBody String getDateRange(){

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		Date endDate = new Date();
		Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -7);
    	Date startDate = cal.getTime();

    	String endDateStr = df.format(endDate);
    	String startDateStr = df.format(startDate);

    	return "End Date --> " + endDateStr + " Start Date --> " + startDateStr;

	}

}