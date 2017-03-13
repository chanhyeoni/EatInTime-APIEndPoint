package EatInTime;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

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

    Date endDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -7);
    Date startDate = cal.getTime();

    // filter out the date range 
    // check ou the MongoDB API JavaDoc 

    FindIterable<Document> result = database.getCollection(tableName).find(
      and(lt("date", endDate), gt("date", startDate))
    );

    ArrayList<Document> docs = new ArrayList();

    result.into(docs);

    return docs;
   }

   public void insert(String tableName, String data){
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
        }catch(ParseException ex){
         System.err.println("ParseException in insert of MongoDB.java -->" + ex.getMessage());
        }
   }
}