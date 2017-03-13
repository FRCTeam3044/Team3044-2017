/**Cassie Companion
 * 3/12/17
 * FRC Team 3044
 * climbs the rope using a winch.
 */

package org.usfirst.frc.team3044.RobotCode;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3044.Reference.Outputs;
import org.usfirst.frc.team3044.Reference.SecondController;

//both move in the same direction, 2 motors
class Climber {
	// change to Y??, boolean button
	SecondController controller = new SecondController();

	public Outputs out = Outputs.getInstance();

	// sets up CANTalons
	public CANTalon winchDrive1;
	public CANTalon winchDrive2;

	public void climberInit() {
		winchDrive1 = out.winchDrive1;
		winchDrive2 = out.winchDrive2;
	}
	
	public void climberAutoInit(){
		
	}

	public void climberAutoPeriodic() {
	}

	public void climberTeleopPeriodic() {
		// Climbs the Robot Up
		boolean yButton = SecondController.getInstance().getRawButton(SecondController.BUTTON_Y);

		// Climbs the Robot Down
		boolean aButton = SecondController.getInstance().getRawButton(SecondController.BUTTON_A);

		if (yButton) {
			winchDrive1.set(.5);
			winchDrive2.set(.5);
		} else if (aButton) {
			winchDrive1.set(1);
			winchDrive2.set(1);
		} else {
			winchDrive1.set(0);
			winchDrive2.set(0);
		}
	}

	public void testPeriodic() {
	}

	public void testInit() {
	}

}
