package com.example;

import java.io.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// [START example]
@SuppressWarnings("serial")
public class MyWebhookServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    out.println("Hello, world");
    
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
	try
	{
	resp.setContentType("application/json");
	StringBuilder buffer = new StringBuilder();
    	BufferedReader reader = req.getReader();
    	String line;
    	while ((line = reader.readLine()) != null) {
        	buffer.append(line);
    	}
    String data = buffer.toString();
	JSONParser parser = new JSONParser();
	JSONObject reqJSON = (JSONObject)parser.parse(data);
	JSONObject result = (JSONObject)reqJSON.get("result");
	String action1 = String.valueOf(result.get("action"));
	JSONObject parameters = (JSONObject)result.get("parameters");
	
	PrintWriter out = resp.getWriter();
	JSONObject obj = new JSONObject();
	obj.put("speech", action1);
	obj.put("displayText", action1);
	out.println(obj);
	
	}
	catch(Exception e){
	}
    
  }
  
}