package com.example;

import java.io.*;
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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


// [START example]
@SuppressWarnings("serial")
public class MyServiceServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(MyServiceServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("inside get method");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter p = new FilterPredicate("EmpID", FilterOperator.EQUAL, "18831");
		Query q = new Query("PersistentEmployees").setFilter(p);
		PreparedQuery pq = datastore.prepare(q);
		Entity result = pq.asSingleEntity();
		log.info(result.toString());
	}
}