package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {

	FirstController controller = FirstController.getInstance();

	double leftAutoSpeed;
	double rightAutoSpeed;
	double leftDriveSpeed;
	double rightDriveSpeed;
	
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;

	private Components comp = Components.getInstance();

	public void driveInit() {
		
		leftFrontDrive = comp.leftFrontDrive;
		leftBackDrive = comp.leftBackDrive;
		rightFrontDrive = comp.rightFrontDrive;
		rightBackDrive = comp.rightBackDrive;

		leftFrontDrive.set(0);
		leftBackDrive.set(0);
		rightFrontDrive.set(0);
		rightBackDrive.set(0);

		leftFrontDrive.enableBrakeMode(true);
		rightFrontDrive.enableBrakeMode(true);
		leftBackDrive.enableBrakeMode(true);
		rightBackDrive.enableBrakeMode(true);
	}
	
	public void disablePID(){
		leftFrontDrive.disable();
		rightFrontDrive.disable();
	}

	public void driveAutoPeriodic() {
		leftAutoSpeed = CommonArea.leftAutoSpeed;
		rightAutoSpeed = -CommonArea.rightAutoSpeed;
	}

	public void driveTeleopPeriodic() {

		if(controller.getLeftY() > 0){
			//Forward
			leftFrontDrive.set(1);
			leftBackDrive.set(1);
			rightFrontDrive.set(1);
			rightBackDrive.set(1);
		}else if(controller.getLeftY() < 0){
			//Reverse
			leftFrontDrive.set(-1);
			leftBackDrive.set(-1);
			rightFrontDrive.set(-1);
			rightBackDrive.set(-1);
		}else if(controller.getLeftX() < 0){
			//Left Shift
			leftFrontDrive.set(-1);
			leftBackDrive.set(1);
			rightFrontDrive.set(1);
			rightBackDrive.set(-1);
		}else if(controller.getLeftX() > 0){
			//Right Shift
			leftFrontDrive.set(1);
			leftBackDrive.set(-1);
			rightFrontDrive.set(-1);
			rightBackDrive.set(1);
		}else if(controller.getRightX() > 0){
			//Clockwise
			leftFrontDrive.set(1);
			leftBackDrive.set(1);
			rightFrontDrive.set(-1);
			rightBackDrive.set(-1);
		}else if(controller.getRightX() < 0){
		//Counter Clockwise
			leftFrontDrive.set(-1);
			leftBackDrive.set(-1);
			rightFrontDrive.set(1);
			rightBackDrive.set(1);
		}
	}

	public void testPeriodic() {
		/*
		 * leftFrontDrive.set(SmartDashboard.getDouble("DB/Slider 0"));
		 * leftBackDrive.set(SmartDashboard.getDouble("DB/Slider 1"));
		 * rightFrontDrive.set(SmartDashboard.getDouble("DB/Slider 2"));
		 * rightBackDrive.set(SmartDashboard.getDouble("DB/Slider 3"));
		 */
		driveTeleopPeriodic();
	}
}
