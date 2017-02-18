package org.usfirst.frc.team3044.Diagnostics;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;
import org.nanohttpd.protocols.http.response.Status;
import org.usfirst.frc.team3044.Reference.Outputs;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Power {
	
	// org.usfirst.frc.team3044.Diagnostics.Power.PDPInfo 
	public DiagnosticsServerDispatchResponse PDPInfo (Object o, org.nanohttpd.protocols.http.request.Method method )
	{
		PowerDistributionPanel pdp = new PowerDistributionPanel();
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse(); 
		JSONObject rd = new JSONObject();
		rd.put("ResponseType", "OutputCurrentReply");
		rd.put("Timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())); 
		try 
		{
			rd.put("leftFrontDrive output current", Outputs.getInstance().leftFrontDrive.getOutputCurrent());
			rd.put("rightFrontDrive output current", Outputs.getInstance().rightFrontDrive.getOutputCurrent());
			rd.put("leftBackDrive output current", Outputs.getInstance().leftBackDrive.getOutputCurrent());
			rd.put("rightBackDrive output current", Outputs.getInstance().rightBackDrive.getOutputCurrent());
			
			for (int i=0;i<16;i++)
			{
				rd.put("PDP channel current" + i, pdp.getCurrent(i)); 
			}
			
			rd.put("PDP voltage", pdp.getVoltage()); 
			
			rd.put("PDP Total Current", pdp.getTotalCurrent()); 
			rd.put("PDP Total Energy", pdp.getTotalEnergy()); 
			rd.put("PDP Total Power", pdp.getTotalPower()); 
			
		} 
		catch (Exception e)
		{
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(e,Status.INTERNAL_ERROR,e.getMessage()); 
		}
		r.Status = Status.OK; 
		r.ResponseData= rd; 
		return r; 
	}
}
