package org.usfirst.frc.team3044.RobotCode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.usfirst.frc.team3044.Reference.*;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Jacob_Drive {
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	//states buttons and joystick
	private Joystick firstJoy;
	public static int BUTTON_START = 8;
	public static int BUTTON_A = 1;
	public static int BUTTON_B = 2;
	FirstController controller = FirstController.getInstance();
	//establishes the variables for the motor speeds during auto period
	double leftAutoSpeed;
	double rightAutoSpeed;
	double leftDriveSpeed;
	double rightDriveSpeed;
	
	boolean TFlip = false;
	//establishes the 4 motors for drive wheels	
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;
	
	
	File f;
	BufferedWriter bw;
	FileWriter fw;
	int i;
	private Components comp = Components.getInstance();
	//establishes controller deadbands for the two sticks along with the math to be used for the following code
	public double deadband(double value){
		if(Math.abs(value) < .1){
			return 0;
		}
		else{
			return value;
		}
	}
		
	public void driveInit() {
		//states 4 motors (left right front back)
		controller.getInstance();
		i = 0;
		leftFrontDrive = comp.leftFrontDrive;
		leftBackDrive = comp.leftBackDrive;
		rightFrontDrive = comp.rightFrontDrive;
		rightBackDrive = comp.rightBackDrive;
		//sets the drive motors at the begining of the code so it is stopped
		leftFrontDrive.set(0);
		leftBackDrive.set(0);
		rightFrontDrive.set(0);
		rightBackDrive.set(0);
		leftFrontDrive.enableBrakeMode(true);
		rightFrontDrive.enableBrakeMode(true);
		leftBackDrive.enableBrakeMode(true);
		rightBackDrive.enableBrakeMode(true);
		
		try {
    		f = new File("/home/lvuser/Motor Information.txt");
    		if(!f.exists()){
    			f.createNewFile();
    		}
			fw = new FileWriter(f);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void driveAutoPeriodic() {
		leftAutoSpeed = CommonArea.leftAutoSpeed;
		rightAutoSpeed = -CommonArea.rightAutoSpeed;
	}

	public void driveTeleopPeriodic() {
	
		Servo gearServo = new Servo(1);
		gearServo.set(0);

		//establishes x as the value of the x axis on the left stick, y as the value of the y axis for the left stick and r as the x value of the right stick
		double x = -deadband(controller.getLeftX());
		double y = deadband(controller.getLeftY());
		double r = deadband(controller.getRightX());
		//sets the 4 wheels to always be effected by the math based on the controller inputs
		double v_FrontLeft = r-y-x;
		double v_FrontRight = r+y-x;
		double v_BackLeft = r-y+x;
		double v_BackRight = r+y+x;
		//Regulates speed so it does not go over the motors limits based on the math and a value f which is equal to 1 
		double f = 1;
		if(Math.abs(v_FrontLeft) > f) f = v_FrontLeft;
		if(Math.abs(v_FrontRight) > f) f = v_FrontRight;
		if(Math.abs(v_BackLeft) > f) f = v_BackLeft;
		if(Math.abs(v_BackRight) > f) f = v_BackRight;
		
		//when start button is held for half a second the controls invert
		if(controller.getRawButton(BUTTON_START)){
			TFlip ^= true;
		}
		//states that when the right stick is used turn based on X value
		if(Math.abs(r) != 0){
			if(TFlip == true){
				leftFrontDrive.set(-r);
				leftBackDrive.set(-r);
				rightFrontDrive.set(-r);
				rightBackDrive.set(-r);
			}
			else{
				leftFrontDrive.set(r);
				leftBackDrive.set(r);
				rightFrontDrive.set(r);
				rightBackDrive.set(r);	
			}
		}
		//translational movement applied here
		else{	
			if(TFlip == false){
				leftFrontDrive.set(v_FrontLeft / f);
				rightFrontDrive.set(v_FrontRight / f);
				leftBackDrive.set(v_BackLeft / f);
				rightBackDrive.set(v_BackRight / f);
			}
		}
		//A button opens servos for gear
		if(controller.getRawButton(BUTTON_A)){
			gearServo.set (1);	
		}
		//B button closes servos for gear
		if(controller.getRawButton(BUTTON_B)){
			gearServo.set(0);			
		}
		
		SmartDashboard.putString("DB/String 5", String.valueOf("Left Front " + Components.getInstance().leftFrontDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 6", String.valueOf("Right Front " + Components.getInstance().rightFrontDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 7", String.valueOf("Left Back " + Components.getInstance().leftBackDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 8", String.valueOf("Right Back " + Components.getInstance().rightBackDrive.getOutputCurrent()));
		i++;
	}

	public void testPeriodic() {
	}
	
	public void testInit(){
		try {
			//bw.close();
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
