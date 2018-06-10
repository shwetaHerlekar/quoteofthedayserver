package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
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
			
			Date today = new Date();
			int index = today.getDate();
			log.info("index :"+index);
			String quote = readQuote("quotes.txt",index);
			resp.getWriter().write(quote);
			
		}catch(Exception e)
		{
			log.info("exception in getting quotes"+e);
		}
		
	}
	
	public String readQuote(String fileName,int index){
		
		ArrayList<String> quotes = new ArrayList<>(); 
		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		
		
		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				quotes.add(line);
			}

			scanner.close();
			log.info("quotes:"+quotes);

		} catch (IOException e) {
			log.info("exception in getting quotes"+e);
		}
			
		return quotes.get(index);
	}
	
}