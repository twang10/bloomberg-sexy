package models;

import java.util.*;

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
    public String bloombergRes;
    
    public Multiverse(String symbol) {
        this.stockSymbol = symbol;
        this.bloombergRes = "fake bloomberg response";
    }
    
    
    // HTTP GET request
	public void sendGet() throws Exception {
 
		String url = "http://www.google.com/search?q=mkyong";
 
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
		System.out.println(response.toString());
	}
	

	public void run() throws Exception
    {
        
        int serverPort = 8194;

        SessionOptions sessionOptions = new SessionOptions();
        String serverHost = "10.8.8.1";
        sessionOptions.setServerHost(serverHost);
        sessionOptions.setServerPort(serverPort);

        System.out.println("Connecting to " + serverHost + ":" + serverPort);
        Session session = new Session(sessionOptions);
        if (!session.start()) {
            System.err.println("Failed to start session.");
            return;
        }
        if (!session.openService("//blp/refdata")) {
            System.err.println("Failed to open //blp/refdata");
            return;
        }
        Service refDataService = session.getService("//blp/refdata");
        Request request = refDataService.createRequest("HistoricalDataRequest");

        Element securities = request.getElement("securities");
        securities.appendValue("IBM US Equity");
        securities.appendValue("MSFT US Equity");

        Element fields = request.getElement("fields");
        fields.appendValue("PX_LAST");
        fields.appendValue("OPEN");

        request.set("periodicityAdjustment", "ACTUAL");
        request.set("periodicitySelection", "MONTHLY");
        request.set("startDate", "20060101");
        request.set("endDate", "20061231");
        request.set("maxDataPoints", 100);
        request.set("returnEids", true);

        System.out.println("Sending Request: " + request);
        session.sendRequest(request, null);

        while (true) {
            Event event = session.nextEvent();
            MessageIterator msgIter = event.messageIterator();
            while (msgIter.hasNext()) {
                Message msg = msgIter.next();
                System.out.println(msg);
            }
            if (event.eventType() == Event.EventType.RESPONSE) {
                break;
            }
        }
    }
	
	
    
    
}