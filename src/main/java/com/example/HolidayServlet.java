package com.example;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Servlet implementation class HolidayServlet
 */
public class HolidayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(HolidayServlet.class.getName());   
   
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("inside get method");
		response.setContentType("application/json");
		JSONObject resObj = new JSONObject();
		try{
			
			//create datastore service object and query
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query("HolidayCalender");
			PreparedQuery pq = datastore.prepare(q);
			Iterable<Entity> holidays = pq.asIterable();
			for (Entity holiday : holidays) {
				resObj.put(holiday.getProperty("Date"), holiday.getProperty("Holiday"));
			}
			
			//return the json
			response.getWriter().write(resObj.toJSONString());
			
		}catch(Exception e){
			log.info("error getting holidays from datastore");
			resObj.put("ERROR_MSG", Constants.ERROR_MSGS[1]);
			resObj.put("error_code", Constants.ERROR_CODES[1]);
			response.getWriter().write(resObj.toJSONString());
		}
	}
	
}
