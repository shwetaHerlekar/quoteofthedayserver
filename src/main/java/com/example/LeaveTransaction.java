package com.example;

import java.io.IOException;
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

/**
 * Servlet implementation class LeaveTransaction
 */
public class LeaveTransaction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger log = Logger.getLogger(MyServiceServlet.class.getName());
	
	@SuppressWarnings({ "null", "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject responseObj=new JSONObject();
		log.info("inside apply leave");
		
		try{
			String responseJson = ReaderUtil.readPostParameter(req);
			JSONParser parser = new JSONParser();
			JSONObject jsonResponseObject;
			jsonResponseObject = (JSONObject) parser
					.parse(responseJson);
			log.info(jsonResponseObject.toJSONString());
			int response = addLeaveTransaction(jsonResponseObject);
			if(response == 1){
				response = deductLeaves(jsonResponseObject);
				if(response == 1)
				{
					log.info("leave applied and deducted");
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
		log.info("inside add leave transaction");
		int response = -1;
		try{
			String employeeName = jsonResponseObject.get("employeeName").toString();
			String startDate = jsonResponseObject.get("startDate").toString();
			String endDate = jsonResponseObject.get("endDate").toString();
			String reason = jsonResponseObject.get("reason").toString();
			String typeOfLeave = jsonResponseObject.get("typeOfLeave").toString();
			log.info("type :"+typeOfLeave);
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Entity leaveTransaction = new Entity("LeaveTransactions");
			leaveTransaction.setProperty("EmployeeName", employeeName);
			leaveTransaction.setProperty("StartDate", startDate);
			leaveTransaction.setProperty("EndDate", endDate);
			leaveTransaction.setProperty("TypeOfLeave", typeOfLeave);
			leaveTransaction.setProperty("Reason", reason);
			log.info(leaveTransaction.toString());
			datastore.put(leaveTransaction);
			response = 1;
		}catch(Exception e){
			log.info("error in adding leave transaction"+e);
			return response;
		}
		return response;
	}
	
	public int deductLeaves(JSONObject jsonResponseObject) {
		log.info("inside deduct leaves");
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
			//log.info(employee.toString());
			
			if(typeOfLeave.equals("PL")){
				log.info("inside PL");
				int leaves = Integer.parseInt(employee.getProperty("PrivilegedLeave").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("PrivilegedLeave", leaves);
			}
			if(typeOfLeave.equals("OH")){
				log.info("inside OH");
				int leaves = Integer.parseInt(employee.getProperty("OptionalHoliday").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("OptionalHoliday", leaves);
			}
			if(typeOfLeave.equals("OL")){
				log.info("inside OL");
				int leaves = Integer.parseInt(employee.getProperty("OptionalLeave").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("OptionalLeave", leaves);
			}
			if(typeOfLeave.equals("CompOff")){
				log.info("inside CompOff");
				int leaves = Integer.parseInt(employee.getProperty("CompensatoryOff").toString());
				leaves = leaves - noOfDays;
				employee.setProperty("CompensatoryOff", leaves);
			}
			//log.info(employee.toString());
			datastore.put(employee);
			response = 1;
		}catch(Exception e){
			log.info("error updating employeee"+e);
		}
		return response;
	}

}
