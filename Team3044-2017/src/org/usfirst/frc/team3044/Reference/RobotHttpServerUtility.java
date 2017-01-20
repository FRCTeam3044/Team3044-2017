/*
 * 	RobotHttpServerUtility.java 
 * 	
 * 	Provides server side utilities for the HTTP server. 
 * 
 * Methods in this file are should be callable by HTTP clients 
 * 
 * 	Any method that returns Object and takes a JSONObject as a parameter can be called 
 * 	by the server. The URL for the Ping method will be 
 * 
 * 	/org.usfirst.frc.team3044.Reference.RobotHttpServerUtility/Ping
 * 	
 * 	Which is /[Class Name]/[Method Name] 
 * 
 * 	Anything the method needs to work should be provided in the JSONObject.
 * 
 * 	If the method needs to return data, it should only return a JSONObject. 
 * 
 * */

package org.usfirst.frc.team3044.Reference;

import java.util.Date;

import org.json.simple.JSONObject;

public class RobotHttpServerUtility
{
	public Object Ping(JSONObject arg)
	{
		JSONObject r = new JSONObject();
		
		r.put("ResponseType","PingReply");
		r.put("TimeStamp", new Date().toString());
		
		return r; 
	}
}