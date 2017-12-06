package com.example;

import java.io.*;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


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
		JSONObject resObj = new JSONObject();
		try{
			
			//create datastore service object
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter filter = null;
			
			//set the filter either EmpID or EmployeeName
			if(req.getParameter("EmpID")!=null)
			{
				 log.info("got employee id");
				 filter = new FilterPredicate("EmpID", FilterOperator.EQUAL, req.getParameter("EmpID"));
			}
			if(req.getParameter("EmployeeName")!=null)
			{
				log.info("got employee name");
				filter = new FilterPredicate("EmployeeName", FilterOperator.EQUAL, req.getParameter("EmployeeName"));
			}
			
			//prepare query
			Query q = new Query("PSLEmployee").setFilter(filter);
			PreparedQuery pq = datastore.prepare(q);
			
			//fetch respective entity
			Entity result = pq.asSingleEntity();
			log.info(result.toString());
			
			if(result==null){
				resObj.put("ERROR_MSG", Constants.ERROR_MSGS[1]);
				resObj.put("error_code", Constants.ERROR_CODES[1]);
			}else{
				//create json object
				
				resObj.put("EmpID", result.getProperty("EmpID"));
				resObj.put("EmployeeName", result.getProperty("EmployeeName"));
				resObj.put("BusinessUnit", result.getProperty("BusinessUnit"));
				resObj.put("Gender", result.getProperty("Gender"));
				resObj.put("Extention", result.getProperty("Extention"));
				resObj.put("Mobile", result.getProperty("Mobile"));
				resObj.put("Location", result.getProperty("Location"));
				resObj.put("ManagerName", result.getProperty("ManagerName"));
				resObj.put("DateOfBirth", result.getProperty("DateOfBirth"));
				resObj.put("DateOfJoining", result.getProperty("DateOfJoining"));
				resObj.put("LastLeaveDate", result.getProperty("LastLeaveDate"));
				resObj.put("LastLeaveReason", result.getProperty("LastLeaveReason"));
				resObj.put("OptionalLeave", result.getProperty("OptionalLeave"));
				resObj.put("OptionalHoliday", result.getProperty("OptionalHoliday"));
				resObj.put("CompensatoryOff", result.getProperty("CompensatoryOff"));
				resObj.put("PrivilegedLeave", result.getProperty("PrivilegedLeave"));
			}
			
			//return the json
			resp.getWriter().write(resObj.toJSONString());
			
		}catch(Exception e)
		{
			log.info("exception in getting employee"+e);
			resObj.put("ERROR_MSG", Constants.ERROR_MSGS[1]);
			resObj.put("error_code", Constants.ERROR_CODES[1]);
			resp.getWriter().write(resObj.toJSONString());
		}
		
	}
	
}