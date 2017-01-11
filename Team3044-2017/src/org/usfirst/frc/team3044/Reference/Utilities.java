package org.usfirst.frc.team3044.Reference;

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
}
