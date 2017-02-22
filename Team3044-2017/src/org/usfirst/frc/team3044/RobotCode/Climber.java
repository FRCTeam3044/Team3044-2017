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

	// sets up CANTalons
	public CANTalon winchDrive1 = new CANTalon(6);
	public CANTalon winchDrive2 = new CANTalon(7);

	public void climberInit() {

	}

	public void climberAutPeriodic() {

	}

	public void climberTeleopPeriodic() {
		//Climbs the Robot Up
		boolean yButton = SecondController.getInstance().getRawButton(SecondController.BUTTON_Y);
		
		//Climbs the Robot Down
		boolean aButton = SecondController.getInstance().getRawButton(SecondController.BUTTON_A);

		if(yButton){
			winchDrive1.set(1);
			winchDrive2.set(1);
		}else if(aButton){
			winchDrive1.set(-1);
			winchDrive2.set(-1);
		}else{
			winchDrive1.set(0);
			winchDrive2.set(0);
		}
	}

	public void testPeriodic() {
	}

	public void testInit() {
	}

}