package org.usfirst.frc.team3044.Reference;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class Outputs {

	private static Outputs instance = null;
	
	private Outputs() {

	}
 
	public static Outputs getInstance() {
		if (instance == null) {
			instance = new Outputs();
		}
		return instance;
	}

	// Drive System
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;
	
	// Climber
	

	// Shooter
	public CANTalon shooter;
	public CANTalon agitator;
	
	// Gear
	public CANTalon gearMotor;
	
	// Pickup
	public CANTalon pickUp;


	public void init() {
		leftFrontDrive = new CANTalon(8);
		leftBackDrive = new CANTalon(2);
		rightFrontDrive = new CANTalon(7);
		rightBackDrive = new CANTalon(9);
		agitator = new CANTalon(3);
		shooter = new CANTalon(1);
		pickUp = new CANTalon(4);
	

	}
}
