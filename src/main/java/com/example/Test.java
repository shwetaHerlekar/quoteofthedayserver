package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.repackaged.org.apache.commons.httpclient.util.HttpURLConnection;

/**
 * Servlet implementation class Test
 */
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(Test.class.getName());   
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("inside Test get");
		String accessToken = "60OrOwPAuvDhrGBjCy33qTuqzt1i";
		String apiurl = "https://api.persistent.com:9020/hr/leaveattendanceself";
		URL url = new URL(apiurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Length", "0");
		conn.setRequestProperty("Authorization", "Bearer "+accessToken);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		
		BufferedReader bufferedReaderObject = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));			
		StringBuilder output = new StringBuilder();			
		
		String op;
		while ((op = bufferedReaderObject.readLine()) != null) {
			output.append(op);
		}
		
		JSONParser parser = new JSONParser();
		JSONObject responseData=null;
		try {
			responseData = (JSONObject) parser.parse(output.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		conn.disconnect();
		log.info(responseData.toString());
		response.getWriter().write(responseData.toJSONString());
	}


}
