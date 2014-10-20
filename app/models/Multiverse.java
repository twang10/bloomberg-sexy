
package models;

import twitter4j.*;
import twitter4j.conf.*;

import java.util.List;

import java.util.*;
import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Multiverse{
    public String stockSymbol;
    public String stockName;
    public double lastPrice;
    public String bloombergRes;

    public String[] giphyURL= new String[3];
    public String wikiSnippet = "";
    public String[] tweets = new String[3];
    public double bitcoinPrice;
    public String redditComment = "";
    public String movieString = "";
    public String[] movieList = {"", "", ""};
    public String NYT = "";
    public String picture = "";

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
            Query query = new Query(this.stockSymbol);
            query.setResultType(Query.POPULAR);
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();
              if(!tweets.isEmpty()){
                for(int i = 0; i < 3 && i < tweets.size(); i++){
                  if(tweets.get(i) != null){
                    this.tweets[i] = "@" + tweets.get(i).getUser().getScreenName() + ":" + tweets.get(i).getText();
                  }
                }
              }

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

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
      if(jsonArr.length() != 0){
        for(int i = 0; i < jsonArr.length() && i < 3; i++){
      		org.json.JSONObject imJSON = jsonArr.getJSONObject(i);
      		this.giphyURL[i] = imJSON.getJSONObject("images").getJSONObject("fixed_height").getString("url");
              //this.giphyURL = obj2.meta.msg;
      		System.out.println(this.giphyURL);
        }
      }
    }

    public void getWiki() throws Exception{
        String word = getFirstWord(this.stockName);
        String url = "http://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="
                      + this.stockName +"&srprop=snippet&format=json";
        URL obj = new URL(url.replace(" ", "%20"));
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
        org.json.JSONObject objQ = obj2.getJSONObject("query");
        org.json.JSONArray jsonArr = objQ.getJSONArray("search");
        org.json.JSONObject search = jsonArr.getJSONObject(0);
        this.wikiSnippet = search.getString("snippet");
        System.out.println(this.wikiSnippet);
    }

    public void getBitcoin() throws Exception{

        String url = "https://api.bitcoinaverage.com/ticker/USD/last";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
        this.bitcoinPrice = lastPrice / Double.parseDouble(response.toString());
        System.out.println(response);
    }

    public void getReddit() throws Exception{
        String word = getFirstWord(this.stockName);
        String url = "http://www.reddit.com/search.json?q=" + word + "&time=week";
        URL obj = new URL(url.replace(" ", "%20"));
        System.out.println("\nSending 'GET' request to URL : " + obj.toString());

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String str = response.toString();

        //org.json.JSONArray jsonArr = new org.json.JSONArray(str);
        org.json.JSONObject data = new org.json.JSONObject(str).getJSONObject("data");
        org.json.JSONArray jsonArr2 = data.getJSONArray("children");
        org.json.JSONObject data2 = jsonArr2.getJSONObject(0).getJSONObject("data");
        String comments = "http://www.reddit.com" + data2.getString("permalink") + ".json?&limit=10";
        System.out.println(comments);

        URL obj2 = new URL(comments);
        System.out.println("\nSending 'GET' request to URL : " + obj2.toString());

        HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
        con2.setRequestMethod("GET");
        int responseCode2 = con2.getResponseCode();

        System.out.println("Response Code : " + responseCode2);
        BufferedReader in2 = new BufferedReader(
                new InputStreamReader(con2.getInputStream()));
        String inputLine2;
        StringBuffer response2 = new StringBuffer();
        while ((inputLine2 = in2.readLine()) != null) {
            response2.append(inputLine2);
        }
        in2.close();

        String str2 = response2.toString();
        org.json.JSONArray jsonArray = new org.json.JSONArray(str2);


          org.json.JSONObject temp = jsonArray.getJSONObject(1);
          org.json.JSONObject parData = temp.getJSONObject("data");
          //org.json.JSONArray jsonArr = new org.json.JSONArray(str);
          org.json.JSONArray jsonArray2 = parData.getJSONArray("children");
          int vall = jsonArray2.length();
          for(int i = 0; i < vall - 1; i++){
            org.json.JSONObject childData = jsonArray2.getJSONObject(i).getJSONObject("data");
            String commentVal = childData.getString("body");
            System.out.println(commentVal);
            this.redditComment += commentVal + "<br/><br/>";
          }
    }

    public void getMovies() throws Exception{
        String word = getFirstWord(this.stockName);
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=hk3pwaxvf7m37dbqhh5bgmg6&q=" +
                      word + "&page_limit=3";
        URL obj = new URL(url.replace(" ", "%20"));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
        String str = response.toString();
        org.json.JSONObject obj2 = new org.json.JSONObject(str);
        //Integer.parseInt(jsonObj.get("id"));
        //String numMovies = obj2.getString("total");
        System.out.println("AAAAA: " + (obj2.get("total").getClass()));
        int numMovies = (Integer)obj2.get("total");
        this.movieString = this.stockName + " is linked to " + numMovies + " movies, the top ones are: ";
        System.out.println(movieString);

        for(int i = 0; i < numMovies && i < 3; i++){
            org.json.JSONArray array = obj2.getJSONArray("movies");
            org.json.JSONObject movie = array.getJSONObject(i);
            String title = movie.getString("title");
            this.movieList[i] = title;
            System.out.println(title);
        }
    }

    public void getNYT() throws Exception{
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q=" +
        this.stockName + "&begin_date=20130101&end_date=20141018&api-key=77a7c6c23825ba8ad653d97b02edefd3%3A2%3A70027345";
        URL obj = new URL((url.replace(" ", "%20")));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
        String str = response.toString();
        org.json.JSONObject obj2 = new org.json.JSONObject(str);
        org.json.JSONObject res = obj2.getJSONObject("response");
        org.json.JSONArray docs = res.getJSONArray("docs");
        String snippet = docs.getJSONObject(0).getString("snippet");
        String link = docs.getJSONObject(0).getString("web_url");

        this.NYT = "<a href='" + link + "'>" +
                    snippet + "</a>";

        System.out.println(this.NYT);
    }

    public void getPic() throws Exception{
        String name = getFirstWord(this.stockName);
        String url = "http://pixabay.com/api/?username=nusoff01&key=95296a61e67cf1795196&search_term=" + name +
                        "&image_type=photo";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
        String str = response.toString();
        org.json.JSONObject obj2 = new org.json.JSONObject(str);
        int numPics = (Integer)obj2.get("totalHits");
        if(numPics > 0){
          org.json.JSONArray hits = obj2.getJSONArray("hits");
          String webURL = hits.getJSONObject(0).getString("webformatURL");
          System.out.println(webURL);
          this.picture = webURL;
        }
    }
}