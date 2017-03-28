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

package org.usfirst.frc.team3044.Diagnostics;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Server
{
	// org.usfirst.frc.team3044.Diagnostics.Server/Ping 
	public DiagnosticsServerDispatchResponse Ping(Object o, org.nanohttpd.protocols.http.request.Method method)
	{
		if (o==null ) o = new JSONObject(); 
		
		JSONObject arg = (JSONObject)o; 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		arg.put("RequestMethod", method.toString());
		arg.put("ResponseType","PingReply");
		arg.put("TimeStamp", sdf.format(new Date()));
		arg.put("Success", true); 
		
		DiagnosticsServerDispatchResponse ret = new DiagnosticsServerDispatchResponse(); 
		ret.Status = Status.OK;
		ret.ResponseData = arg; 
		return ret; 
	}
	
	// org.usfirst.frc.team3044.Diagnostics.Server/PutSmartDashboardString 
	public DiagnosticsServerDispatchResponse PutSmartDashboardString(Object o, Method method)
	{
		JSONObject arg = (JSONObject)o; 
		
		DiagnosticsServerDispatchResponse ret = new DiagnosticsServerDispatchResponse(); 
		try {
		if (Method.PUT.equals(method) || Method.POST.equals(method))
		{
			String sdSlot = (String)arg.get("DashboardSlot");
			String sdData = (String)arg.get("Data"); 
			SmartDashboard.putString(sdSlot,sdData); 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			arg.put("ResponseType","PutSmartDashboardStringReply");
			arg.put("TimeStamp", sdf.format(new Date()));
			arg.put("Success", true); 
			ret.Status = Status.OK; 
			ret.ResponseData = arg; 
			return ret ; 
		}
		else
		{
			ret.Status = Status.METHOD_NOT_ALLOWED; 
			ret.ResponseData = "HTTP method not valid for this operation"; 
			return ret; 
		}} 
		catch (Exception e)
		{
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(e, Status.INTERNAL_ERROR, e.getMessage());
			
		}
	}
}