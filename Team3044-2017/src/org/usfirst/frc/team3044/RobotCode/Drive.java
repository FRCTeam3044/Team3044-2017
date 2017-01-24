package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	FirstController controller = FirstController.getInstance();

	double leftAutoSpeed;
	double rightAutoSpeed;
	double leftDriveSpeed;
	double rightDriveSpeed;
	
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;

	private Components comp = Components.getInstance();

	public double deadband(double value){
		if(Math.abs(value) < .1){
			return 0;
		}else{
			return value;
		}
	}
	
	public void driveInit() {
		controller.getInstance();
		leftFrontDrive = comp.leftFrontDrive;
		leftBackDrive = comp.leftBackDrive;
		rightFrontDrive = comp.rightFrontDrive;
		rightBackDrive = comp.rightBackDrive;

		leftFrontDrive.set(0);
		leftBackDrive.set(0);
		rightFrontDrive.set(0);
		rightBackDrive.set(0);

		leftFrontDrive.enableBrakeMode(true);
		rightFrontDrive.enableBrakeMode(true);
		leftBackDrive.enableBrakeMode(true);
		rightBackDrive.enableBrakeMode(true);
		
	}

	public void driveAutoPeriodic() {
		leftAutoSpeed = CommonArea.leftAutoSpeed;
		rightAutoSpeed = -CommonArea.rightAutoSpeed;
	}

	public void driveTeleopPeriodic() {

		double x = -deadband(controller.getLeftX());
		double y = deadband(controller.getLeftY());
		double r = deadband(controller.getRightX());
		
		double v_FrontLeft = r-y-x;
		double v_FrontRight = r+y-x;
		double v_BackLeft = r-y+x;
		double v_BackRight = r+y+x;
		
		double f = 1;
		if(Math.abs(v_FrontLeft) > f) f = v_FrontLeft;
		if(Math.abs(v_FrontRight) > f) f = v_FrontRight;
		if(Math.abs(v_BackLeft) > f) f = v_BackLeft;
		if(Math.abs(v_BackRight) > f) f = v_BackRight;
		
		leftFrontDrive.set(v_FrontLeft / f);
		rightFrontDrive.set(v_FrontRight / f);
		leftBackDrive.set(v_BackLeft / f);
		rightBackDrive.set(v_BackRight / f);
		
		SmartDashboard.putString("DB/String 5", String.valueOf("Left Front " + Components.getInstance().leftFrontDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 6", String.valueOf("Right Front " + Components.getInstance().rightFrontDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 7", String.valueOf("Left Back " + Components.getInstance().leftBackDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 8", String.valueOf("Right Back " + Components.getInstance().rightBackDrive.getOutputCurrent()));
	}

	public void testPeriodic() {
	}
	
	public void testInit(){
	}
	}
