package org.usfirst.frc.team3044.Diagnostics;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;
import org.nanohttpd.protocols.http.response.Status;
import org.usfirst.frc.team3044.Reference.Outputs;
import com.ctre.CANTalon;

public class Talon {
	
	// org.usfirst.frc.team3044.Diagnostics.Talon.Set 
	public DiagnosticsServerDispatchResponse Set(Object o, org.nanohttpd.protocols.http.request.Method method)
	{
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
		catch (Exception ex)
		{
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(ex, Status.INTERNAL_ERROR, ex.getMessage());
		}
	}
}
