package org.usfirst.frc.team3044.Reference;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utilities {

	public static boolean tolerance(double lowerValue, double actualValue, double upperValue){
		return (lowerValue < actualValue) && (actualValue < upperValue); 
	}
	
	public static double deadband (double Value, double Tolerance){
		if (-Tolerance < Value && Value < Tolerance){
			return 0; 
		} else {
			return Value;
		}
	}
	
	public static String getStackTraceString(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
