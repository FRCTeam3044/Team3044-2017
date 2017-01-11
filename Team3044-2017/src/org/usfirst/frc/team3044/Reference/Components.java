package org.usfirst.frc.team3044.Reference;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class Components {

	private static Components instance = null;
	
	private Components() {

	}
 
	public static Components getInstance() {
		if (instance == null) {
			instance = new Components();
		}
		return instance;
	}

	// Drive System
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;

	// Shooter
	public CANTalon topShooter;
	public CANTalon botShooter;

	// Defense
	public CANTalon daltonArm;
		


	public void init() {
		leftFrontDrive = new CANTalon(1);
		leftBackDrive = new CANTalon(2);
		rightFrontDrive = new CANTalon(3);
		rightBackDrive = new CANTalon(4);

		leftFrontDrive.setFeedbackDevice(FeedbackDevice.AnalogEncoder);
		rightFrontDrive.setFeedbackDevice(FeedbackDevice.AnalogEncoder);

	}
}
