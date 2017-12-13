package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Servlet implementation class Test1
 */
public class Test1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(Test.class.getName());   
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("inside get test1");
		String userName = "shweta_herlekar";
		String accessToken = "z7l0lL8vOU9epMTw1meQMAw8tyaX";
		JSONObject obj = getResponseFromLeaveTypeAPI(accessToken, userName);
		
		///leaveTypeCid need to be set based on leave type
		int leaveTypeCid = 1;
		JSONObject obj1 = getResponseFromLeaveInfoAPI(accessToken, userName, leaveTypeCid);
	}
	
	
	static JSONObject getResponseFromLeaveTypeAPI(String accessToken,String userName){
		JSONObject responseData=null;
		try{
			log.info("inside getting response of api for type");
			String apiurl = "https://api.persistent.com:9020/hr/leavetypes/"+userName;
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
			JSONArray leaveTypes = (JSONArray) parser.parse(output.toString());
			conn.disconnect();
			log.info(leaveTypes.toString());
		}catch(Exception e){
			log.info("error accessing leave balance :"+e);
		}

		return responseData;
	}

	static JSONObject getResponseFromLeaveInfoAPI(String accessToken,String userName, int leaveTypeCid){
		JSONObject responseData=null;
		try{
			log.info("inside getting response of api for leave balance");
			String apiurl = "https://api.persistent.com:9020/hr/leaveinfo/"+userName+"/"+leaveTypeCid;
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
			JSONArray leaveInfo= (JSONArray) parser.parse(output.toString());
			conn.disconnect();
			log.info(leaveInfo.toString());
		}catch(Exception e){
			log.info("error accessing leave balance :"+e);
		}

		return responseData;
	}
}
