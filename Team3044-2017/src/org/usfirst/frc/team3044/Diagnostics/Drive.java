package org.usfirst.frc.team3044.Diagnostics;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Status;
import org.usfirst.frc.team3044.Reference.Outputs;
import org.usfirst.frc.team3044.Reference.FirstController;
import org.usfirst.frc.team3044.Reference.Utilities;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Drive extends org.usfirst.frc.team3044.RobotCode.Drive {
	
	PowerDistributionPanel pdp;
	
	public Drive() {
		super();
	} 
	
	public DiagnosticsServerDispatchResponse OutputCurrent (Object o, org.nanohttpd.protocols.http.request.Method method )
	{
		pdp = new PowerDistributionPanel();
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
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
		}
		r.Status = Status.OK; 
		r.ResponseData= rd; 
		return r; 
	}
	
	public DiagnosticsServerDispatchResponse Init(Object o , org.nanohttpd.protocols.http.request.Method method)
	{
		DiagnosticsServerDispatchResponse r = new DiagnosticsServerDispatchResponse();
		try { 
			FirstController controller = FirstController.getInstance();
			if (Method.PUT.equals(method) || Method.POST.equals(method))
			{
				driveInit(); 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				r.Status = Status.OK; 
				JSONObject rd = new JSONObject(); 
				rd.put("ResponseType", "DriveInitReply");
				rd.put("Timestamp",  sdf.format(new Date())); 
				rd.put("Success", true);
				r.ResponseData = rd; 
				return r; 
			}
			else
			{
				r.Status = Status.METHOD_NOT_ALLOWED; 
				r.StatusMessage = "HTTP Method not valid for this operation";
				return r; 
			} 
		}
		catch (Exception e)
		{
			return DiagnosticsServerDispatchResponse.BuildExceptionResponse(e); 
		}
	}
}
