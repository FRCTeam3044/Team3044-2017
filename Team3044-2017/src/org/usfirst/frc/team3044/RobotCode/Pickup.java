/*Evan Couchman
 * 2/6/2017
 * FRC Robotics 2017
 * Programs the CANTalon of the intake system (sweeper)
*/
package org.usfirst.frc.team3044.RobotCode;

import com.ctre.CANTalon;
import org.usfirst.frc.team3044.Reference.SecondController;
import org.usfirst.frc.team3044.Reference.Outputs;

public class Pickup {

	// defines controller

	SecondController controller = new SecondController();

	public Outputs out = Outputs.getInstance();

	// defines CANTalon

	public CANTalon pickUp;

	// A deadband sets the tolerance of the joystick
	// Change if necessary
	public double deadband(double value) {
		if (Math.abs(value) < .15) {
			return 0;
		} else {
			return value;
		}
	}

	public void pickupInit() {

		/*
		 * Sets the CANTalon being used in this code to the CANTalon that is
		 * defined in the code "Outputs.java"
		 */

		pickUp = out.pickUp;
	}

	public void pickupAutoInit() {
	}

	public void pickupAutoPeriodic() {
	}

	// TeleOp phase
	public void pickupTeleopPeriodic() {

		pickUp.set(deadband(-controller.getLeftY() * 0.8));
	}

	public void testInit() {
	}

	public void testPeriodic() {
	}
}
