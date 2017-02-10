/*Evan Couchman
 * 2/6/2017
 * FRC Robotics 2017
 * Programs the CANTalon of the intake system (sweeper)
*/



package org.usfirst.frc.team3044.RobotCode;

/*Imports files from the code system and package to pull inputs and allow specialized 
 *functions in the code.
*/
// 
import org.usfirst.frc.team3044.Reference.*;

import com.ctre.CANTalon;
import org.usfirst.frc.team3044.Reference.SecondController;
import org.usfirst.frc.team3044.Reference.Outputs;

public class Pickup {
	
	//defines controller
	//test
	SecondController controller = new SecondController();
	
	//defines CANTalon
	
	public CANTalon pickUp;

	//A deadband sets the tolerance of the joystick
	
	public double deadband(double value) {
		if (Math.abs(value) < .1) {
			return 0;
		} else {
			return value;
		}
	}
	//States the states that the motor is going to be in
	
	public enum state {
		FORWARD, BACKWARD, STOPPED
	}
	//This is the state the robot is in when the code is started
	state pickupState = state.STOPPED;
	
	public void pickupAutoInit() {
	}

	public void pickupAutoPeriodic() {
	}

	public void pickupInit() {
		
		/*Sets the CANTalon being used in this code to the CANTalon that is defined
		 * in the code "Outputs.java"
		 */
		
		pickUp = Outputs.getInstance().pickUp;
	}
	
	//TeleOp phase
	public void pickupTeleopPeriodic() {

		switch (pickupState) {	
		/*This is defining when the motor is stopped then if the joystick 
		 *is pushed one way or the other it will go to a different state
		*/
		case STOPPED:
			if (deadband(controller.getRightY()) > 0) {
				pickupState = state.FORWARD;
			} else if (deadband(controller.getRightY()) < 0) {
				pickupState = state.BACKWARD;
			} else {
				pickUp.set(controller.getRightY());
			}

			break;
			/*This is defining when the motor is going forward then if the joystick 
			  *is released(put to value between -.1 and .1) or pushed the other way it 
			  *will go to a different state
			  */
		case FORWARD:
			if (deadband(controller.getRightY()) < 0) {
				pickupState = state.BACKWARD;
			} else if (deadband(controller.getRightY()) > 0) {
				pickupState = state.STOPPED;
			} else {
				pickUp.set(controller.getRightY());
			}
			break;
		 /*This is defining when the motor is going forward then if the joystick 
		  *is released(put to value between -.1 and .1) or pushed the other way it will 
		  *go to a different state
		  */
		case BACKWARD:

			if (controller.getRightY() > .0) {
				pickupState = state.FORWARD;
			}else if (deadband(controller.getRightY()) > 0) {
				pickupState = state.STOPPED;
			} else {
				pickUp.set(controller.getRightY());
			}

			break;
		}
	}

	public void testInit() {
	}

	public void testPeriodic() {
	}
}

