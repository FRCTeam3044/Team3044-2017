package org.usfirst.frc.team3044.Diagnostics;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Status;
import org.usfirst.frc.team3044.Reference.Outputs;
import com.ctre.CANTalon;

public class Talon {
	
	// org.usfirst.frc.team3044.Diagnostics.Talon/OutputVoltage
	public DiagnosticsServerDispatchResponse OutputVoltage(Object o,org.nanohttpd.protocols.http.request.Method method )
	{
		if (method!= Method.GET)
		{
			return DiagnosticsServerDispatchResponse.BuildErrorResponse(Status.METHOD_NOT_ALLOWED, "Method not supported on this resource"); 
		}
		
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse(); 
		JSONObject rd = new JSONObject();
		rd.put("ResponseType", "OutputVoltageReply");
		rd.put("Timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())); 
		try
		{
		
			int canTalonID = 0; 
			JSONObject params = (JSONObject) o ;
			
			if (params.get("CANTalonID") != null) { canTalonID = Integer.parseInt(params.get("CANTalonID").toString()); }
			else { throw new Exception("CANTalonID is a required parameter"); }
			
			com.ctre.CANTalon ct = new com.ctre.CANTalon(canTalonID) ; 
			double outputVoltage = ct.getOutputVoltage(); 
			rd.put("OutputCurrent", outputVoltage); 
			rd.put("Success", true); 
			r.Status = Status.OK; 
			r.ResponseData = rd; 
			return r; 
		}
		catch (Exception e)
		{
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(e, Status.INTERNAL_ERROR, e.getMessage());
		}
	}
	
	// org.usfirst.frc.team3044.Diagnostics.Talon/OutputCurrent 
	public DiagnosticsServerDispatchResponse OutputCurrent(Object o,org.nanohttpd.protocols.http.request.Method method )
	{
		if (method!= Method.GET)
		{
			return DiagnosticsServerDispatchResponse.BuildErrorResponse(Status.METHOD_NOT_ALLOWED, "Method not supported on this resource"); 
		}
		
		
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse(); 
		JSONObject rd = new JSONObject();
		rd.put("ResponseType", "OutputCurrentReply");
		rd.put("Timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())); 
		try
		{
			int canTalonID = 0; 
			JSONObject params = (JSONObject) o ;
			
			if (params.get("CANTalonID") != null) { canTalonID = Integer.parseInt(params.get("CANTalonID").toString()); }
			else { throw new Exception("CANTalonID is a required parameter"); }
			
			com.ctre.CANTalon ct = new com.ctre.CANTalon(canTalonID) ; 
			double outputCurrent = ct.getOutputCurrent(); 
			rd.put("OutputCurrent", outputCurrent); 
			rd.put("Success", true); 
			r.Status = Status.OK; 
			r.ResponseData = rd; 
			return r; 
		}
		catch (Exception e)
		{
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(e, Status.INTERNAL_ERROR, e.getMessage());
		}
	}
	
	
	// org.usfirst.frc.team3044.Diagnostics.Talon.Set 
	public DiagnosticsServerDispatchResponse Set(Object o, org.nanohttpd.protocols.http.request.Method method)
	{
		if (method!= Method.POST)
		{
			return DiagnosticsServerDispatchResponse.BuildErrorResponse(Status.METHOD_NOT_ALLOWED, "Method not supported on this resource"); 
		}
		
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse(); 
		JSONObject rd = new JSONObject();
		rd.put("ResponseType", "SetPowerReply");
		rd.put("Timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())); 
		try
		{
			// CANTalonID, output power 
			int canTalonID = 0; 
			double outputValue = 0; 
			
			JSONObject params = (JSONObject) o ;
			
			if (params.get("CANTalonID") != null) { canTalonID = Integer.parseInt(params.get("CANTalonID").toString()); }
			else { throw new Exception("CANTalonID is a required parameter"); }
			
			if (params.get("OutputValue") != null) { outputValue = Double.parseDouble(params.get("OutputValue").toString()); }
			else { throw new Exception("OutputValue is a required parameter"); }
			
			com.ctre.CANTalon ct = new com.ctre.CANTalon(canTalonID) ; 
		
			ct.set(outputValue);
			
			rd.put("Success", true); 
			r.Status = Status.OK; 
			r.ResponseData = rd; 
			return r; 
		}
		catch (Exception e)
		{
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(e, Status.INTERNAL_ERROR, e.getMessage());
		}
	}
}
