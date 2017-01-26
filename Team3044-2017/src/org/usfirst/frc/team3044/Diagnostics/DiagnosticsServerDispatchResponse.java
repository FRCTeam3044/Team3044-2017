package org.usfirst.frc.team3044.Diagnostics;

import org.json.simple.JSONObject;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Status;
import org.usfirst.frc.team3044.Reference.Utilities;

public class DiagnosticsServerDispatchResponse {
	public IStatus Status;
	public Object ResponseData; 
	public String StatusMessage; 
	
	public static DiagnosticsServerDispatchResponse BuildExceptionResponse(Exception e)
	{
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse();
		JSONObject er = new JSONObject(); 
		er.put("Success", false);
		er.put("ExceptionMessage", e.getMessage());
		er.put("StackTrace", Utilities.getStackTraceString(e));
		r.Status=org.nanohttpd.protocols.http.response.Status.INTERNAL_ERROR; 
		r.ResponseData = er; 
		return r; 
	}
}
