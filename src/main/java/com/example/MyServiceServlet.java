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
	
	@SuppressWarnings({ "null", "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject responseObj=null;
		
		try{
			String responseJson = ReaderUtil.readPostParameter(req);
			JSONParser parser = new JSONParser();
			JSONObject jsonResponseObject;
			jsonResponseObject = (JSONObject) parser
					.parse(responseJson);
			
			
			int response = addLeaveTransaction(jsonResponseObject);
			if(response == 1){
				response = deductLeaves(jsonResponseObject);
				if(response == 1)
				{
					responseObj.put("error_code", Constants.ERROR_CODES[0]);
					responseObj.put("ERROR_MSG", Constants.ERROR_MSGS[0]);
					resp.getWriter().write(responseObj.toJSONString());
				}
			}
			else{
				responseObj.put("error_code", Constants.ERROR_CODES[1]);
				responseObj.put("ERROR_MSG", Constants.ERROR_MSGS[1]);
				resp.getWriter().write(responseObj.toJSONString());
			}
		}catch(Exception e){
			log.info("exception in applying leaves"+e);
			responseObj.put("error_code", Constants.ERROR_CODES[1]);
			responseObj.put("ERROR_MSG", Constants.ERROR_MSGS[1]);
			resp.getWriter().write(responseObj.toJSONString());
		}
	}
	
	public int addLeaveTransaction(JSONObject jsonResponseObject) {
		int response = -1;
		try{
			String employeeName = jsonResponseObject.get("employeeName").toString();
			String startDate = jsonResponseObject.get("startDate").toString();
			String endDate = jsonResponseObject.get("endDate").toString();
			String reason = jsonResponseObject.get("reason").toString();
			String typeOfLeave = jsonResponseObject.get("typeOfLeave").toString();
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Entity leaveTransaction = new Entity("LeaveTransactions");
			leaveTransaction.setProperty("EmployeeName", employeeName);
			leaveTransaction.setProperty("StartDate", startDate);
			leaveTransaction.setProperty("EndDate", endDate);
			leaveTransaction.setProperty("TypeOfLeave", typeOfLeave);
			leaveTransaction.setProperty("Reason", reason);
			datastore.put(leaveTransaction);
			response = 1;
		}catch(Exception e){
			log.info("error in adding leave transaction"+e);
			return response;
		}
		return response;
	}
	
	public int deductLeaves(JSONObject jsonResponseObject) {
		int response = -1;
		try{
			String employeeName = jsonResponseObject.get("employeeName").toString();
			int noOfDays = Integer.parseInt(jsonResponseObject.get("noOfDays").toString());
			String typeOfLeave = jsonResponseObject.get("typeOfLeave").toString();
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter filter = new FilterPredicate("EmployeeName", FilterOperator.EQUAL, employeeName);
			
			//prepare query
			Query q = new Query("PSLEmployee").setFilter(filter);
			PreparedQuery pq = datastore.prepare(q);
			
			//fetch respective entity
			Entity employee = pq.asSingleEntity();
			log.info(employee.toString());
			
			if(typeOfLeave.equals("PL")){
				int leaves = Integer.parseInt(employee.getProperty("PrivilegedLeave").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("PrivilegedLeave", leaves);
			}
			if(typeOfLeave.equals("OH")){
				int leaves = Integer.parseInt(employee.getProperty("OptionalHoliday").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("OptionalHoliday", leaves);
			}
			if(typeOfLeave.equals("OL")){
				int leaves = Integer.parseInt(employee.getProperty("OptionalLeave").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("OptionalLeave", leaves);
			}
			if(typeOfLeave.equals("CompOff")){
				int leaves = Integer.parseInt(employee.getProperty("CompensatoryOff").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("CompensatoryOff", leaves);
			}
			
			datastore.put(employee);
			response = 1;
		}catch(Exception e){
			log.info("error updating employeee"+e);
		}
		return response;
	}
}