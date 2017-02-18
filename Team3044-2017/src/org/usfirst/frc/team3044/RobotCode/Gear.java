package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Reference.SecondController;


public class Gear {
	//We are using cantalons which controls motors to extend and retract
	//start closed
	//port number might change as Cassie is using 1 & 2 such as 3 & 4 
public CANTalon GearCantalon = new CANTalon(1);
	//public CANTalon GearCantalon = new CANTalon(2)
public DigitalInput limitSwitch1 = new DigitalInput(1);
public DigitalInput limitSwitch2 = new DigitalInput(2);

//Code does not have breaks as I am not sure where to use them
//As of 2/11/17 CANTalons not solenoids

	public void gearAutoInit() {
	}

	public void gearAutoPeriodic() {
	}

	public void gearInit() {
		//called once, when robot starts up. Use limit switches
		//Motor starts off 
				GearCantalon.set(0);
	}

/****************************************************************************
* TeleopPeriod Goal: Use X button to drive motors forward & B button to drive 
* motors backwards 
* Start fully retracted, X = extending until fully extended unless B button is 
* pressed = retracting until fully retracted or X button is pushed once more
* If I button mash X and then B back and forth,
* should be retracting and extending like it's spazzing
* @return
***************************************************************************/
	public void gearTeleopPeriodic() {

/****************************************************************************
* 3 if statement to satisfy the gear Mechanism. Does not use states
* Trial 1
* @return
***************************************************************************/
		//B = in or retracting
		 boolean buttonBPressed = SecondController.getInstance().getRawButton(SecondController.BUTTON_B) ;
		//X = out or extending
		 boolean buttonXPressed = SecondController.getInstance().getRawButton(SecondController.BUTTON_X) ;
		 
		 if(buttonXPressed){
			 GearCantalon.set(1);
		 }else if(buttonBPressed){
			 GearCantalon.set(0);
		 }else{
			 GearCantalon.set(0);
		 }
		 
}

	public void testInit() {
		
	}

	public void testPeriodic() {
	}
}
