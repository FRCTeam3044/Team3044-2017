package org.usfirst.frc.team3044.RobotCode;

import java.io.IOException;
import org.usfirst.frc.team3044.Reference.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
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
	// AHRS ahrs;

	Timer time = new Timer();

	public void robotInit() {
		Outputs.getInstance().init();
		vision.robotInit();
		/*
		 * try {
		 * ahrs = new AHRS(I2C.Port.kOnboard);
		 * ahrs.reset();
		 * ahrs.resetDisplacement();
		 * } catch (RuntimeException ex) {
		 * System.out.println("Error instantiating navX-MXP:  " + ex.getMessage());
		 * }
		 */
	}

	public void autonomousInit() {
		vision.startVisionThread();
		// vision.autonomousInit();
		try {
			diagnosticsServer.start(0, true);
		} catch (IOException e) {

		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	int driveForwardState = 0;

	public void driveForward() {
		switch (driveForwardState) {
		case 0:
			vision.autonomousPeriodic();
			driveForwardState = 1;
			/*
			 * double rot = .95 - (ahrs.getAngle() / 45);
			 * if (rot > .3)
			 * rot = .3;
			 * if (rot < -.3)
			 * rot = -.3;
			 * out.leftFrontDrive.set(-rot);
			 * out.leftBackDrive.set(-rot);
			 * out.rightFrontDrive.set(-rot);
			 * out.rightBackDrive.set(-rot);
			 * SmartDashboard.putString("DB/String 1", "Angle: " + String.valueOf(ahrs.getAngle()));
			 * SmartDashboard.putString("DB/String 1", "X: " + String.valueOf(ahrs.getCompassHeading()));
			 */
			break;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	int RightGearState = 0;

	public void RightGear() {
		switch (RightGearState) {

		case 0:
			out.leftFrontDrive.set(-.5);
			out.leftBackDrive.set(-.5);
			out.rightFrontDrive.set(.5);
			out.rightBackDrive.set(.5);
			time.start();
			RightGearState = 1;
			break;

		case 1:
			if (time.get() > 1.5) {
				out.leftFrontDrive.set(-.3);
				out.leftBackDrive.set(-.3);
				out.rightFrontDrive.set(-.3);
				out.rightBackDrive.set(-.3);
				time.stop();
				time.reset();
				time.start();
				RightGearState = 2;
			}
			break;

		case 2:
			if (time.get() > 1.8 || gear.limitSwitchOut.get()) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				vision.autonomousPeriodic();
				RightGearState = 3;
			}
			break;

		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------
	int LeftGearState = 0;

	public void LeftGear() {
		switch (RightGearState) {

		case 0:
			out.leftFrontDrive.set(-.5);
			out.leftBackDrive.set(-.5);
			out.rightFrontDrive.set(.5);
			out.rightBackDrive.set(.5);
			time.start();
			LeftGearState = 1;
			break;

		case 1:
			if (time.get() > 1.5) {
				out.leftFrontDrive.set(.3);
				out.leftBackDrive.set(.3);
				out.rightFrontDrive.set(.3);
				out.rightBackDrive.set(.3);
				time.stop();
				time.reset();
				time.start();
				LeftGearState = 2;
			}
			break;

		case 2:
			if (time.get() > 1.8 || gear.limitSwitchOut.get()) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				vision.autonomousPeriodic();
				LeftGearState = 3;
			}
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
			this.RightGear();
		} else if (Dashboard == 2) {
			this.LeftGear();
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
		vision.stopVisionThread();
	}

	public void disabledPeriodic() {
		//SmartDashboard.putString("DB/String 1", "Angle: " + String.valueOf(ahrs.getAngle()));

	}

	public void testInit() {

		vision.startVisionThread();
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
