package com.example;

import java.io.*;
import java.util.List;
import java.util.Date;
import java.util.logging.Logger;
import org.json.*;

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
		
			Date today = new Date();
			String root = "https://storage.googleapis.com/data-quoteapp/back";
			int index = today.getDate();
			log.info("index date..."+index);
			int[] imagecnts = new int[]{1,2};
			log.info("index :"+index);
			int imgcnt = index % 2;
			log.info("image cnt"+imgcnt);
			JSONObject obj = new JSONObject();
			String quote = readQuote(index%10);
			obj.put("quote", quote);
			obj.put("image_url", root+imagecnts[imgcnt]+".jpg");
			log.info("final json"+obj.toString());
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
	
	
}