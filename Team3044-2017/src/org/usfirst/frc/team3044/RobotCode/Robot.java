package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.RobotCode.*;

import java.io.IOException;

import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Drive drive = new Drive();
	FirstController controller;
	private double Dashboard;
	
	RobotHttpServer httpService = new RobotHttpServer(); 
	
	
	public void robotInit() {
		Components.getInstance().init();
		controller.getInstance();
		drive.driveInit();
	}

	public void autonomousInit() {
	}


	public void autonomousPeriodic() {
	}

	public void teleopInit() {
		Components.getInstance().init();
		
		//drive.driveInit();
	}

	public void teleopPeriodic() {
		CommonArea.CommonPeriodic();
		drive.driveTeleopPeriodic();
	}

	public void disabledInit() {
	}

	public void disabledPeriodic() {
	}

	public void testInit() {
		try {
			httpService.start(0, true);
		} catch (IOException e) {
			System.out.println("Exception thrown while starting HTTP server");
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}

	public void testPeriodic() {
		CommonArea.CommonPeriodic();
	}
}
