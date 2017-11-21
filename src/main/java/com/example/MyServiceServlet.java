package com.example;

import java.io.*;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// [START example]
@SuppressWarnings("serial")
public class MyServiceServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(MyServiceServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("inside get method");
		resp.getWriter().write("Inside Get");
		
	}
}