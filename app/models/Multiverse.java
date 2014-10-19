package models;

import java.util.*;
import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
 
import javax.net.ssl.HttpsURLConnection;



import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;
import com.bloomberglp.blpapi.Request;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.SessionOptions;



public class Multiverse{
    public String stockSymbol;
    public String stockName;
    public float lastPrice;
    public String bloombergRes;
    
    public Multiverse(String symbol) {
        this.stockSymbol = symbol;
        this.bloombergRes = "fake bloomberg response";
    }
    
    
    // HTTP GET request
	public void sendGet() throws Exception {
 
		String url = "http://dev.markitondemand.com/API/v2/Quote/json?symbol=AAPL";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
 
        
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//print result
		
		String str = response.toString();
        JSONObject obj2 = new JSONObject(str);
        this.stockName = obj2.getString("Name");

        this.lastPrice = Float.parseFloat(obj2.getString("LastPrice"));
        //System.out.println(n);  // prints "Alice 20"

		//System.out.println(response.toString());
	}
	
    // public String getTwitter(){
    //     String stock = this.stockSymbol;
        
    // }
	
	
	
    
    
}