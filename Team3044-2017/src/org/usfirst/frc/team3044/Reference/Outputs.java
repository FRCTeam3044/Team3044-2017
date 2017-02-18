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
	public CANTalon GearCantalon;
	
	// Pickup
	public CANTalon pickUp;


	public void init() {
		leftFrontDrive = new CANTalon(4);//correct
		leftBackDrive = new CANTalon(5);//correct
		rightFrontDrive = new CANTalon(1);//correct
		rightBackDrive = new CANTalon(0);//correct
		
		GearCantalon = new CANTalon(2);
		
		agitator = new CANTalon(3);
		
		shooter = new CANTalon(1);
		
		pickUp = new CANTalon(4);
		
	

	}
}
