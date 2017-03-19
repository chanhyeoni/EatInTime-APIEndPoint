package EatInTime;

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
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDB{
   // MongoDB Client 
    private MongoClientURI connectionString;
    private MongoClient mongo;
    private MongoDatabase database;
    private String message;

    // JSON object
    private JSONParser parser = new JSONParser();

   public MongoDB(String connString, String dbName){
      connectionString = new MongoClientURI(connString);
      mongo = new MongoClient(connectionString);
      database = mongo.getDatabase(dbName);
   }

   public MongoCollection<Document> get(String tableName){
         return database.getCollection(tableName);
   }

   public ArrayList<Document> getByDateRange(String tableName){

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date endDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -7);
    Date startDate = cal.getTime();

    String endDateStr = df.format(endDate);
    String startDateStr = df.format(startDate);

    // filter out the date range 
    // check ou the MongoDB API JavaDoc 

    Document query = new Document("date", new Document("$gte", startDateStr).append("$lte", endDateStr));

    FindIterable<Document> result = database.getCollection(tableName).find(query);

    ArrayList<Document> docs = new ArrayList();

    result.into(docs);

    return docs;
   }

   public int insert(String tableName, String data){
      // insert the document based on the name of the table

        // initialize the date and convert it to the string format
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(currentDate);

        try{
         // parse the json data
           Object obj = parser.parse(data);
           JSONObject jsonObj = (JSONObject) obj;

           // solution 1 : store the data in the database
           // send the data to the database
           // 1. heroku mongodb database
           // access the collection
           MongoCollection<Document> collection = database.getCollection(tableName);

           if (collection != null){

            switch (tableName){
               case "rawData":
                  // if the table is rawData
                   // create a new document
                    Document doc = new Document("date", strDate)
                                    .append("temperature", jsonObj.get("temperature"))
                                    .append("light", jsonObj.get("light"));            
                    // insert the new document into the collection
                    collection.insertOne(doc);
                  break;
               default:
                  break;
            }
           }
           return 0; 
        }catch(Exception ex){
          System.err.println("Exception in the function insert in MongoDB.java -->" + ex.getMessage());
          return -1;

        }

        return -1;
   }
}