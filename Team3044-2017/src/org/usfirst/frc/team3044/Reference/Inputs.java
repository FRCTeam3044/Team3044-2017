package org.usfirst.frc.team3044.Reference;

import org.usfirst.frc.team3044.Reference.FirstController;
import org.usfirst.frc.team3044.Reference.SecondController;

import edu.wpi.first.wpilibj.DigitalInput;

public class Inputs {
	
	static FirstController firstJoy = FirstController.getInstance();
	static SecondController secondJoy = SecondController.getInstance();
	
	public static DigitalInput limitSwitchOut = new DigitalInput(4);
	public static DigitalInput limitSwitchIn = new DigitalInput(5);

	
	//Drive 
	public static double leftDriveSpeed;
	public static double rightDriveSpeed;
	public static double leftAutoSpeed;
	public static double rightAutoSpeed;
	public static double leftDesiredEncoderValue;
	public static double rightDesiredEncoderValue;
	public static boolean movexFeet = false;
	public static boolean atDistance = false;
	public static boolean isManualDrive = true;

	//Shooter
	public static double shooterVisionTopSpeed;
	public static double shooterVisionBotSpeed;
	public static boolean aimFlag = false;
	public static boolean shooterMotorFlag = false;
	public static boolean shootFlag = false; 
	public static boolean isShot = false;
	
	//Gate
	public static boolean chevalFlag = false;
	public static boolean gateCalibrated = false;
	public static boolean pickUpBoulder = false;
	
	//PickUp
	public static boolean portcullisFlag = false;
	public static boolean pickUpDrawBridgeFlag = false;
	
	//Arm
	public static boolean armDrawBridgeFlag = false;
	public static boolean armCalibrated = false;
	
	//Vision
	public static boolean isTargetSeen = false;
	public static boolean isAligned = false;
	public static boolean isUpToSpeed = false;
	public static boolean autoShot = false;
	public static int angleToTarget;
	public static double distanceFromTarget;
	
	//FirstController
	public static boolean gateUp;
	public static boolean manualFire;
	public static boolean gateDown;
	public static boolean autoAlign;
	public static boolean pickRollersOut;
	public static boolean pickRollersIn;
	public static boolean getShootertoAutoSpeed;
	public static boolean setSpeed;
	public static boolean portcullius;
	public static boolean calibrate;
	public static boolean chevalDeFrise;
	public static boolean AVALIABLEDLEFT;
	public static boolean shooterToSpeed;
	public static boolean shooterInit;
	
	//SecondaryController
	public static boolean X1;
	public static boolean X2;
	public static boolean Y2;
	public static boolean Y1;
	public static boolean H2;
	public static boolean H1;
	public static boolean CAL;
	public static boolean STOP_TARGETING;
	public static boolean UA_Up;
	public static boolean LA_Up;
	public static boolean UA_Down;
	public static boolean LA_Down;
	public static boolean AVALAIBLELT;
	public static boolean AVALAIBLERT;
	public static boolean ejectBack;
	public static boolean UP;
	public static boolean DOWN;
	
	public static void inputsPeriodic(){
		
		//FirstController
		secondJoy.getLeftX();
		secondJoy.getLeftY();
		secondJoy.getRightX();
		
		//SecondaryController
		UP = secondJoy.getRawButton(SecondController.BUTTON_Y);
		DOWN = secondJoy.getRawButton(SecondController.BUTTON_A);
		X1 = false;//secondaryJoy.getRawButton(SecondaryController.BUTTON_Y);
		X2 = false; //secondaryJoy.getRawButton(SecondaryController.BUTTON_X);
		Y2 = false;//secondaryJoy.getRawButton(SecondaryController.BUTTON_A);
		Y1 = secondJoy.getRawButton(SecondController.BUTTON_B);
		H2 = secondJoy.getRawButton(SecondController.BUTTON_LB);
		H1 = secondJoy.getRawButton(SecondController.BUTTON_RB);
		CAL = secondJoy.getRawButton(SecondController.BUTTON_BACK);
		STOP_TARGETING = secondJoy.getRawButton(SecondController.BUTTON_START);
		UA_Up = secondJoy.getDPadUp();
		LA_Up = secondJoy.getDPadRight();
		UA_Down = secondJoy.getDPadDown();
		LA_Down = secondJoy.getDPadLeft();
		AVALAIBLELT = secondJoy.getTriggerLeft();
		AVALAIBLERT = secondJoy.getTriggerRight();
	}
}
