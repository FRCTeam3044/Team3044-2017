package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import com.ctre.*;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {

	FirstController controller = FirstController.getInstance();

	public enum state {
		stopped, moveLeftMotor, moveRightMotor, moveBothMotors, manualDrive
	}

	state autoDriveState = state.stopped;
	double leftAutoSpeed;
	double rightAutoSpeed;
	double leftDriveSpeed;
	double rightDriveSpeed;
	double rightDesiredEncoderValue;
	double leftDesiredEncoderValue;
	
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;

	public double encoderTolerance = 50;

	private final double deadband = .2;

	private Components comp = Components.getInstance();

	public boolean isAtDistance(double current, double desired) {
		return (desired + encoderTolerance > current || current > desired
				- encoderTolerance);

	}

	public void driveInit() {
		
		leftFrontDrive = comp.leftFrontDrive;
		leftBackDrive = comp.leftBackDrive;
		rightFrontDrive = comp.rightFrontDrive;
		rightBackDrive = comp.rightBackDrive;

		leftFrontDrive.set(0);
		leftBackDrive.set(0);
		rightFrontDrive.set(0);
		rightBackDrive.set(0);

		leftFrontDrive.setPosition(0);
		rightFrontDrive.setPosition(0);

		leftFrontDrive.enableBrakeMode(true);
		rightFrontDrive.enableBrakeMode(true);
		leftBackDrive.enableBrakeMode(true);
		rightBackDrive.enableBrakeMode(true);
		/*
		leftFrontDrive.setPIDSourceType(PIDSourceType.kRate);
		rightFrontDrive.setPIDSourceType(PIDSourceType.kRate);
		leftFrontDrive.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
		rightFrontDrive.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
		
		rightFrontDrive.setInverted(true);*/
		
		
	}
	
	double p = 0, i = 0, d = 0;
	
	public void DriveRightPID(double speed){
		if(!rightFrontDrive.isEnabled()){
			rightFrontDrive.enable();
			
		}
		p = SmartDashboard.getNumber("P",0);
		i = SmartDashboard.getNumber("I",0);
		d = SmartDashboard.getNumber("d",0);
		rightFrontDrive.setPID(p, i, d);
		rightFrontDrive.setSetpoint(speed);
		rightBackDrive.setSetpoint(rightFrontDrive.getOutputVoltage());
		
		
	}
	public void DriveLeftPID(double speed){
		if(!leftFrontDrive.isEnabled()){
			leftFrontDrive.enable();
		}
		p = SmartDashboard.getNumber("P",0);
		i = SmartDashboard.getNumber("I",0);
		d = SmartDashboard.getNumber("d",0);
		
		leftFrontDrive.setPID(p, i, d);
		leftFrontDrive.setSetpoint(speed);
		leftBackDrive.setSetpoint(rightBackDrive.getOutputVoltage());
	}
	
	public void disablePID(){
		leftFrontDrive.disable();
		rightFrontDrive.disable();
	}

	public void driveAutoPeriodic() {
		boolean movexFeet = CommonArea.movexFeet;

		leftAutoSpeed = CommonArea.leftAutoSpeed;
		rightAutoSpeed = -CommonArea.rightAutoSpeed;

		double rightCurrentEncoderValue = rightFrontDrive.getAnalogInRaw();
		double leftCurrentEncoderValue = leftFrontDrive.getAnalogInRaw();
		SmartDashboard.putDouble("DB/String 5", rightCurrentEncoderValue);
		SmartDashboard.putDouble("DB/String 6", leftCurrentEncoderValue);
		boolean leftOnTarget = isAtDistance(leftCurrentEncoderValue,
				leftDesiredEncoderValue);
		boolean rightOnTarget = isAtDistance(rightCurrentEncoderValue,
				rightDesiredEncoderValue);

		switch (autoDriveState) {
		case stopped:
			if (!movexFeet) {
				autoDriveState = state.manualDrive;

			} else if (!rightOnTarget && leftOnTarget) {
				rightFrontDrive.set(rightAutoSpeed);
				rightBackDrive.set(rightAutoSpeed);
				autoDriveState = state.moveRightMotor;

			} else if (!leftOnTarget && rightOnTarget) {
				leftFrontDrive.set(leftAutoSpeed);
				leftBackDrive.set(leftAutoSpeed);
				autoDriveState = state.moveLeftMotor;

			} else if (!leftOnTarget && !rightOnTarget) {
				leftFrontDrive.set(leftAutoSpeed);
				leftBackDrive.set(leftAutoSpeed);
				rightFrontDrive.set(rightAutoSpeed);
				rightBackDrive.set(rightAutoSpeed);
				autoDriveState = state.moveBothMotors;
			} else if (leftOnTarget && rightOnTarget) {
				CommonArea.atDistance = true;
			}
			break;
		case moveLeftMotor:
			if (!movexFeet) {
				autoDriveState = state.manualDrive;

			} else if (rightOnTarget && leftOnTarget) {
				leftFrontDrive.set(0);
				leftBackDrive.set(0);
				rightFrontDrive.set(0);
				rightBackDrive.set(0);
				autoDriveState = state.stopped;

			} else if (!rightOnTarget && !leftOnTarget) {
				leftFrontDrive.set(leftAutoSpeed);
				leftBackDrive.set(leftAutoSpeed);
				rightFrontDrive.set(rightAutoSpeed);
				rightBackDrive.set(rightAutoSpeed);
				autoDriveState = state.moveBothMotors;
			}
			break;
		case moveRightMotor:
			if (!movexFeet) {
				autoDriveState = state.manualDrive;

			} else if (rightOnTarget && leftOnTarget) {
				leftFrontDrive.set(0);
				leftBackDrive.set(0);
				rightFrontDrive.set(0);
				rightBackDrive.set(0);
				autoDriveState = state.stopped;

			} else if (!rightOnTarget && !leftOnTarget) {
				leftFrontDrive.set(leftAutoSpeed);
				leftBackDrive.set(leftAutoSpeed);
				rightFrontDrive.set(rightAutoSpeed);
				rightBackDrive.set(rightAutoSpeed);
				autoDriveState = state.moveBothMotors;
			}
			break;
		case moveBothMotors:
			if (!movexFeet) {
				autoDriveState = state.manualDrive;

			} else if (rightOnTarget && leftOnTarget) {
				leftFrontDrive.set(0);
				leftBackDrive.set(0);
				rightFrontDrive.set(0);
				rightBackDrive.set(0);
				autoDriveState = state.stopped;

			} else if (!rightOnTarget && leftOnTarget) {
				rightFrontDrive.set(rightAutoSpeed);
				rightBackDrive.set(rightAutoSpeed);
				leftFrontDrive.set(0);
				leftBackDrive.set(0);
				autoDriveState = state.moveRightMotor;

			} else if (!leftOnTarget && rightOnTarget) {
				leftFrontDrive.set(leftAutoSpeed);
				leftBackDrive.set(leftAutoSpeed);
				rightFrontDrive.set(0);
				rightBackDrive.set(0);
				autoDriveState = state.moveLeftMotor;
			}
			break;
		case manualDrive:
			if (movexFeet) {
				if (leftDesiredEncoderValue == 0) {
					leftFrontDrive.set(0);
					leftBackDrive.set(0);
				}
				if (rightDesiredEncoderValue == 0) {
					rightFrontDrive.set(0);
					rightBackDrive.set(0);
				}
				autoDriveState = state.stopped;
			} else {
				if (Math.abs(leftAutoSpeed) < deadband) {
					leftAutoSpeed = 0;
				}
				if (Math.abs(rightAutoSpeed) < deadband) {
					rightAutoSpeed = 0;
				}

				leftFrontDrive.set(leftAutoSpeed);
				leftBackDrive.set(leftAutoSpeed);
				rightFrontDrive.set(rightAutoSpeed);
				rightBackDrive.set(rightAutoSpeed);
			}
			break;
		}
	}

	public void driveTeleopPeriodic() {
		SmartDashboard.putString("DB/String 4", String.valueOf(leftFrontDrive.getOutputCurrent()));
		if (!CommonArea.isManualDrive) {
			leftDriveSpeed = CommonArea.leftDriveSpeed;
			rightDriveSpeed = -CommonArea.rightDriveSpeed;

		} else {
			leftDriveSpeed = (controller.getLeftY());
			rightDriveSpeed = (-controller.getRightY());

			/*
			 * if (controller.getTriggerLeft() > .5) { leftDriveSpeed =
			 * leftDriveSpeed * .5; rightDriveSpeed = rightDriveSpeed * .5; }
			 */
		}

		if (Math.abs(leftDriveSpeed) < deadband) {
			leftDriveSpeed = 0;
		}
		if (Math.abs(rightDriveSpeed) < deadband) {
			rightDriveSpeed = 0;
		}
		leftFrontDrive.set(leftDriveSpeed);
		leftBackDrive.set(leftDriveSpeed);
		rightFrontDrive.set(rightDriveSpeed);
		rightBackDrive.set(rightDriveSpeed);
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
