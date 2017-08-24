package com.example;

import java.io.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ai.api.AIServiceException;
import ai.api.model.AIResponse;
import ai.api.web.AIServiceServlet;

import org.json.simple.*;
import org.mortbay.log.Log;

import sun.util.logging.resources.logging;


// [START example]
@SuppressWarnings("serial")
public class MyServiceServlet extends AIServiceServlet {

  @SuppressWarnings("unchecked")
@Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
	
	String sessionId = req.getParameter("sessionId");
	try{
	
		AIResponse aiResponse = request(req.getParameter("message"), sessionId);
		
		resp.setContentType("application/json");
		
		JSONObject obj = new JSONObject();
		obj.put("displayText", aiResponse.getResult().getFulfillment().getSpeech());
		obj.put("speech", aiResponse.getResult().getFulfillment().getSpeech());
		
		Log.info(aiResponse.getResult().getFulfillment().getDisplayText());
		
		PrintWriter out = resp.getWriter();
		out.print(obj);
	
		if(aiResponse.getResult().getFulfillment().getDisplayText()!=null)
		{
			obj.put("displayText", aiResponse.getResult().getFulfillment().getDisplayText());
		}
			
		//resp.getWriter().append(aiResponse.getResult().getFulfillment().getSpeech());
	}
	
	catch(AIServiceException e){
	
	}
  }
}