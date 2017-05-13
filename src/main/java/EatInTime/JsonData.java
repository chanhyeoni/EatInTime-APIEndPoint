/**
* JsonData is the model used to insert data into the NoSQL database repository. 
* The class has the following members
* tableName (String) : the name of the table (or collection)
* jsoNData (String) : the raw data formatted in json 
**/
package EatInTime;

public class JsonData{
	@JsonProperty("tableName")
	public String tableName;
	@JsonProperty("jsonData")
	public String jsonData;


	public void print(){
		System.out.print(this.tableName + " ");
		System.out.println(this.jsonData);
	}
}