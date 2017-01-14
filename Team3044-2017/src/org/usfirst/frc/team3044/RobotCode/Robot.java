package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.RobotCode.*;
import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Drive drive = new Drive();
	FirstController controller;
	private double Dashboard;

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
		drive.testInit();
	}

	public void testPeriodic() {
		CommonArea.CommonPeriodic();
	}
}
