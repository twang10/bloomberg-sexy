
package models;

import twitter4j.*;
import twitter4j.conf.*;

import java.util.List;


// import play.libs.F.Function;
// import play.libs.F.Option;
// import play.libs.F.Promise;
// import play.libs.oauth.OAuth;
// import play.libs.oauth.OAuth.ConsumerKey;
// import play.libs.oauth.OAuth.OAuthCalculator;
// import play.libs.oauth.OAuth.RequestToken;
// import play.libs.oauth.OAuth.ServiceInfo;
// import play.libs.ws.WSClient;
// import play.libs.ws.WSResponse;
// import play.mvc.Result;

// import com.google.common.base.Strings;

// import javax.inject.Inject;



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
    public double lastPrice;
    public String bloombergRes;

    public String giphyURL;

    public String[] tweets = new String[3];

    
    public Multiverse(String symbol) {
        this.stockSymbol = symbol;
    }
    
    
    // HTTP GET request
	public void init() throws Exception {
    
		String url = "http://dev.markitondemand.com/API/v2/Quote/json?symbol=";
        url += this.stockSymbol;
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
        org.json.JSONObject obj2 = new org.json.JSONObject(str);
        this.stockName = obj2.getString("Name");

        this.lastPrice = obj2.getDouble("LastPrice");
        //System.out.println(n);  // prints "Alice 20"

		//System.out.println(response.toString());
	}
	
    public void getTwitter(){
        System.out.println("starting get twitter");
        String stock = this.stockSymbol;

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("y8l2gv9Zxm2Kvq78PkGeI1y4K")
          .setOAuthConsumerSecret("WBMht3ezjYkWms2InMwgRhAuYwWBVKVVMNx4QokHmieantvbTY")
          .setOAuthAccessToken("2179291328-OdLzCA4wITLwv3qkIxGaVnkZgJ2ljGDonnX3GOd")
          .setOAuthAccessTokenSecret("wgiYcPVtIu02HxNUbKAsSJwawtnoiPumbHNbZ6jk4XeKs");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
                        
        try{
            Query query = new Query("$" + this.stockSymbol);
            query.setResultType(Query.POPULAR);
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            
            for(int i = 0; i < 3; i++){
                this.tweets[i] = "@" + tweets.get(i).getUser().getScreenName() + ":" + tweets.get(i).getText();
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
    
    
//     public void getInsta() throws Exception {
    
//         String url = "https://api.instagram.com/v1/tags/" + this.stockName + 
//                      "snow/media/recent?access_token=" + ACCESS-TOKEN;
    
// 		String url = "http://dev.markitondemand.com/API/v2/Quote/json?symbol=";
//         url += this.stockSymbol;
// 		URL obj = new URL(url);
// 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

// 		// optional default is GET
// 		con.setRequestMethod("GET");
 
        
 
// 		int responseCode = con.getResponseCode();
// 		System.out.println("\nSending 'GET' request to URL : " + url);
// 		System.out.println("Response Code : " + responseCode);
 
// 		BufferedReader in = new BufferedReader(
// 		        new InputStreamReader(con.getInputStream()));
// 		String inputLine;
// 		StringBuffer response = new StringBuffer();
 
// 		while ((inputLine = in.readLine()) != null) {
// 			response.append(inputLine);
// 		}
// 		in.close();
// 		//print result
		
// 		String str = response.toString();
//         org.json.JSONObject obj2 = new org.json.JSONObject(str);
//         this.stockName = obj2.getString("Name");

//         this.lastPrice = obj2.getDouble("LastPrice");
//         //System.out.println(n);  // prints "Alice 20"

// 		//System.out.println(response.toString());
// 	}
    
    
    private String getFirstWord(String text) {
        if (text.indexOf(' ') > -1) { 
            return text.substring(0, text.indexOf(' ')); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
    }
    
    public void getGiphy() throws Exception{
        System.out.println("starting giphy");
        
        String word = getFirstWord(this.stockName);
        //System.out.println(word);
        String url = "http://api.giphy.com/v1/gifs/search?q=" + word + "&api_key=dc6zaTOxFJmzC";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default 
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
		
		org.json.JSONObject obj2 = new org.json.JSONObject(str);
		org.json.JSONArray jsonArr = obj2.getJSONArray("data"); 
		org.json.JSONObject imJSON = jsonArr.getJSONObject(0);
		this.giphyURL = imJSON.getJSONObject("images").getJSONObject("fixed_height").getString("url");
        //this.giphyURL = obj2.meta.msg;
		System.out.println(this.giphyURL);
        
        //System.out.println(n);  // prints "Alice 20"

		//System.out.println(response.toString());
        
    }
    
    
}