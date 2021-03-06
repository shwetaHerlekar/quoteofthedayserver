package com.example;

import java.io.*;
import java.util.List;
import java.util.Date;
import java.util.logging.Logger;

import org.json.*;

import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions;


// [START example]
@SuppressWarnings("serial")
public class MyServiceServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(MyServiceServlet.class.getName());

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("inside get method");
		resp.setContentType("application/json");
		//JSONObject resObj = new JSONObject();
		try{
			
			log.info("using cron job..........................");
		
			int random = (int )(Math.random() * 50 + 1);
			Date today = new Date();
			String root = "https://storage.googleapis.com/data-quoteapp/back";
			//int index = today.getDate();
			int index = random;
			log.info("index date..."+index);
			int[] imagecnts = new int[]{1,2,3,4,5,6,7,8};
			log.info("index :"+index);
			int imgcnt = index % 8;
			log.info("image cnt"+imgcnt);
			JSONObject obj = new JSONObject();
			String quote = readQuote(index%10);
			obj.put("quote", quote);
			obj.put("image_url", root+imagecnts[imgcnt]+".jpg");
			log.info("final json"+obj.toString());
			pushNotification(quote, root+imagecnts[imgcnt]+".jpg?avoidCache="+random);
			resp.getWriter().write(obj.toString());
			
		}catch(Exception e)
		{
			log.info("exception in getting quotes"+e);
		}
		
	}
	
	public String readQuote(int index){
		
		log.info("inside readquote");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		final Query q = new Query("quotes");
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> posts = pq.asList(FetchOptions.Builder.withLimit(31));
		
		return String.valueOf(posts.get(index).getProperty("text"));
		
	}
	
	public void pushNotification(String msg, String link){
		String authKey = "AAAA07PX7_8:APA91bHFK63ZCC9B7OIYQ-hwY4vvKUgsJtNYI_CbIB8m_xV8XLbsNwAwl31Fy18xQJ6rnkIKTK_-N2ukgvlae0QWPWsW7mJvWC63G3eupb6vRHuQwScF3cVqGQRyzmGzvV1bHK-Nv2JK";   // You FCM AUTH key
	    String FMCurl = "https://fcm.googleapis.com/fcm/send";


	    try {
	    	URL url = new URL(FMCurl);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		    conn.setUseCaches(false);
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization","key="+authKey);
		    conn.setRequestProperty("Content-Type","application/json");

		    JSONObject json = new JSONObject();
		    json.put("to","/topics/quotes");
		    //JSONObject notfn = new JSONObject();
		    //notfn.put("title","Quote Of The Day");
		    //json.put("notification", notfn);
		    JSONObject data = new JSONObject();
		    data.put("message",msg);
		    data.put("url", link);
		    data.put("forceStart", "1");
		    json.put("click_action", ".QuoteActivity");
		    json.put("data", data);

		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(json.toString());
		    wr.flush();
		    wr.close();
		    
		    int responseCode = conn.getResponseCode();
		    log.info("responsecode :"+responseCode);
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		    StringBuffer response = new StringBuffer();
		    
		    while((inputLine = in.readLine())!=null){
		    	response.append(inputLine);
		    }
		    in.close();
		    log.info("response:"+response);
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("exception in pushing quote"+e);
		}
	    
	}
	
	
}