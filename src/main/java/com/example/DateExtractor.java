package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class DateExtractor
 */
public class DateExtractor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String[] reg = {"[0-9]{1,2}[a-zA-Z]{3}[0-9]{2}", "[0-9]{1,2}[a-zA-Z]{3} [0-9]{2}","[0-9]{1,2}[a-zA-Z]{3}[0-9]{4}", "[0-9]{1,2} [a-zA-Z]{3} [0-9]{2}", "[0-9]{1,2} [a-zA-Z]{3} [0-9]{4}"};
       
	private static final Logger log = Logger.getLogger(HolidayServlet.class.getName());   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		log.info("Inside post of date extractor");
		JSONObject responseObj=new JSONObject();
		try{
			
			//load post parameter
			String responseJson = ReaderUtil.readPostParameter(request);
			JSONObject jsonResponseObject = new JSONObject(responseJson);
			//log.info(jsonResponseObject.toString());
			
			//Perform ocr on base64 image
			String imgBase64 = jsonResponseObject.getString("imgBase64");
			String imgText = performOCR(imgBase64);
			
			//extract date
			log.info("here.......");
			String extractedDate = getDate(imgText);
			
			if(extractedDate==null){
				responseObj.put("code", "200");
				responseObj.put("msg", "Success");
				responseObj.put("date", extractedDate);
			}
			else{
				responseObj.put("code", "400");
				responseObj.put("msg", "No date found");
			}
		}catch(Exception e){
			log.info("error extracting date"+e.getMessage());
		}
	}
	
	public static String performOCR(String base64String){
        
        String apiUrl = "https://vision.googleapis.com/v1/images:annotate?";
        String apiKey = "key=AIzaSyC3IyBtatcC3pWGseGIfS3kfxqSpnaUUNA";
        String descriptionStr=null;
        if(base64String.isEmpty())
            base64String = "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";
        JSONObject result = new JSONObject();
        
        try{   
               
               URL url = new URL(apiUrl+apiKey);
               
               System.setProperty("https.proxyHost", "hjproxy.persistent.co.in");
               System.setProperty("https.proxyPort", "8080");
               
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setDoOutput(true);
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Content-Type", "application/json");
 
               String strBody="{ \"requests\":[ { \"image\":{ \"content\":\""+base64String+"\" }, \"features\":[ { \"type\":\"DOCUMENT_TEXT_DETECTION\", \"maxResults\":1000 } ] } ] }";
               
               OutputStream os = conn.getOutputStream();
               os.write(strBody.getBytes());
               os.flush();
               
               if (conn.getResponseCode() != 200) {
                     throw new RuntimeException("Failed : HTTP error code : "
                                   + conn.getResponseCode());
               }             
               
               BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
               
               String output = "";
               String op;
               while ((op = br.readLine()) != null) {
                     output = output.concat(op);
                     //System.out.println(output);
               }

               result.put("body", output);
               conn.disconnect();
        }
        catch(Exception e){
               System.err.print("inside error in vision catch"+e);
        }

        try {
				JSONObject body = new JSONObject(result.get("body"));
				String bodytring=result.getString("body");
		    	//System.out.println(bodytring);
		    	JSONObject bodyObject=new JSONObject(bodytring);
		    	JSONArray responsesArray=(JSONArray) bodyObject.getJSONArray("responses");
		        JSONObject textAnnotaionsDict=responsesArray.getJSONObject(0);
		        JSONArray textAnnotationArray=(JSONArray)textAnnotaionsDict.getJSONArray("textAnnotations");
		        JSONObject firstObj=(JSONObject) textAnnotationArray.get(0);
		        descriptionStr=firstObj.getString("description");
		        //System.out.println("Description-"+descriptionStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return descriptionStr;
 	}
	
	public static String getDate(String stringToSearch){
    	String match = null;
    	for (String expr : reg) {
    		 Pattern p = Pattern.compile(expr);   // the pattern to search for
    	     Matcher m = p.matcher(stringToSearch);

    	     // if we find a match, get the group 
    	     if (m.find())
    	     {
    	         // we're only looking for one group, so get it
    	    	 match = m.group();
    	         break;
    	      }
    	  
		}
    	return match;
	}

}
