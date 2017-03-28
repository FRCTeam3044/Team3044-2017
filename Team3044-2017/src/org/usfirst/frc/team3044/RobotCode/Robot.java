package org.usfirst.frc.team3044.RobotCode;

import java.io.IOException;
import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Diagnostics.*;

public class Robot extends IterativeRobot {
	org.usfirst.frc.team3044.RobotCode.Vision vision = new org.usfirst.frc.team3044.RobotCode.Vision();

	Drive drive = new Drive();
	Gear gear = new Gear();
	Climber climber = new Climber();
	Shooter shooter = new Shooter();
	Pickup pickup = new Pickup();

	public Outputs out = Outputs.getInstance();

	DiagnosticsServer diagnosticsServer = new DiagnosticsServer();

	Timer time = new Timer();

	public void robotInit() {
		Outputs.getInstance().init();
		vision.robotInit();
	}

	public void autonomousInit() {

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	/* 
	  
	 NNR this is a really bad place for this. It's a class variable, but its declared right on top of a 
	 method. This should me moved the class definition. 
	
	 Also, this could be the one of the reasons behind autonomous initialization failure - driveForwardState is never re-set to 0. 
	 Until the class is re-declared, this variable will stay set to 5 after driveForward() is called once. 
	 
	 */
	
	int driveForwardState = 0;

	public void driveForward() {
		switch (driveForwardState) {
		case 0:
			out.leftFrontDrive.set(-.6);
			out.leftBackDrive.set(-.6);
			out.rightFrontDrive.set(.6);
			out.rightBackDrive.set(.6);
			time.start();
			driveForwardState = 1;
			break;

		case 1:
			if (time.get() > 1.5) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				driveForwardState = 5;
			}
			break;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	int middleGearState = 0;

	public void middleGear() {
		switch (middleGearState) {

		case 0:
			out.leftFrontDrive.set(-.5);
			out.leftBackDrive.set(-.5);
			out.rightFrontDrive.set(.5);
			out.rightBackDrive.set(.5);
			time.start();
			middleGearState = 1;
			break;

		case 1:
			if (time.get() > 1.5) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				out.GearCANTalon.set(-1);
				time.stop();
				time.reset();
				time.start();
				middleGearState = 2;
			}
			break;

		case 2:
			if (time.get() > 1 || gear.limitSwitchOut.get()) {
				out.GearCANTalon.set(0);
				middleGearState = 3;
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	int sideGearState = 0;

	public void sideGear() {
		switch (sideGearState) {
		case 0:
			out.leftFrontDrive.set(-.6);
			out.leftBackDrive.set(-.6);
			out.rightFrontDrive.set(.6);
			out.rightBackDrive.set(.6);
			time.start();
			sideGearState = 1;
			break;

		case 1:
			if (time.get() > 1.5) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(.3);
				out.rightBackDrive.set(.3);
				time.stop();
				time.reset();
				time.start();
				sideGearState = 2;
			}
			break;
		}
	}

	/*
	 * Auto Periodic Allows For Driver Station Selection Of An Auto; This Is
	 * Done By Using A Slider;
	 */
	public void autonomousPeriodic() {
		double Dashboard = SmartDashboard.getNumber("DB/Slider 0", 10);

		if (Dashboard == 0) {
			this.driveForward();
		} else if (Dashboard == 1) {
			this.middleGear();
		} else if (Dashboard == 2) {
			this.sideGear();
		}
	}

	public void teleopInit() {
		Outputs.getInstance().init();
		drive.driveInit();
		climber.climberInit();
		gear.gearInit();
		shooter.shooterInit();
		pickup.pickupInit();
	}

	public void teleopPeriodic() {
		drive.driveTeleopPeriodic();
		climber.climberTeleopPeriodic();
		gear.gearTeleopPeriodic();
		shooter.shooterTeleopPeriodic();
		pickup.pickupTeleopPeriodic();

	}

	public void disabledInit() {
		diagnosticsServer.stop(); 
	}

	public void disabledPeriodic() {
	}

	public void testInit() {

		// vision.autonomousInit();
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
