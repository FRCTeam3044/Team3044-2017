/* Ethan Tabachneck
 * 03/03/17
 * FRC Robotics 2017
 * enables usage of shooter and impeller based on RPMs of the fly wheel on
 * the shooter.
 */
package org.usfirst.frc.team3044.RobotCode;

// Imports files from the code system and package to pull inputs and allow 
// specialized functions in the code.
import org.usfirst.frc.team3044.Reference.*;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// creates the shooter function
public class Shooter {

	public Outputs out = Outputs.getInstance();

	// declares outputs for talon controlled motors associated w/shooter
	public CANTalon shooter;
	public CANTalon shooter2;
	public CANTalon impeller;
	DummyTacho shooterTacho = out.shooterTachoCounter;
	PIDController shooterPID;
	PIDController shooter2PID;

	SecondController secondCon = new SecondController();

	final double toleranceShooter = 4;

	double shootPower = .6;
	double impPower = .7;

	// sets the RPM variables for tachometer
	double p = .001, i = -1, d = 0.003, shootingRPM = 38;

	// sets the true false statements to false that will determine function
	// later on in the code
	boolean canShoot = false;

	public boolean OnTarget(double TargetValue, double Value, double Threshold) {
		return Math.abs(TargetValue - Value) < Threshold;

	}

	// code that runs when robot is initiated
	public void shooterInit() {

		shooter = out.shooter;
		shooter2 = out.shooter2;
		impeller = out.impeller;

		shooterTacho.setPIDSourceType(PIDSourceType.kRate);

		if (shooterPID == null) {
			shooterPID = new PIDController(p, i, d, shooterTacho, out.shooter);
			shooter2PID = new PIDController(p, i, d, shooterTacho, out.shooter2);
		}

		shooterPID.setInputRange(0, 150);
		shooter2PID.setInputRange(0, 150);
		shooterPID.setOutputRange(0, 1);
		shooter2PID.setOutputRange(0, 1);

		shooterPID.enable();
		shooter2PID.enable();
		shooterPID.setPID(p, i, d);
		shooter2PID.setPID(p, i, d);

		shooterPID.setAbsoluteTolerance(toleranceShooter);
		shooter2PID.setAbsoluteTolerance(toleranceShooter);
		out.shooter.setPIDSourceType(PIDSourceType.kRate);

		SmartDashboard.putString("Target RPM", "0");
		SmartDashboard.putString("P value", "0");
		SmartDashboard.putString("I value", "0");
		SmartDashboard.putString("D value", "0");
	}

	// creates what the robot does in autonomous
	public void shooterAutoPeriodic() {
	}

	// creates what the robot does in teleop
	public void shooterTeleopPeriodic() {

		// shootingRPM = SmartDashboard.getNumber("DB/Slider 0");
		// impPower = SmartDashboard.getNumber("DB/Slider 1");
		// SmartDashboard.putString("DB/String 8", String.valueOf(shooterTacho.getRate()));

		SmartDashboard.putNumber("RPM value", shooterTacho.getRate());
		/*
		 * p = Double.parseDouble(SmartDashboard.getString("P value"));
		 * i = Double.parseDouble(SmartDashboard.getString("I value"));
		 * d = Double.parseDouble(SmartDashboard.getString("D value"));
		 * shootingRPM = Double.parseDouble(SmartDashboard.getString("Target"));
		 */

		if (secondCon.getDPadUp()) {
			shootingRPM = shootingRPM * 1.02;

			// This needs to be tested
			/*
			 * try {
			 * Thread.sleep(500);
			 * } catch (InterruptedException e) {
			 * e.printStackTrace();
			 * }
			 */
		} else if (secondCon.getDPadDown()) {
			shootingRPM = shootingRPM / 1.02;

			// This needs to be tested
			/*
			 * try {
			 * Thread.sleep(500);
			 * } catch (InterruptedException e) {
			 * e.printStackTrace();
			 * }
			 */
		}

		SmartDashboard.putString("DB/String 5", String.valueOf(shootingRPM));
		impPower = 1; 
		//impPower = SmartDashboard.getNumber("DB/Slider 2");

		if (secondCon.getRawButton(secondCon.BUTTON_START)) {
			shooter.set(shootPower);
			shooter2.set(shootPower);
		}

		startShooter(secondCon.getTriggerLeft());
		impOn(secondCon.getTriggerRight());
	}

	// the function that runs when test is initiated
	public void testInit() {

	}

	public void testPeriodic() {

	}

	// creates a method for starting the shooter
	public void startShooter(boolean onPID) {

		if (onPID) {

			shooterPID.setSetpoint(shootingRPM);
			shooter2PID.setSetpoint(shootingRPM);

			if (OnTarget(shooterPID.getSetpoint(), shooterTacho.getRate(), 5) && OnTarget(shooter2PID.getSetpoint(), shooterTacho.getRate(), 5)) {

				System.out.println("Up to speed");
				canShoot = true;
			} else {

				canShoot = false;
			}
		} else {

			canShoot = false;
			shooterPID.setSetpoint(0);
			shooter2PID.setSetpoint(0);
		}
	}

	// creates a method for starting the impeller
	public void impOn(boolean onImp) {
		if (onImp) {
			impeller.set(impPower);
		} else {
			impeller.set(0);
		}
	}
}
