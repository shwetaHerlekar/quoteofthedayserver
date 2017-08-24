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
	
	

	if(action1.equals("calculate_bill")){
		String coke = String.valueOf(parameters.get("coke"));
		int cokecnt = Integer.parseInt(String.valueOf(parameters.get("cokecount")));
		String pizza = String.valueOf(parameters.get("pizza"));
		int pizzacnt = Integer.parseInt(String.valueOf(parameters.get("pizzaCount")));
	
		int bill = calculateBill(pizza,pizzacnt,coke,cokecnt);
		PrintWriter out = resp.getWriter();
		JSONObject obj = new JSONObject();
		obj.put("displayText", "Your bill is "+String.valueOf(bill)+"Rupees. Thanks for visiting us.");
		obj.put("speech", "Your bill is "+String.valueOf(bill)+"Rupees. Thanks for visiting us.");
		out.println(obj);
	}
	else if(action1.equals("calculate_pizza_bill")){
		
		String pizza = String.valueOf(parameters.get("pizza"));
		int pizzacnt = Integer.parseInt(String.valueOf(parameters.get("pizzaCount")));
	
		int bill = calculatePizzaBill(pizza,pizzacnt);
		PrintWriter out = resp.getWriter();
		JSONObject obj = new JSONObject();
		obj.put("displayText", "Your bill is "+String.valueOf(bill)+"Rupees. Thanks for visiting us.");
		obj.put("speech", "Your bill is "+String.valueOf(bill)+"Rupees. Thanks for visiting us.");
		out.println(obj);
	}
	
	
	}
	catch(Exception e){
	}
    
  }
  
  public int calculateBill(String pizza, int cnt1, String coke, int cnt2)
  {
	String[] pizzas = {"Austrelian", "Autumn", "cheese", "Peppy Panir"};
	int[] cost1 = {200, 100, 300, 230};
	
	String[] cokes = {"Coca cola", "Mirinda"};
	int[] cost2 = {30,50};

	int price1=0,price2=0;
	for(int i=0; i < pizzas.length; i++){
		if(pizza.equalsIgnoreCase(pizzas[i]))
		{
			price1 = cost1[i];
		}
	}
	
	for(int i=0; i < cokes.length; i++){
		if(coke.equalsIgnoreCase(cokes[i]))
		{
			price2 = cost2[i];
		}
	}
	
	return price1*cnt1+price2*cnt2;
	
  }
  
  public int calculatePizzaBill(String pizza, int cnt1)
  {
	String[] pizzas = {"Austrelian", "Autumn", "cheese", "Peppy Panir"};
	int[] cost1 = {200, 100, 300, 230};
	
	
	int price1=0;
	for(int i=0; i < pizzas.length; i++){
		if(pizza.equalsIgnoreCase(pizzas[i]))
		{
			price1 = cost1[i];
		}
	}
	
	return price1*cnt1;
	
  }

}