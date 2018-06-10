package com.example;

import java.io.*;
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
			
			log.info("inner inside\n");
			
			String quote = readFile("quotes.txt");
			resp.getWriter().write(quote);
			
		}catch(Exception e)
		{
			log.info("exception in getting quotes"+e);
		}
		
	}
	
	public String readFile(String fileName){
		log.info("bdjkbsjdkndlsvnldsvvdlkvmls");
		StringBuilder result = new StringBuilder("");
		String quote = new String("");
		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		log.info("/////////"+file);
		
		Boolean isFirstLine = true;
		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				if(isFirstLine){
					quote = scanner.nextLine();
					log.info(quote);
					isFirstLine = false;
				}
				else{
					String line = scanner.nextLine();
					log.info(line);
					result.append(line).append("\n");
				}
			}

			scanner.close();
			result.append(quote).append("\n");
			writeToFile(fileName, result.toString());

		} catch (IOException e) {
			log.info("exception in getting quotes"+e);
		}
			
		return quote;

	  }
	
	public void writeToFile(String fileName,String data){
		log.info("dataaaaa:"+data);
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		try {
			FileWriter fr = new FileWriter(file);
			BufferedWriter br = new BufferedWriter(fr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("exception in getting quotes"+e);
		}
	}
	
}