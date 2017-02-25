package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.RobotCode.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.usfirst.frc.team3044.Reference.*;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
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
	}

	public void autonomousInit() {
		drive.driveAutoInit();
	}

	/*
	 * This Is The Zero Auto Code; This Code Uses Time For Start/Stop; Also Has
	 * The Value Added To leftBackDrive; The Robot Will Drive Forward For Two
	 * Seconds Then Stop;
	 */
	public void driveForward() {
		int driveForwardState = 0;

		switch (driveForwardState) {
		case 0:
			out.leftFrontDrive.set(-.25);
			out.leftBackDrive.set(-.25 * .84);
			out.rightFrontDrive.set(.25);
			out.rightBackDrive.set(.25);

			time.start();
			driveForwardState = 1;

			break;

		case 1:
			if (time.get() > 2) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);

				driveForwardState = 5;
			}
			break;

		}
	}

	/*
	 * This Is The First Auto Code; This Code Uses Time For Start/Stop; Also Has
	 * The Value Added To leftBackDrive; The Robot Will Shoot And Run Impeller
	 * For 3 Seconds Then Stop; The Robot Will Then Drive Forward For Three
	 * Seconds;
	 */
	public void shootAndCross() {
		int shootAndCross = 0;

		switch (shootAndCross) {
		case 0:
			shooter.shooterStart(true);
			shooter.impellerStart(true);

			time.start();
			shootAndCross = 1;

			break;

		case 1:
			if (time.get() > 3) {
				shooter.shooterStart(false);
				shooter.impellerStart(false);

				out.leftFrontDrive.set(-.25);
				out.leftBackDrive.set(-.25 * .84);
				out.rightFrontDrive.set(.25);
				out.rightBackDrive.set(.25);

				shootAndCross = 2;
			}
			break;
		case 2:
			if (time.get() > 3) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);

				shootAndCross = 5;
			}
			break;
		}
	}

	/*
	 * This Is The Second Auto Code; This Code Uses Time For Start/Stop; Also
	 * Has The Value Added To leftBackDrive; The Robot Will Drive Forward For
	 * Two Seconds Then Stop; Then The Gear Mechanism Will Open; Then The Gear
	 * Mechanism Will Close;
	 */
	public void onlyGear() {
		int onlyGearState = 0;

		switch (onlyGearState) {
		case 0:
			out.leftFrontDrive.set(-.25);
			out.leftBackDrive.set(-.25 * .84);
			out.rightFrontDrive.set(.25);
			out.rightBackDrive.set(.25);

			time.start();
			onlyGearState = 1;

			break;

		case 1:
			if (time.get() > 2) {
				out.leftFrontDrive.set(0);
				out.leftBackDrive.set(0);
				out.rightFrontDrive.set(0);
				out.rightBackDrive.set(0);

				onlyGearState = 2;
			}
			break;

		case 2:
			if (time.get() > 2) {
				out.GearCANTalon.set(-1);
			}
			onlyGearState = 3;
			break;

		case 3:
			if (time.get() > 2) {
				out.GearCANTalon.set(0.5);
			}
			onlyGearState = 5;
			break;
		}
	}

	/*
	 * Auto Periodic Allows For Driver Station Selection Of An Auto; This Is
	 * Done By Using A Slider;
	 */
	public void autonomousPeriodic() {
		double Dashboard = SmartDashboard.getNumber("DB/Slider 0", 7);

		if (Dashboard == 0) {
			this.driveForward();
		} else if (Dashboard == 1) {
			this.shootAndCross();
		} else if (Dashboard == 2){
			this.onlyGear();
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
