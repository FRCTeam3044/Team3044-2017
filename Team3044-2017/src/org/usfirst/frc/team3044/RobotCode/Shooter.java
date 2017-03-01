/* Ethan Tabachneck
 * 2/21/17
 * FRC Robotics 2017
 * enables usage of shooter and impeller based on RPMs of the fly wheel on
 * the shooter.
 */
package org.usfirst.frc.team3044.RobotCode;

// Imports files from the code system and package to pull inputs and allow 
// specialized functions in the code.
import org.usfirst.frc.team3044.Reference.*;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// creates the shooter function
public class Shooter {

	public Outputs out = Outputs.getInstance();

	// declares outputs for talon controlled motors associated w/shooter
	public CANTalon shooter;
	public CANTalon shooter2;
	public CANTalon impeller;
	DummyTacho shooterTacho = out.shooterTachoCounter;

	SecondController secondCon = new SecondController();

	// sets the RPM variables for tachometer
	double currentRPM = 0;
	final int shootingRPM = 50;
	// double period = shooterTacho.getPeriod();

	// sets powers for the powered objects
	public double shootPower = .6;

	// sets the true false statements to false that will determine function
	// later on in the code
	boolean shooterOn = false;
	boolean canShoot = false;

	// variables for fail safe
	double resistanceCurr = 1;
	double resistanceThres = 0;
	boolean okayFlag = true;

	// makes a timer
	Timer time = new Timer();

	// adds write commands
	File f;
	BufferedWriter bw;
	FileWriter fw;

	// code that runs when robot is initiated
	public void shooterInit() {

		// makes a file for data
		try {
			f = new File("/shooterData.txt");
			if (!f.exists())
				f.createNewFile();
			fw = new FileWriter(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// sets and starts a timer
		time.reset();
		time.start();

		shooter = out.shooter;
		shooter2 = out.shooter2;
		impeller = out.impeller;

	}

	// creates what the robot does in autonomous
	public void shooterAutoPeriodic() {
	}

	// creates what the robot does in teleop
	public void shooterTeleopPeriodic() {

		SmartDashboard.putString("DB/String 8", String.valueOf(shooterTacho.getRate()));
		currentRPM = shooterTacho.getRate();

		if (secondCon.getRawButton(secondCon.BUTTON_BACK)) {
			shooter.set(shootPower);
			shooter2.set(shootPower);
		} else {
			shooter.set(0);
			shooter2.set(0);
		}

		if (secondCon.getRawButton(secondCon.BUTTON_START)) {
			impeller.set(.6);
		} else {
			impeller.set(0);
		}

		/*
		 * Conditional Statement For Adding 10% and Subtracting 10% To Each
		 * Shooter Motor
		 */

		if (secondCon.getRawButton(secondCon.BUTTON_LB)) {
			shootPower += .01;
		} else if (secondCon.getRawButton(secondCon.BUTTON_RB)) {
			shootPower -= .01;
		}

		// checks for required safe mode
		/*
		 * resistanceCurr = shooter.getOutputVoltage() /
		 * shooter.getOutputCurrent(); if (resistanceCurr <= resistanceThres)
		 * okayFlag = false;
		 */
		okayFlag = true;
		// sets RPMs read as to not have them change mid-read
		// if (period != 0)
		// currentRPM = (int) (60 / period);

		// checks for the shooter being okay
		if (okayFlag) {

			// makes it so when right bumper is hit, it changes the shooter
			// from on to off
			if (secondCon.getTriggerLeft()) {
				shooterOn = !shooterOn;
			}
			shooterStart(shooterOn);

			// starts impeller method based on right triggers
			impellerStart(secondCon.getTriggerRight());
		} else {
			shooterStart(false);
			impellerStart(false);
		}

		// writes a file
		/*
		 * try { fw.write(time.get() + ", " + currentRPM + ", " + resistanceCurr
		 * + "\n"); } catch (IOException e) { e.printStackTrace(); }
		 */

	}

	// the function that runs when test is initiated
	public void testInit() {

	}

	public void testPeriodic() {

	}

	// creates a method for starting the shooter
	public void shooterStart(boolean turnShooterOn) {

		// Checks to see if the shooter boolean is on and that the
		// Tacho is reading the accurate RPM, and if so, allows for
		// shooting
		if (turnShooterOn) {

			// tests RPM to adjust the motor to regulate shooter RPMs
			if (currentRPM > (shootingRPM - 5) && currentRPM < (shootingRPM + 5)) {
				shooter.set(shootPower);
				shooter2.set(shootPower);
				canShoot = true;
			}

			// adds motor power if the RPM does not surpass a minimum
			// value
			else if (currentRPM <= (shootingRPM - 5)) {
				shootPower = (shootPower + .1);
				shooter.set(shootPower);
				shooter2.set(shootPower);
				canShoot = false;
			}

			// reduces motor speed if the RPM surpasses a maximum value
			else if (currentRPM >= (shootingRPM + 5)) {
				shootPower = (shootPower + .1);
				shooter.set(shootPower);
				shooter2.set(shootPower);
				canShoot = false;
			}
		} else {

			// if the shooter isn't toggled, it gets no power and makes
			// it so impeller can't start
			canShoot = false;
			shooter.set(0);
			shooter2.set(0);
		}
	}

	// creates a method for starting the impeller
	public void impellerStart(boolean impellerOn) {

		// checks for a true from the previous check and for the
		// trigger to be activated and then starts the impeller,
		// if not, all shuts off
		if (impellerOn && canShoot) {
			impeller.set(.6);
		} else {
			impeller.set(0);
		}
	}
}
