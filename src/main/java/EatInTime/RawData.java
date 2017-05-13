/**
* JsonData is the model used to insert data into the NoSQL database repository. 
* The class has the following members
* tableName (String) : the name of the table (or collection)
* jsoNData (String) : the raw data formatted in json 
**/
package EatInTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.Float;

public class RawData{
	@JsonProperty("NH3")
	public float nh3;
	@JsonProperty("CO")
	public float co;
	@JsonProperty("NO2")
	public float no2;
	@JsonProperty("C3H8")
	public float c3h8;
	@JsonProperty("C4H10")
	public float c4h10;
	@JsonProperty("CH4")
	public float ch4;
	@JsonProperty("H2")
	public float h2;
	@JsonProperty("C2H5OH")
	public float c2h5oh;

	public void print(){
		System.out.print(this.nh3 + " ");
		System.out.print(this.co + " ");
		System.out.print(this.no2 + " ");
		System.out.print(this.c3h8 + " ");
		System.out.print(this.c4h10 + " ");
		System.out.print(this.ch4 + " ");
		System.out.print(this.h2 + " ");
		System.out.println(this.c2h5oh + " ");
	}

	public String toString(){
		String data = Float.toString(this.nh3) + " " + 
			Float.toString(this.co) + " " + 
			Float.toString(this.no2) + " " + 
			Float.toString(this.c3h8) + " " + 
			Float.toString(this.cc4h10o) + " " + 
			Float.toString(this.ch4) + " " + 
			Float.toString(this.h2) + " " + 
			Float.toString(this.c2h5oh) + " ";
		return data;
	}
}