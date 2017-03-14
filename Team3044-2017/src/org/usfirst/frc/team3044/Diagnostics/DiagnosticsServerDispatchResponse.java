package org.usfirst.frc.team3044.Diagnostics;

import org.json.simple.JSONObject;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;
import org.usfirst.frc.team3044.Reference.Utilities;

public class DiagnosticsServerDispatchResponse {
	public IStatus Status;
	public Object ResponseData; 
	public String StatusMessage; 
	
	public static DiagnosticsServerDispatchResponse BuildErrorResponse( org.nanohttpd.protocols.http.response.Status status,String reasonPhrase)
	{
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse();
		JSONObject er = new JSONObject(); 
		er.put("Success", false);
		r.Status=status; 
		r.ResponseData = er; 
		r.StatusMessage = reasonPhrase; 
		return r; 
		
		
	}
	
	public static DiagnosticsServerDispatchResponse BuildExceptionResponse(Exception e, org.nanohttpd.protocols.http.response.Status status,
			String reasonPhrase)
	{
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse();
		JSONObject er = new JSONObject(); 
		er.put("Success", false);
		er.put("ExceptionMessage", e.getMessage());
		er.put("StackTrace", Utilities.getStackTraceString(e));
		r.Status=status; 
		r.ResponseData = er; 
		r.StatusMessage = reasonPhrase; 
		return r; 
	}
}
