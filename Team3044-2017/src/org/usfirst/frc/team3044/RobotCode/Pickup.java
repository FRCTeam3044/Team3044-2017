/*Evan Couchman
 * 3/3/2017
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
		
		boolean rb = SecondController.getInstance().getRawButton(SecondController.BUTTON_RB);
		
		boolean lb = SecondController.getInstance().getRawButton(SecondController.BUTTON_LB);
		
		if(rb){
			pickUp.set(-0.6);
			
		} else if(lb){
			pickUp.set(0.6);
			
		} else {
			pickUp.set(0);
		}		
	}

	public void testInit() {
	}

	public void testPeriodic() {
	}
}
