package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.RobotCode.*;

import java.io.IOException;

import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Diagnostics.*;

public class Robot extends IterativeRobot {
	Drive drive = new Drive();
	FirstController controller;
	private double Dashboard;
	
	DiagnosticsServer diagnosticsServer = new DiagnosticsServer(); 
	
	public void robotInit() {
		Outputs.getInstance().init();
		controller.getInstance();
		drive.driveInit();
	}

	public void autonomousInit() {
	}


	public void autonomousPeriodic() {
	}

	public void teleopInit() {
		Outputs.getInstance().init();
		drive.driveInit();
	}

	public void teleopPeriodic() {
		Inputs.CommonPeriodic();
		drive.driveTeleopPeriodic();
	}

	public void disabledInit() {
	}

	public void disabledPeriodic() {
	}

	public void testInit() {
		try { 
		diagnosticsServer.start(0, true);
		}
		catch (IOException e)
		{
			
		}
	}

	public void testPeriodic() {
		Inputs.CommonPeriodic();
	}
}
