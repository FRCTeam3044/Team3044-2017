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

	// Drive
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;

	// Climber
	public CANTalon winchDrive1;
	public CANTalon winchDrive2;

	// Shooter
	public CANTalon shooter;
	public CANTalon shooter2;
	public CANTalon impeller;
	public DigitalInput shooterTacho = new DigitalInput(6);
	public DummyTacho shooterTachoCounter = new DummyTacho(shooterTacho);
	
	// Gear
	public CANTalon GearCANTalon;

	// Pickup
	public CANTalon pickUp;

	public void init() {

		// Drive
		leftFrontDrive = new CANTalon(4);
		leftBackDrive = new CANTalon(5);
		rightFrontDrive = new CANTalon(1);
		rightBackDrive = new CANTalon(0);

		// Climber
		winchDrive1 = new CANTalon(6);
		winchDrive2 = new CANTalon(7);

		// Shooter
		shooter = new CANTalon(8);
		shooter2 = new CANTalon(9);
		impeller = new CANTalon(10);

		
		// Switch These Talons
		
		// Gear
		GearCANTalon = new CANTalon(2);

		// Pickup
		pickUp = new CANTalon(3);

	}
}
