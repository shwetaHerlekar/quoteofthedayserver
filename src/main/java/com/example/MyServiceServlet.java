package com.example;

import java.io.*;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


// [START example]
@SuppressWarnings("serial")
public class MyServiceServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(MyServiceServlet.class.getName());

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("inside get method");
		//resp.setContentType("application/json");
		//JSONObject resObj = new JSONObject();
		try{
			
			String a = "Hello quote world!!";
			resp.getWriter().write(a);
			
		}catch(Exception e)
		{
			log.info("exception in getting employee"+e);
		}
		
	}
	
}