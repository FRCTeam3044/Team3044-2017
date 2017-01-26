package org.usfirst.frc.team3044.Reference;

import java.lang.reflect.InvocationTargetException;
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

// import org.nanohttpd.protocols.http.response.Response;

public class RobotHttpServer extends org.nanohttpd.protocols.http.NanoHTTPD {

	RobotHttpServer( int port) {
		super(5800);
	}
	
	public RobotHttpServer()
	{
		super(5800);
	}
	
	@Override public Response serve(org.nanohttpd.protocols.http.IHTTPSession session)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		Method method = session.getMethod(); 
		
		if (Method.PUT.equals(method) || Method.POST.equals(method)) {
	        try {
	            session.parseBody(map);
	        } 
	        catch (Exception e)
	        {
	        	System.out.println ("Exception thrown while parsing request");
	        	System.out.println(e.getMessage());
	        	System.out.println(e.getStackTrace());
	        }
		}
		String json = map.get("postData");

		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(json); 
			
			JSONObject jsonObject = (JSONObject) obj;
			
			String requestUri = session.getUri(); 
			
			IStatus status = Status.NOT_IMPLEMENTED; 
			
			RobotHttpServerDispatchResponse result = Dispatch (requestUri, jsonObject, status);
			
			if (result.Status==Status.OK) { 
			return Response.newFixedLengthResponse(result.Status, "application/json", ((JSONObject)result.ResponseData).toJSONString());
			}
			else
			{
				return Response.newFixedLengthResponse(result.Status, "application/text", (String)result.StatusMessage);
			}
			
			
		}
		catch (Exception e)
		{
			return Response.newFixedLengthResponse(Status.INTERNAL_ERROR, "application/json",e.getMessage()); 
		}

		
		
	}
	
	
	// This will dynamically invoke the target method 
	RobotHttpServerDispatchResponse Dispatch(String target, JSONObject arguments, IStatus status)
	{
		// URI format will be 
		// /Package.Class.Name/methodName
		
		RobotHttpServerDispatchResponse response = new RobotHttpServerDispatchResponse();
		
		String[] pathSegments = target.split("/"); 
		
		if (pathSegments.length==3)
		{
			String className= pathSegments[1];
			String methodName = pathSegments[2]; 
			try {
		
				
				Class c = Class.forName(className);
				
				Object o = c.newInstance();
				
				java.lang.reflect.Method m = 
						c.getMethod(methodName, new Class[] {JSONObject.class});
			
				Object r = m.invoke(o, arguments); 
				
				
				response.Status= Status.OK; 
				response.ResponseData = r;
				return response; 
				
			} catch (ClassNotFoundException e) {
				response.Status =  Status.NOT_FOUND; 
				response.StatusMessage = "Class " + pathSegments[1] + " not found"; 
			} catch (NoSuchMethodException e) {
				response.Status=Status.NOT_FOUND; 
				response.StatusMessage= "Method " + pathSegments[2] + " in " + pathSegments[1] + "  not found";
			} catch (SecurityException e) {
				response.Status=Status.FORBIDDEN;
				response.StatusMessage= "Security exception thrown while processing request";
			} catch (InstantiationException e) {
				response.Status=Status.INTERNAL_ERROR;
				response.StatusMessage= "Instantiation exception";
			} catch (IllegalAccessException e) {
				response.Status=Status.INTERNAL_ERROR; 
				response.StatusMessage= "Illegal access exception";
			} catch (IllegalArgumentException e) {
				response.Status=Status.BAD_REQUEST;
				response.StatusMessage= "Illegal argument exception"; 
			} catch (InvocationTargetException e) {
				response.Status=Status.INTERNAL_ERROR;
				response.StatusMessage= "Invocation exception";
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


