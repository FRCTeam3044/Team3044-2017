/* Jacob Claypool
 * 2/6/17
 * FRC Robotics 2017
 * code to allow first controller (xbox) usage of mecanum drive wheels 
 */

package org.usfirst.frc.team3044.RobotCode;

/* imports necessary files for use later in the code for items such as inputs and outputs
* (talons, buttons etc.)
*/

import org.usfirst.frc.team3044.Reference.*;
import com.kauailabs.navx.frc.*;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	FirstController controller = FirstController.getInstance();

	AHRS ahrs;

	// true false statement activated by the start button to invert the mecanum
	// drive controls

	boolean TFlip = false;

	// establishes the 4 motors for drive wheels

	public CANTalon leftFrontDrive;
	public CANTalon leftBackDrive;
	public CANTalon rightFrontDrive;
	public CANTalon rightBackDrive;

	double dashData = SmartDashboard.getNumber("DB/Slider 0", 0.0);

	public Outputs out = Outputs.getInstance();

	/*
	 * establishes controller deadbands for the two sticks so that the motors
	 * only activate once the sticks have passed .2 value on the X and or Y axis
	 */

	public double deadband(double value) {
		if (Math.abs(value) < .2) {
			return 0;
		} else {
			return value;
		}
	}

	public void driveInit() {

		try {
			/* Communicate w/navX-MXP via the MXP SPI Bus. */
			/*
			 * Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or
			 * SerialPort.Port.kUSB
			 */
			/*
			 * See
			 * http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/
			 * for details.
			 */
			ahrs = new AHRS(I2C.Port.kOnboard);
			ahrs.reset();
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
		}

		/*
		 * pulls 4 motors from the output class states (left right front back)
		 * and sets int variable of i to 0
		 */
		leftFrontDrive = out.leftFrontDrive;
		leftBackDrive = out.leftBackDrive;
		rightFrontDrive = out.rightFrontDrive;
		rightBackDrive = out.rightBackDrive;

		// sets the drive motors at the beginning of teleop mode so they are
		// stopped (brake mode)

		leftFrontDrive.set(0);
		leftBackDrive.set(0);
		rightFrontDrive.set(0);
		rightBackDrive.set(0);
		leftFrontDrive.enableBrakeMode(true);
		rightFrontDrive.enableBrakeMode(true);
		leftBackDrive.enableBrakeMode(true);
		rightBackDrive.enableBrakeMode(true);

		// writes errors (failures) in the motor info file or creates it if it
		// is not there

		/*
		 * try { f = new File("/home/lvuser/Motor Information.txt"); if
		 * (!f.exists()) { f.createNewFile(); } fw = new FileWriter(f); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
	}

	public void driveAutoInit() {

	}

	public void driveAutoPeriodic(){
	}

	public void driveTeleopPeriodic() {
		/*
		 * This code is supposed to relate to the NavX This is supposed to get
		 * values from the Gyro on the NavX And print the values to the
		 * Dashboard
		 */

		SmartDashboard.putString("DB/String 1", String.valueOf(ahrs.getYaw()));
		SmartDashboard.putString("DB/String 2", String.valueOf(ahrs.getDisplacementX()));
		SmartDashboard.putString("DB/String 3", String.valueOf(ahrs.getAngle()));

		/*
		 * establishes x as the value of the x axis on the left stick, y as the
		 * value of the y axis for the left stick and r as the x value of the
		 * right stick
		 */
		double x = (-deadband(controller.getLeftX())); // x direction
		double y = deadband(controller.getLeftY()); // y direction
		double r = deadband(controller.getRightX()); // rotation

		// sets the algorithm based on the controller inputs that will later
		// affect the motors

		double v_FrontLeft = r - y - x;
		double v_FrontRight = r + y - x;
		double v_BackLeft = r - y + x;
		double v_BackRight = r + y + x;

		// uses the double, f, to regulate the algorithm for when it later used
		// to power motors

		double f = 1;
		if (Math.abs(v_FrontLeft) > f)
			f = v_FrontLeft;
		if (Math.abs(v_FrontRight) > f)
			f = v_FrontRight;
		if (Math.abs(v_BackLeft) > f)
			f = v_BackLeft;
		if (Math.abs(v_BackRight) > f)
			f = v_BackRight;

		// when start button is held for half a second the controls invert and
		// stops the robot

		if (controller.getRawButton(FirstController.BUTTON_START)) {
			leftFrontDrive.set(0);
			leftBackDrive.set(0);
			rightFrontDrive.set(0);
			rightBackDrive.set(0);
			TFlip = !TFlip;
		}

		// if TFlip is true then it inverts the motor speeds that will be used
		// later on

		if (TFlip == true) {
			v_BackRight = -v_BackRight;
			v_BackLeft = -v_BackLeft;
			v_FrontRight = -v_FrontRight;
			v_FrontLeft = -v_FrontLeft;
		}

		/*
		 * checks to see if the controls should be inverted for the turning then
		 * applies motor speed
		 */
		/*
		 * if (Math.abs(r) != 0) { if (TFlip == true) { leftFrontDrive.set(-r);
		 * leftBackDrive.set(-r); rightFrontDrive.set(-r);
		 * rightBackDrive.set(-r); } else { leftFrontDrive.set(r);
		 * leftBackDrive.set(r); rightFrontDrive.set(r); rightBackDrive.set(r);
		 * } }
		 */
		/*
		 * translational movement applied here based on the algorithm and TFlip
		 * variable established earlier only after it has checked for a right
		 * stick value
		 */

		leftFrontDrive.set((v_FrontLeft / f));
		rightFrontDrive.set((v_FrontRight / f));
		leftBackDrive.set((v_BackLeft / f));
		rightBackDrive.set((v_BackRight / f));

		// prints the motor values to sting 5-8 respectively on the driver
		// station

		SmartDashboard.putNumber("1", Outputs.getInstance().leftFrontDrive.getOutputCurrent());
		SmartDashboard.putNumber("2", Outputs.getInstance().rightFrontDrive.getOutputCurrent());
		SmartDashboard.putNumber("3", Outputs.getInstance().leftBackDrive.getOutputCurrent());
		SmartDashboard.putNumber("4", Outputs.getInstance().rightBackDrive.getOutputCurrent());

	}

	public void testInit() {

	}

	public void testPeriodic() {

	}
}
