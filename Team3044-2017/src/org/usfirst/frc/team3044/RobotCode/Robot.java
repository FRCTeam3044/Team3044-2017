package org.usfirst.frc.team3044.RobotCode;

import java.io.IOException;
import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Diagnostics.*;

public class Robot extends IterativeRobot {
	org.usfirst.frc.team3044.RobotCode.Vision vision = new org.usfirst.frc.team3044.RobotCode.Vision(); // Used because there is another vision class.
	Drive drive = new Drive();
	Gear gear = new Gear();
	Climber climber = new Climber();
	Shooter shooter = new Shooter();
	Pickup pickup = new Pickup();
	public Outputs out = Outputs.getInstance();
	DiagnosticsServer diagnosticsServer = new DiagnosticsServer();
	Timer time = new Timer();

	// Sets the auto states to 0
	int driveForwardState = 0; // For baseline, timed
	int driveForwardGearState = 0; // Vision for center gear
	int RightGearState = 0; // Vision for gear starting on the right
	int LeftGearState = 0; // Vision for gear starting on the left
	int timedGearMiddleState = 0; // Gear in the center, timed

	/////////////////////////////////////////////////////////////////////////////////////////////////

	// Function to set the auto states to 0
	public void initializeAutoStates() {
		driveForwardState = 0;
		driveForwardGearState = 0;
		RightGearState = 0;
		LeftGearState = 0;
		timedGearMiddleState = 0;
	}

	public void robotInit() {
		initializeAutoStates(); // Calls function to set the auto states to 0

		// These 2 should only be called once. Keep it like this
		Outputs.getInstance().init();
		vision.robotInit();
		// Thus ends the mandate "keep it like this". However, always let a trained professional wrangle the spaghetti code found below.
		// Don't try this at home, kids.
	}

	public void autonomousInit() {
		initializeAutoStates(); // Calls function to set the auto states to 0
		vision.startVisionThread(); // Starts the vision processing
		try {
			diagnosticsServer.start(0, true);
		} catch (IOException e) {

		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	public void driveForward() {
		switch (driveForwardState) {
		case 0:
			out.leftFrontDrive.set(-.5);
			out.leftBackDrive.set(-.5);
			out.rightFrontDrive.set(.5);
			out.rightBackDrive.set(.5);
			time.start();
			driveForwardState = 1;
			break;
		case 1:
			if (time.get() > 1.5) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				time.stop();
				time.reset();
				time.start();
				driveForwardState = 2;
			}
			break;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	public void driveForwardGear() {
		switch (driveForwardGearState) {
		case 0:
			time.start();
			driveForwardGearState = 1;
			break;
		case 1:
			vision.autonomousPeriodic(); // Immediately goes into to the vision auto(which will automatically move forward)
			if (time.get() > 10) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				time.stop();
				time.reset();
				driveForwardGearState = 2;
			}
			break;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	public void RightGear() {
		switch (RightGearState) {

		case 0: // Moves forward
			out.leftFrontDrive.set(-.5);
			out.leftBackDrive.set(-.5);
			out.rightFrontDrive.set(.5);
			out.rightBackDrive.set(.5);
			time.start();
			RightGearState = 1;
			break;

		case 1: // Once the time reaches 1.5 seconds, turn
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

		case 2: // When the reseted time reaches 1.8 seconds, stop moving and go into the vision auto.
			if (time.get() > 1.8 || gear.limitSwitchOut.get()) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				// vision.autonomousPeriodic();
				RightGearState = 3;
			}
			break;

		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	public void LeftGear() {
		switch (LeftGearState) {

		case 0: // Moves forward
			out.leftFrontDrive.set(-.5);
			out.leftBackDrive.set(-.5);
			out.rightFrontDrive.set(.5);
			out.rightBackDrive.set(.5);
			time.start();
			LeftGearState = 1;
			break;

		case 1:// Once the time reaches 1.5 seconds, turn
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

		case 2:// When the reseted time reaches 1.8 seconds, stop moving and go into the vision auto.
			if (time.get() > 1.8 || gear.limitSwitchOut.get()) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				// vision.autonomousPeriodic();
				LeftGearState = 3;
			}
			break;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	public void timedGearMiddle() {
		switch (timedGearMiddleState) {
		case 0:
			out.leftFrontDrive.set(-.5);
			out.leftBackDrive.set(-.5);
			out.rightFrontDrive.set(.5);
			out.rightBackDrive.set(.5);
			time.start();
			timedGearMiddleState = 1;
			break;

		case 1:
			if (time.get() > 1.3) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);

				out.GearCANTalon.set(1);

				time.stop();
				time.reset();
				time.start();
				timedGearMiddleState = 2;
			}
			break;
		case 2:
			if (time.get() > .5 || !gear.limitSwitchIn.get()) {

				out.GearCANTalon.set(0);
				time.stop();
				time.reset();
				time.start();
				timedGearMiddleState = 3;
			}
			break;

		case 3:
			if (time.get() > .5) {
				out.leftFrontDrive.set(.5);
				out.leftBackDrive.set(.5);
				out.rightFrontDrive.set(-.5);
				out.rightBackDrive.set(-.5);
				time.stop();
				time.reset();
				time.start();
				timedGearMiddleState = 4;
			}
			break;
		case 4:
			if (time.get() > .5) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);
				time.stop();
				time.reset();
				time.start();
				timedGearMiddleState = 4;
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
		// double Dashboard = 2;

		if (Dashboard == 0) {
			this.driveForward(); // For baseline, timed
		} else if (Dashboard == 1) {
			this.driveForwardGear(); // Vision center gear
		} else if (Dashboard == 2) {
			this.timedGearMiddle(); // Center gear, timed
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
		vision.stopVisionThread(); // Don't run vision when disabled
	}

	public void disabledPeriodic() {
		// SmartDashboard.putString("DB/String 1", "Angle: " +
		// String.valueOf(ahrs.getAngle()));'
	}

	public void testInit() { // Vision is called in testInit and testPeriodic for, well, testing.

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
