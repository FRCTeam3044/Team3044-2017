package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.RobotCode.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.usfirst.frc.team3044.Reference.*;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Diagnostics.*;

public class Robot extends IterativeRobot {
	org.usfirst.frc.team3044.RobotCode.Vision vision = new org.usfirst.frc.team3044.RobotCode.Vision();

	Drive drive = new Drive();
	Gear gear;
	Climber climber = new Climber();

	SmartDashboard smartDashboard = new SmartDashboard();

	DigitalOutput digitalOutput1 = new DigitalOutput(7);
	DigitalOutput digitalOutput2 = new DigitalOutput(8);
	DigitalOutput digitalOutput3 = new DigitalOutput(9);

	byte test = 0;

	DiagnosticsServer diagnosticsServer = new DiagnosticsServer();

	public void robotInit() {
		Outputs.getInstance().init();
		drive.driveInit();
	}

	public void autonomousInit() {
		drive.driveInit();
	}

	public void autonomousPeriodic() {
		System.out.println("Auto Periodic");

		drive.driveAutoPeriodic();

	}

	public void teleopInit() {
		Outputs.getInstance().init();
		drive.driveInit();
		climber.climberInit();

	}

	public void teleopPeriodic() {
		drive.driveTeleopPeriodic();
		climber.climberTeleopPeriodic();

		test = (byte) Math.floor(smartDashboard.getDouble("DB/Slider 0"));

		digitalOutput1.set((test & 4) == 4);
		digitalOutput2.set((test & 2) == 2);
		digitalOutput3.set((test & 1) == 1);

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
