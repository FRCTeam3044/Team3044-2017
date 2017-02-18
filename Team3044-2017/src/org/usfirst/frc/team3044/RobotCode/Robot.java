package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.RobotCode.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Diagnostics.*;

public class Robot extends IterativeRobot {
	org.usfirst.frc.team3044.RobotCode.Vision vision = new org.usfirst.frc.team3044.RobotCode.Vision();

	Drive drive;
	Gear gear;
	Climber climber;
	FirstController controller;
	public double Dashboard;

	DiagnosticsServer diagnosticsServer = new DiagnosticsServer();

	public void robotInit() {
		Outputs.getInstance().init();
		controller.getInstance();
	}

	public void autonomousInit() {
	}

	public void autonomousPeriodic() {

	}

	public void teleopInit() {
		Outputs.getInstance().init();
		drive.driveInit();
		gear.gearInit();
		climber.climberInit();

	}

	public void teleopPeriodic() {
		drive.driveTeleopPeriodic();
		gear.gearTeleopPeriodic();
		climber.climberTeleopPeriodic();
	}

	public void disabledInit() {
	}

	public void disabledPeriodic() {
	}

	public void testInit() {
		vision.robotInit();
		vision.autonomousInit();
		try {
			diagnosticsServer.start(0, true);
		} catch (IOException e) {

		}
	}

	public void testPeriodic() {
		// Change this
		vision.autonomousPeriodic();
	}
}
