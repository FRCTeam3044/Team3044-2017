/*
 * Allanah Mathews
 * 4/10/2017
 */
package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.CANTalon;

public class Gear {
	// We are using CANTalons which controls motors to extend and retract
	// start closed
	// port number might change as Cassie is using 1 & 2 such as 3 & 4
	public CANTalon GearCANTalon;

	// public CANTalon GearCantalon = new CANTalon(2)
	public DigitalInput limitSwitchOut = Inputs.limitSwitchOut;
	public DigitalInput limitSwitchIn = Inputs.limitSwitchIn;

	public Outputs out = Outputs.getInstance();

	// Code does not have breaks as I am not sure where to use them
	// As of 2/11/17 CANTalons not solenoids
	public void gearInit() {
		// called once, when robot starts up. Use limit switches
		// Motor starts off
		GearCANTalon = out.GearCANTalon;
	}

	public void gearAutoInit() {
	}

	public void gearAutoPeriodic() {
	}

	public void gearPneumatic() {

		boolean buttonBPressed = SecondController.getInstance().getRawButton(SecondController.BUTTON_B);

		if (SecondController.getInstance().getRawButton(SecondController.BUTTON_B)) {

			out.gearPneumaticRelease.set(true);
			out.gearPneumaticIn.set(false);
		}

		else {

			out.gearPneumaticRelease.set(false);
			out.gearPneumaticIn.set(true);
		}
	}

	/****************************************************************************
	 * TeleopPeriod Goal: Use X button to drive motors forward & B button to
	 * drive motors backwards Start fully retracted, X = extending until fully
	 * extended unless B button is pressed = retracting until fully retracted or
	 * X button is pushed once more If I button mash X and then B back and
	 * forth, should be retracting and extending like it's spazzing
	 * 
	 * @return
	 ***************************************************************************/
	public void gearTeleopPeriodic() {

		gearPneumatic();

		/****************************************************************************
		 * 3 if statement to satisfy the gear Mechanism. Does not use states
		 * Trial 1
		 * 
		 * @return
		 ***************************************************************************/

		// B = in or retracting
		/*
		 * boolean buttonBPressed = SecondController.getInstance().getRawButton(SecondController.BUTTON_B);
		 * // boolean buttonXPressed = SecondController.getInstance().getRawButton(SecondController.BUTTON_X);
		 * 
		 * if (buttonBPressed && limitSwitchIn.get()) {
		 * GearCANTalon.set(1);
		 * } else if (!buttonBPressed && limitSwitchOut.get()) {
		 * GearCANTalon.set(-.5);
		 * } else {
		 * GearCANTalon.set(0);
		 * }
		 */

	}

	public void testInit() {

	}

	public void testPeriodic() {
	}
}
