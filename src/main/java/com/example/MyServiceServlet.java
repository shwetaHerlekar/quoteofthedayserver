package com.example;

import java.io.*;
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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


// [START example]
@SuppressWarnings("serial")
public class MyServiceServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(MyServiceServlet.class.getName());

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("inside get method");
		resp.setContentType("application/json");
		if(req.getParameter("EmpID")!=null)
		{
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter p = new FilterPredicate("EmpID", FilterOperator.EQUAL, req.getParameter("EmpID"));
			Query q = new Query("PersistentEmployees").setFilter(p);
			PreparedQuery pq = datastore.prepare(q);
			Entity result = pq.asSingleEntity();
			log.info(result.toString());
			JSONObject resObj = new JSONObject();
			resObj.put("EmpID", result.getProperty("EmpID"));
			resObj.put("EmployeeName", result.getProperty("EmployeeName"));
			resObj.put("Gender", result.getProperty("Gender"));
			resObj.put("Extention", result.getProperty("Extention"));
			resObj.put("DateOfBirth", result.getProperty("DateOfBirth"));
			resObj.put("DateOfJoining", result.getProperty("DateOfJoining"));
			resObj.put("LastLeaveDate", result.getProperty("LastLeaveDate"));
			resObj.put("LastLeaveReason", result.getProperty("LastLeaveReason"));
			resp.getWriter().write(resObj.toJSONString());
		}
		if(req.getParameter("EmployeeName")!=null)
		{
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter p = new FilterPredicate("EmployeeName", FilterOperator.EQUAL, req.getParameter("EmployeeName"));
			Query q = new Query("PersistentEmployees").setFilter(p);
			PreparedQuery pq = datastore.prepare(q);
			Entity result = pq.asSingleEntity();
			log.info(result.toString());
			JSONObject resObj = new JSONObject();
			resObj.put("EmpID", result.getProperty("EmpID"));
			resObj.put("EmployeeName", result.getProperty("EmployeeName"));
			resObj.put("Gender", result.getProperty("Gender"));
			resObj.put("Extention", result.getProperty("Extention"));
			resObj.put("DateOfBirth", result.getProperty("DateOfBirth"));
			resObj.put("DateOfJoining", result.getProperty("DateOfJoining"));
			resObj.put("LastLeaveDate", result.getProperty("LastLeaveDate"));
			resObj.put("LastLeaveReason", result.getProperty("LastLeaveReason"));
			resp.getWriter().write(resObj.toJSONString());
		}
		
	}
}