package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import org.usfirst.frc.team3044.robot.FirstController;
import org.usfirst.frc.team3044.robot.Robot.state;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber {
	FirstController controller = FirstController.getInstance();

	// states states
	public enum state {
		STOPPED, MOVINGDOWN, MOVINGUP
	}

	// climber starts the match stopped
	state climberState = state.STOPPED;
	double winchDriveSpeed;
	
	// sets up CANTalon
	public CANTalon winchDrive = new CANTalon(1);

	public void climberInit() {

	}

	public void climberAutPeriodic() {

	}

	public void climberTeleopPeriodic() {

		/*
		 * creates the variables for the left and right triggers, makes them
		 * always positive, and creates a variable for if both are pressed
		 */

		double leftTrig = Math.abs(controller.firstJoy.getRawAxis(2));
		double rightTrig = Math.abs(controller.firstJoy.getRawAxis(3));
		boolean bothPressed = false;

		switch (climberState) {

		case STOPPED:
			// if the only the left trigger is pressed, the robot descends
			if (leftTrig > 0 && !bothPressed) {
				climberState = state.MOVINGDOWN;
				winchDriveSpeed = leftTrig;
			}

			// if only the right trigger is pressed, the robot climbs
			else if (rightTrig > 0 && !bothPressed) {
				climberState = state.MOVINGUP;
				winchDriveSpeed = rightTrig;
			}

			SmartDashboard.putString("DB/String 1", "STOPPED");

			break;

		case MOVINGDOWN:
			// if both triggers are pressed, the climber stops
			if (leftTrig > 0 && rightTrig > 0) {
				bothPressed = true;
				climberState = state.STOPPED;
				winchDriveSpeed = 0;
			}
			// if ONLY the right trigger is pressed, the robot climbs
			else if (rightTrig > 0 && !bothPressed) {
				climberState = state.MOVINGUP;
				winchDriveSpeed = rightTrig;
			}
			// if BOTH triggers are pressed, the climber stops
			else if (rightTrig == 0 && leftTrig == 0) {
				climberState = state.STOPPED;
				winchDriveSpeed = 0;

			}
			SmartDashboard.putString("DB/String 1", "MOVINGDOWN");

			break;

		case MOVINGUP:
			// if BOTH triggers are pressed, the climber stops
			if (leftTrig > 0 && rightTrig > 0) {
				bothPressed = true;
				climberState = state.STOPPED;
				winchDriveSpeed = 0;
			}
			// if the only the left trigger is pressed, the robot descends
			else if (leftTrig > 0 && !bothPressed) {
				climberState = state.MOVINGDOWN;
				winchDriveSpeed = leftTrig;
			}

			// if neither trigger is pressed, the climber stops (duh)
			else if (rightTrig == 0 && leftTrig == 0) {
				climberState = state.STOPPED;
				winchDriveSpeed = 0;

			}

			SmartDashboard.putString("DB/String 1", "MOVINGUP");

			break;
		}

	}

	public void testPeriodic() {
	}

	public void testInit() {
	}

}
