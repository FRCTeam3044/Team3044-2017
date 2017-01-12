package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.CommonArea;
import org.usfirst.frc.team3044.Reference.Components;
import org.usfirst.frc.team3044.Reference.Utilities;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Drive drive = new Drive();
	
	private double Dashboard;

	public void robotInit() {
		Components.getInstance().init();
		
		drive.driveInit();
	}

	public void autonomousInit() {
		//Drive
		CommonArea.leftAutoSpeed = 0;
		CommonArea.rightAutoSpeed = 0;
		CommonArea.leftDesiredEncoderValue = 0;
		CommonArea.rightDesiredEncoderValue = 0;
		CommonArea.movexFeet = false;
		CommonArea.atDistance = false;
	}


	public void autonomousPeriodic() {
		//Null
	}

	public void teleopInit() {
		//Null
	}

	public void teleopPeriodic() {

		CommonArea.CommonPeriodic();
		drive.driveTeleopPeriodic();
		
		SmartDashboard.putNumber("DB/String 5", Components.getInstance().leftFrontDrive.getAnalogInPosition());
		SmartDashboard.putNumber("DB/String 6", Components.getInstance().rightFrontDrive.getAnalogInPosition());
		SmartDashboard.putNumber("DRIVECURRENT", Components.getInstance().leftFrontDrive.getOutputCurrent());
	
	}

	public void disabledInit() {
		//Null
	}

	public void disabledPeriodic() {
		//Null <- Whoever wrote this is stooped
	}

	public void testInit() {
		drive.driveInit();
	}

	public void testPeriodic() {
		CommonArea.CommonPeriodic();
		
		SmartDashboard.putNumber("DB/String 0", Components.getInstance().rightFrontDrive.getAnalogInPosition());
		SmartDashboard.putNumber("DB/String 1", Components.getInstance().leftFrontDrive.getAnalogInPosition());

		drive.driveTeleopPeriodic();
	}
}
