/**Cassie Companion
 * 9/6/17
 * FRC Team 3044
 * climbs the rope using a winch.
 */

package org.usfirst.frc.team3044.RobotCode;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Reference.SecondController;

//both move in the same direction, 2 motors
class Climber {
	// change to Y??, boolean button
	SecondController controller = new SecondController();
	public int winchPower = 1;

	// creates states to use in the cases
	public enum state {
		STOPPED, MOVINGUP
	}

	// climber starts the match stopped
	state climberState = state.STOPPED;

	// sets up CANTalons
	public CANTalon winchDrive1 = new CANTalon(1);
	public CANTalon winchDrive2 = new CANTalon(2);

	public void climberInit() {

	}

	public void climberAutPeriodic() {

	}

	public void climberTeleopPeriodic() {
		boolean yButton = SecondController.getInstance().getRawButton(SecondController.BUTTON_Y);

		// checks to see if Y is not pressed
		if (!yButton) {
			//if Y is not pressed, the motor does't move
			winchPower = 0;
		}

		switch (climberState) {

		case STOPPED:
			// displays current state on SmartDashboard
			SmartDashboard.putString("DB/String 1", "STOPPED");

			// check if y is pressed
			if (yButton) {
				//if Y is pressed it moves up
				climberState = state.MOVINGUP;
				winchPower = 1;
			}

			break;

		case MOVINGUP:
			// displays current state on SmartDashboard
			SmartDashboard.putString("DB/String 1", "MOVINGUP");

			// if Y isn't pressed it doesn't move
			if (!yButton) {
				climberState = state.STOPPED;
				// both motors stop
				winchPower = 0;

			}

			break;
		}
		//sets 
		winchDrive1.set(winchPower);
		winchDrive2.set(winchPower);
	}

	public void testPeriodic() {
	}

	public void testInit() {
	}

}