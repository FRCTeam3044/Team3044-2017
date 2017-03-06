package org.usfirst.frc.team3044.RobotCode;

//allow code to import code from different sources 
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

//
public class Drive {
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	// states buttons and joystick
	private Joystick firstJoy;
	public static int BUTTON_START = 8;
	public static int BUTTON_A = 1;
	public static int BUTTON_B = 2;
	FirstController controller = FirstController.getInstance();
	// establishes the variables for the motor speeds during auto period
	double leftAutoSpeed;
	double rightAutoSpeed;
	double leftDriveSpeed;
	double rightDriveSpeed;

	boolean TFlip = false;
	// establishes the 4 motors for drive wheels
	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;

	File f;
	BufferedWriter bw;
	FileWriter fw;
	int i;
	private Outputs comp = Outputs.getInstance();

	// establishes controller deadbands for the two sticks along with the math
	// to be used for the following code
	public double deadband(double value) {
		if (Math.abs(value) < .1) {
			return 0;
		} else {
			return value;
		}
	}

	public void driveInit() {
		// states 4 motors (left right front back)
		controller.getInstance();
		i = 0;
		leftFrontDrive = comp.leftFrontDrive;
		leftBackDrive = comp.leftBackDrive;
		rightFrontDrive = comp.rightFrontDrive;
		rightBackDrive = comp.rightBackDrive;
		// sets the drive motors at the begining of the code so it is stopped
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
			if (!f.exists()) {
				f.createNewFile();
			}
			fw = new FileWriter(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void driveAutoPeriodic() {
		// leftAutoSpeed = inputs.leftAutoSpeed;
		// rightAutoSpeed = -inputs.rightAutoSpeed;
	}

	public void driveTeleopPeriodic() {

		// establishes x as the value of the x axis on the left stick, y as the
		// value of the y axis for the left stick and r as the x value of the
		// right stick
		double x = -deadband(controller.getLeftX());
		double y = deadband(controller.getLeftY());
		double r = -deadband(controller.getRightX());

		driveMecanum(x, y, r);

	}

	public void driveMecanum(double x, double y, double r) {
		// sets the 4 wheels to always be effected by the math based on the
		// controller inputs
		double v_FrontLeft = r - y - x;
		double v_FrontRight = r + y - x;
		double v_BackLeft = r - y + x;
		double v_BackRight = r + y + x;
		// Regulates speed so it does not go over the motors limits based on the
		// math and a value f which is equal to 1
		double f = 1;
		if (v_FrontLeft > f)
			f = v_FrontLeft;
		if (v_FrontRight > f)
			f = v_FrontRight;
		if (v_BackLeft > f)
			f = v_BackLeft;
		if (v_BackRight > f)
			f = v_BackRight;

		// when start button is held for half a second the controls invert
		if (controller.getRawButton(BUTTON_START)) {
			TFlip ^= true;
		}

		// if r is not 0 then...

		if (r != 0) {

			// test if TFlip is true or false, if false then use default turning
			if (x == 0 && y == 0) {

				if (r > 0) {

					leftFrontDrive.set(-r);
					leftBackDrive.set(-r);
					rightFrontDrive.set(r);
					rightBackDrive.set(r);

				}
				if (r < 0) {

					leftFrontDrive.set(r);
					leftBackDrive.set(r);
					rightFrontDrive.set(-r);
					rightBackDrive.set(-r);

				}

			}

		} else if (TFlip == false) {

			// if r is greater than 0 then double the power of the left
			// motors and half the
			// power of the right motors

			if (r < 0) {
				leftFrontDrive.set((-v_FrontLeft / f) * 2);
				rightFrontDrive.set((-v_FrontRight / f) / 2);
				leftBackDrive.set((-v_BackLeft / f) * 2);
				rightBackDrive.set((-v_BackRight / f) / 2);
			}

			// if r is less than 0 then double the power of the right motors
			// and half
			// the power of the left motors

			else {
				leftFrontDrive.set((-v_FrontLeft / f) / 2);
				rightFrontDrive.set((-v_FrontRight / f) * 2);
				leftBackDrive.set((-v_BackLeft / f) / 2);
				rightBackDrive.set((-v_BackRight / f) * 2);
			}
		}
		// if TFlip is true, invert the turning

		if (TFlip == true) {

			// if r is greater than 0 then double the power of the right
			// motors and half the power of the left motors

			if (r > 0) {
				leftFrontDrive.set(((-v_FrontLeft / f) / 2) * .9);
				rightFrontDrive.set(((-v_FrontRight / f) * 2) * .9);
				leftBackDrive.set(((-v_BackLeft / f) / 2) * .9);
				rightBackDrive.set(((-v_BackRight / f) * 2) * .9);
			}
			// if r is less than 0 then double the power of the left
			// motors and half the power of the right motors
			else {
				leftFrontDrive.set(((-v_FrontLeft / f) * 2) * .9);
				rightFrontDrive.set(((-v_FrontRight / f) / 2) * .9);
				leftBackDrive.set(((-v_BackLeft / f) * 2) * .9);
				rightBackDrive.set(((-v_BackRight / f) / 2) * .9);

			}
		}

		// if the r value is 0 then apply the basic algorythm to the wheels

		else {
			leftFrontDrive.set((-v_FrontLeft / f) * .9);
			rightFrontDrive.set((-v_FrontRight / f) * .9);
			leftBackDrive.set((-v_BackLeft / f) * .9);
			rightBackDrive.set((-v_BackRight / f) * .9);
		}
	}

	public void testPeriodic() {
	}

	public void testInit() {
		try {
			// bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
