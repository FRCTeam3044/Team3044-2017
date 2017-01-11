package org.usfirst.frc.team3044.Reference;

import org.usfirst.frc.team3044.Reference.FirstController;
import org.usfirst.frc.team3044.Reference.SecondaryController;

public class CommonArea {
	
	static FirstController firstJoy = FirstController.getInstance();
	static SecondaryController secondaryJoy = SecondaryController.getInstance();
	
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
	
	public static void CommonPeriodic(){
		
		//FirstController
		gateUp = firstJoy.getRawButton(FirstController.BUTTON_Y) || secondaryJoy.getRawButton(FirstController.BUTTON_Y);
		manualFire = firstJoy.getRawButton(FirstController.BUTTON_X) || secondaryJoy.getRawButton(FirstController.BUTTON_X);
		gateDown = firstJoy.getRawButton(FirstController.BUTTON_A) || secondaryJoy.getRawButton(FirstController.BUTTON_A);
		ejectBack = firstJoy.getRawButton(FirstController.BUTTON_B);
		pickRollersOut = firstJoy.getRawButton(FirstController.BUTTON_LB) || secondaryJoy.getRawButton(FirstController.BUTTON_LB);
		pickRollersIn = firstJoy.getRawButton(FirstController.BUTTON_RB) || secondaryJoy.getRawButton(FirstController.BUTTON_RB);
		getShootertoAutoSpeed = firstJoy.getRawButton(FirstController.BUTTON_BACK);
		setSpeed = firstJoy.getRawButton(FirstController.BUTTON_START);
		portcullius = firstJoy.getDPadUp();
		calibrate = firstJoy.getDPadRight();
		chevalDeFrise = firstJoy.getDPadDown();
		pickUpBoulder = firstJoy.getDPadLeft();
		shooterToSpeed = firstJoy.getTriggerLeft() || secondaryJoy.getTriggerLeft();
		autoAlign = firstJoy.getTriggerRight();
		
		//SecondaryController
		UP = secondaryJoy.getRawButton(SecondaryController.BUTTON_Y);
		DOWN = secondaryJoy.getRawButton(SecondaryController.BUTTON_A);
		X1 = false;//secondaryJoy.getRawButton(SecondaryController.BUTTON_Y);
		X2 = false; //secondaryJoy.getRawButton(SecondaryController.BUTTON_X);
		Y2 = false;//secondaryJoy.getRawButton(SecondaryController.BUTTON_A);
		Y1 = secondaryJoy.getRawButton(SecondaryController.BUTTON_B);
		H2 = secondaryJoy.getRawButton(SecondaryController.BUTTON_LB);
		H1 = secondaryJoy.getRawButton(SecondaryController.BUTTON_RB);
		CAL = secondaryJoy.getRawButton(SecondaryController.BUTTON_BACK);
		STOP_TARGETING = secondaryJoy.getRawButton(SecondaryController.BUTTON_START);
		UA_Up = secondaryJoy.getDPadUp();
		LA_Up = secondaryJoy.getDPadRight();
		UA_Down = secondaryJoy.getDPadDown();
		LA_Down = secondaryJoy.getDPadLeft();
		AVALAIBLELT = secondaryJoy.getTriggerLeft();
		AVALAIBLERT = secondaryJoy.getTriggerRight();
	}
}
