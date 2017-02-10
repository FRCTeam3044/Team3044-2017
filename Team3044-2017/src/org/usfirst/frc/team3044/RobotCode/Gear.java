package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3044.Reference.SecondController;

public class Gear {
	public enum GearState  {OPEN, CLOSED, OPENING, CLOSING, UNSURE};
	
	GearState CurrentState = GearState.CLOSED;
	DoubleSolenoid GearSolenoid = new DoubleSolenoid(1, 2);
	//int GearChange;
	//GearChange = 0
	private Position position = GearState.UNSURE;

	public Position getGearState() {
        checkState();
        return position;
    	}
	/****************************************************************************
	 * testing the ability to test the state of the solenoid 
	 * will most likely be deleted better attempt below 
	 * @return
	 ***************************************************************************/

   
      //Tests if is extended.
      
       //true if this solenoid is fully extended, or  false otherwise
     
    /*public boolean isOpen() {
        Object getGearState;
		return getGearState() == getGearState.OPEN;
   	 }
    
      //Tests if is retracted. 
    private Object getGearState() {
		return null;
	} */

	/****************************************************************************
	 * This is to test the state of the solenoid 
	 * @return
	 ***************************************************************************/

	//true if this solenoid is fully extended, or false otherwise
     
    public boolean isOpen() {
        return getGearState() == getGearState.OPEN;
    	}
      
      //true if this solenoid is fully retracted, or false otherwise
     
    public boolean isClosed() {
        return getGearState() == getGearState.CLOSED;
    	}

    //true if in the process of switching from on to off otherwise false
	public boolean isClosing() {
        	return getGearState() == getGearState.CLOSING;
		}

	//true if in the process of switching from off to on otherwise false
	public boolean isOpening() {
        	return getGearState() == getGearState.OPENING;
		}
	
}

	public void gearAutoInit() {
	}

	public void gearAutoPeriodic() {
	}

	public void gearInit() {
	}

	
/****************************************************************************
* TeleopPeriod Goal: Use A button to extend & retract solenoid
* Start fully retracted, A = extending until fully extended unless A button is 
* pressed again = retracting until fully retracted or A button is pushed once more
* constantly checking position of solenoid and might need way to determine
* first push of A vs second push or something. 
* 5 cases: open, closed, opening, closing, unknown
* If i button mash A, should be retracting and extending like it's spazzing
* @return
***************************************************************************/
	public void gearTeleopPeriodic() {
	
		{
			  
			   boolean firstPress = true;
			   boolean inHighGear = false;
			   /* start = button  not pressed. if it is, is it going to be first press? 
			   yes = true	no = false
			  inHighGear = extending */
			
			boolean buttonAPressed = 
				  SecondController.getInstance().getRawButton(SecondController.BUTTON_A) ; 
		 /* Ryan's idea
		  * I don't think this will work as this seems like it should be held down and
		  * dont want that.
		  * if(buttonAPressed){
			   GearSolenoid.set(DoubleSolenoid.Value.kForward);}
			else if (!buttonAPressed)
			GearSolenoid = DoubleSolenoid.Value.kReverse
			   */
		while(true){
			   	if(buttonAPressed && firstPress){//first button
			   		GearSolenoid.set(DoubleSolenoid.Value.kForward);
			   		GearState CurrentState = GearState.OPENING;
			   		//shall set variables to opposite for the next push
			   		inHighGear = !inHighGear;
			   		firstPress = false;
			   }
			   	else if(!buttonAPressed){
			   		firstPress = true; }
		}
		
			   	 if(buttonAPressed && !firstPress or uh firstPress = false)
			   		GearSolenoid.set(DoubleSolenoid.Value.kReverse);
			   	 	GearState CurrentState = GearState.CLOSING;
			   		//shall set variables to opposite for the next push
			   		inHighGear = inHighGear;
			   		firstPress = true;	
			   		
			   		else if(buttonAPressed){
				   		firstPress = false; }
			   	 
			   		
				/* if (GearSolenoid = DoubleSolenoid.Value.kForward) 
					GearChange = 1

				if (GearSolenoid = DoubleSolenoid.Value.kReverse) 
					GearChange = -1
				if 
					*/
	 /********************************************************************************
	  * These are notes on to how to do basic solenoid functions
	  * most likely to be deleted at end
	  ****************************************************************************/
			  /* Do I need to make a variable for when its fully open 
			   * to change to fully closed by value??
			   
			  // DoubleSolenoid GearSolenoid = new DoubleSolenoid(1, 2);
			  // GearSolenoid.set(DoubleSolenoid.Value.kOff);
			  //GearSolenoid.set(DoubleSolenoid.Value.kReverse);
			  
			    https://www.frightprops.com/faq/content/2/51/en/solenoid-valves-the-different-types-explained.html
			    file:///C:/Users/robot/Downloads/FRC_Java_Programming.pdf
			  */ 
			  
			   /* // Variables
	public Solenoid shifter;
	public Joystick joy;

	//IN CONSTRUCTOR PERHAPS?
	shifter = new Solenoid(1);//1 is the port number
	joy = new Joystick(1);


	//IN TELEOP METHOD NOT AUTO!!!
	boolean firstPress = true;
	boolean inHighGear = false;
	// inHighGear = extending

	while(true){
		if(joy.getRawButton(1) && firstPress){//first button
			inHighGear = !inHighGear;
			firstPress = false;
	}
		else if(!joy.getRawButton(1)){
			firstPress = true;
	}
	shifter.set(inHighGear);
	}
			   
		  }
		  
		  
		
	}

	public void testInit() {
		
	}

	public void testPeriodic() {
	}
}
