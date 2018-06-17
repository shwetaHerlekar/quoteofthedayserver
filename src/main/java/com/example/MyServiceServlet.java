package com.example;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

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
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			final Query q = new Query("quotes");
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> posts = pq.asList(FetchOptions.Builder.withLimit(31));
			
			for (Entity entity : posts) {
				log.info(""+entity.getProperty("text"));
			}

			
			resp.getWriter().write("cron job");
			
		}catch(Exception e)
		{
			log.info("exception in getting quotes"+e);
		}
		
	}
	
	public void readQuote(){
		
		log.info("inside readquote");
	}
	
	
}