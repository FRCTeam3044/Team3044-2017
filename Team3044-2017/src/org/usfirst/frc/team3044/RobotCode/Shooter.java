/* Ethan Tabachneck
 * 03/12/17
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
	SecondController secondCon = new SecondController();

	public void hopperPneumatic() {
		if (secondCon.getTriggerRight()) {

			out.hopperPneumaticRelease.set(true);
			out.hopperPneumaticIn.set(false);
		}

		else {

			out.hopperPneumaticRelease.set(false);
			out.hopperPneumaticIn.set(true);
		}
	}
	// creates what the robot does in autonomous
	public void shooterAutoPeriodic() {
	}

	// creates what the robot does in teleop
	public void shooterTeleopPeriodic() {
		hopperPneumatic();

	}

	// the function that runs when test is initiated
	public void testInit() {

	}

	public void testPeriodic() {

	}

	// creates a method for starting the shooter
	
}
