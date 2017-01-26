package org.usfirst.frc.team3044.Diagnostics;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// NanoHTTPD
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
// JSON-simple 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.usfirst.frc.team3044.Diagnostics.*;

public class DiagnosticsServer extends org.nanohttpd.protocols.http.NanoHTTPD {

	public static int TCP_PORT = 5800; 
	public static Map<String, Object> Cache; 
	
	DiagnosticsServer( int port) {
		super(port);
		Cache= Collections.EMPTY_MAP;  
	}
	
	public DiagnosticsServer()
	{
		super(TCP_PORT);
		Cache= Collections.EMPTY_MAP; 
	}
	
	@Override public Response serve(org.nanohttpd.protocols.http.IHTTPSession session)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		Method method = session.getMethod(); 
		
		Object obj = null; 
		
		if (Method.PUT.equals(method) || Method.POST.equals(method)) {
	        try {
	            session.parseBody(map);
	            String json = map.get("postData");
	            JSONParser parser = new JSONParser();
	        	obj = parser.parse(json); 
	  	        } 
	        catch (Exception e)
	        {
	        	System.out.println ("Exception thrown while parsing request");
	        	System.out.println(e.getMessage());
	        	System.out.println(e.getStackTrace());
	        }
		}
		
		try {
			
			String requestUri = session.getUri(); 
			
			IStatus status = Status.NOT_IMPLEMENTED; 
			
			DiagnosticsServerDispatchResponse result = Dispatch (requestUri, obj, status,method);
			
			if (result.ResponseData.getClass().equals(JSONObject.class) )
			{
				return Response.newFixedLengthResponse(result.Status, "application/json", ((JSONObject)result.ResponseData).toJSONString()); 
			}
			
			if (result.ResponseData.getClass().equals(String.class))
			{
				return Response.newFixedLengthResponse(result.Status, "application/text", (String)result.StatusMessage);
			}
			
			return Response.newFixedLengthResponse(Status.OK, "application/unknown", result.toString());
		}
		catch (Exception e)
		{
			return Response.newFixedLengthResponse(Status.INTERNAL_ERROR, "application/json",e.getMessage()); 
		}
	}
	
	
	// This will dynamically invoke the target method 
	DiagnosticsServerDispatchResponse Dispatch(String target, Object arguments, IStatus status, Method httpMethod)
	{
		DiagnosticsServerDispatchResponse response = new DiagnosticsServerDispatchResponse();
		
		String[] pathSegments = target.split("/"); 
		
		if (pathSegments.length==3)
		{
			String className= pathSegments[1];
			String methodName = pathSegments[2]; 
			try {
		
				
				Class c = Class.forName(className);
				
				Object o = c.newInstance();
				
				java.lang.reflect.Method m = 
						c.getMethod(methodName, new Class[] {Object.class, org.nanohttpd.protocols.http.request.Method.class});
			
				Object r = m.invoke(o, arguments, httpMethod); 
				
				// TODO: Allow invoke to set HTTP Status and status message 
				return (DiagnosticsServerDispatchResponse) r; 
				
			} catch (ClassNotFoundException e) {
				response.Status =  Status.NOT_FOUND; 
				response.StatusMessage = "Class " + pathSegments[1] + " not found"; 
				response.ResponseData = DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
			} catch (NoSuchMethodException e) {
				response.Status=Status.NOT_FOUND; 
				response.StatusMessage= "Method " + pathSegments[2] + " in " + pathSegments[1] + "  not found";
				response.ResponseData = DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
			} catch (SecurityException e) {
				response.Status=Status.FORBIDDEN;
				response.StatusMessage= "Security exception thrown while processing request";
				response.ResponseData = DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
			} catch (InstantiationException e) {
				response.Status=Status.INTERNAL_ERROR;
				response.StatusMessage= "Instantiation exception";
				response.ResponseData = DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
			} catch (IllegalAccessException e) {
				response.Status=Status.INTERNAL_ERROR; 
				response.StatusMessage= "Illegal access exception";
				response.ResponseData = DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
			} catch (IllegalArgumentException e) {
				response.Status=Status.BAD_REQUEST;
				response.StatusMessage= "Illegal argument exception"; 
				response.ResponseData = DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
			} catch (InvocationTargetException e) {
				response.Status=Status.INTERNAL_ERROR;
				response.StatusMessage= "Invocation exception: " + e.getStackTrace().toString();
				response.ResponseData = DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
			}
			return response; 
		}
		else
		{
			response.Status=Status.BAD_REQUEST;
			response.StatusMessage="Invalid request"; 
			return response; 
		}
		
	}
	
	
}


