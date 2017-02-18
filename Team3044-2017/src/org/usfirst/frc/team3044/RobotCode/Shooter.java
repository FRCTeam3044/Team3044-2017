/* Ethan Tabachneck
 * 2/11/17
 * FRC Robotics 2017
 * enables usage of shooter and agitator based on RPMs of the fly wheel on
 * the shooter.
 */
package org.usfirst.frc.team3044.RobotCode;

// Imports files from the code system and package to pull inputs and allow 
// specialized functions in the code.
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.usfirst.frc.team3044.Reference.* ;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

// creates the shooter function
public class Shooter {

	// sets the RPM variables for tachometer
	final int shootingRPM = 2700;

	// sets powers for the powered objects
	public double shootPower = .6;
	public double agitatorPower;

	// sets the true false statements to false that will determine function
	// later on in the code
	boolean start = false;
	boolean shooterOn = false;
	boolean canShoot = false;

	// declares outputs for talon controlled motors associated w/shooter
	public CANTalon shooter;
	public CANTalon agitator;
	DigitalInput limitSwitch = new DigitalInput(1);
	DigitalInput limitSwitch2 = new DigitalInput(2);
	AnalogInput ShooterTacho = new AnalogInput(0);
	SecondController secondCon = new SecondController();

	// not used but there from the compile eclipse uses
	public void driveInit() {

	}

	// creates what the robot does in autonomous
	public void driveAutoPeriodic() {
	}

	// creates what the robot does in teleop
	public void driveTeleopPeriodic() {
		{
			// declares the tachometer that reads the RPMs of the shooter
			AnalogInput ShooterTacho = new AnalogInput(0);
			// v Converts digital to analog v
			ShooterTacho.setOversampleBits(4);
			ShooterTacho.setAverageBits(2);

			// makes it so when right bumper is hit, it changes the shooter
			// from on to off
			if (secondCon.getRawButton(SecondController.BUTTON_RB)) {
				shooterOn = !shooterOn;
			}

			// Checks to see if the shooter boolean is on and that the
			//Tacho is reading the accurate RPM, and if so, allows for
			//shooting
			if (shooterOn) {

				// tests RPM to adjust the motor to regulate shooter RPMs
				if (ShooterTacho.getValue() > (shootingRPM - 5) && ShooterTacho.getValue() < (shootingRPM + 5)) {
					shooter.set(shootPower);
					canShoot = true;
				}

				// adds motor power if the RPM does not surpass a minimum
				// value
				else if (ShooterTacho.getValue() <= (shootingRPM - 5)) {
					shootPower = (shootPower + .1);
					shooter.set(shootPower);
					canShoot = false;
				}

				// reduces motor speed if the RPM surpasses a maximum value
				else if (ShooterTacho.getValue() >= (shootingRPM + 5)) {
					shootPower = (shootPower + .1);
					shooter.set(shootPower);
					canShoot = false;
				}
			} else {

				// if the shooter isn't toggled, it gets no power
				shooter.set(0);
			}

			// checks for a true from the previous check and for the
			// trigger to be activated and then starts the agitator,
			// if not, all shuts off
			if (secondCon.getTriggerRight() && canShoot) {
				agitator.set(.2);
			} else {
				agitator.set(0);
			}
		}
	}

	// another unused function from the compiler
	public void testInit() {

	}
}
