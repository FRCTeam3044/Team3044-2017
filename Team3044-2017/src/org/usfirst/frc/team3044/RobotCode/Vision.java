/*Colin MacDonald
 * Implements the GripPipeline and VisionProcessingThread to find the gear deposit station
 * Maximum range of 11 feet
 * Minimum range of 1 foot
*/
package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.*;
import org.usfirst.frc.team3044.RobotCode.*;
import com.ctre.CANTalon;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
//import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Vision extends IterativeRobot {

	public static AxisCamera FrontCamera;
	public static UsbCamera ClimberCamera;

	// Sets the image width and height
	private static final int IMG_WIDTH = 320; // Half is 160
	private static final int IMG_HEIGHT = 240;

	// Built in drive function could be used, or our own functions
	private RobotDrive drive;
	public static  Object imgLock = new Object();

	// Declares the visionThread
	private VisionProcessingThread visionThread;

	// Sets a variable drive speed
	public static double drive_speed = .15;

	int state = 0;

	@Override
	public void robotInit() {
		// CAMERA_IP
		FrontCamera = CameraServer.getInstance().addAxisCamera("10.30.44.11");
		
		ClimberCamera = CameraServer.getInstance().startAutomaticCapture("ClimberCamera", "/dev/cam2");
				
		//ClimberCamera.setResolution(320, 240);
		
		state = 0;
		// Sets the camera resolution to the pre-defined size
		FrontCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);

		// Starts the visionThread
		visionThread = new VisionProcessingThread(Vision.FrontCamera);
		// visionThread = new VisionProcessingThread(Vision.ClimberCamera);
		visionThread.start();

	}

	final int ALIGN = 0;
	final int APPROACH = 1;
	final int FINISH = 2;

	@Override
	public void autonomousPeriodic() {

		// X is is left and right translational, Y is forwards and backwards, R is rotational.
		double x = 0, y = 0, r = 0;
		SmartDashboard.putString("DB/String 8", String.valueOf(state));

		synchronized (imgLock)
		{
		
		// Start of the cases
		switch (state) {

		// Aligns the robot and does not move forward
		case ALIGN:

			// If the center is to the right, translate to the right at a scaled speed
			if (visionThread.center_of_board < 150) {
				x = -(Math.abs(visionThread.center_of_board - 160) / 40) * .6;
				x -= .1;

				// If the center is to the left, translate to the left at a scaled speed
			} else if (visionThread.center_of_board > 170) {
				x = (Math.abs(visionThread.center_of_board - 160) / 40) * .6;
				x += .1;

				// If the center of the board is in the center of the image, don't translate
			} else {
				x = 0;

			}
			// If the difference in area is more than 50, correct it.
			if (Math.abs(visionThread.area_difference) > 50) {

				// Scaled rotation - the more tilted the target is, the faster the robot rotates
				r = (visionThread.area_difference / 350) * .2;

			} else
				r = 0;

			// If the robot is aligned, move onto the next state
			if (visionThread.center_of_board > 150 && visionThread.center_of_board < 170 && Math.abs(visionThread.area_difference) <= 50) {
				state++;
			}

			if ((VisionProcessingThread.pipeline.filterContoursOutput().size() < 2)) {
				x = 0;
				y = 0;
				r = 0;
			}

			break;

		case APPROACH:

			// If the area difference is less than 50, the center of the board is between 160 and 170, and the rectangle area is less than 2000, drive forward
			if (Math.abs(visionThread.area_difference) <= 50 && visionThread.center_of_board > 160 && visionThread.center_of_board < 170 && visionThread.rect1_area < 2000) {
				y = ((2000 - visionThread.rect1_area) / 1500) * .3;
				y += .1;
			} else {

				// If they are not true, don't drive forward
				y = 0;

				// Translate and rotate at a decreased speed than the first case

				if (visionThread.center_of_board < 150) {
					x = -(Math.abs(visionThread.center_of_board - 160) / 40) * .4;

					// If the center is to the left, translate to the left at a scaled speed
				} else if (visionThread.center_of_board > 170) {
					x = (Math.abs(visionThread.center_of_board - 160) / 40) * .4;

					// If the center of the board is in the center of the image, don't translate
				} else {
					x = 0;
				}
				// If the difference in area is more than 50, correct it.
				if (Math.abs(visionThread.area_difference) > 50) {

					// Scaled rotation - the more tilted the target is, the faster the robot rotates
					r = (visionThread.area_difference / 350) * .2;

				} else
					r = 0;
			}
			
			if ((VisionProcessingThread.pipeline.filterContoursOutput().size() < 2)) {
				x = 0;
				y = 0;
				r = 0;
			}

			// Limits on the translational, rotational, and forward/backward movement variables, adjusted to the decreased movement
			if (x > .4)
				x = .4;
			if (x < -.4)
				x = -.4;
			if (r > .2)
				r = .2;
			if (r < -.2)
				r = -.2;
			if (y > .3)
				y = .3;
			if (y < 0)
				y = 0;

			break;

		// Uses time to make final approach to gear deposit station, releases the gear, and then backs away to do other things
		case FINISH:

			break;
		}
		}
		// Will be changed. Currently, if the A button is pressed, drive
		// forwards
		// and continue adjusting
		/*
		 * if (SecondController.getInstance().getRawButton(SecondController.
		 * BUTTON_A)) { y = ((2000 - visionThread.rect1_area) / 1500) * .2; }
		 * else { y = 0; }
		 * 
		 * // If the area difference is less than 50, the center of the board is
		 * // between 160 and 170, and the rectangle area is less than 2000,
		 * drive // forward if (visionThread.area_difference < 50 &&
		 * visionThread.center_of_board > 160 && visionThread.center_of_board <
		 * 170 && visionThread.rect1_area < 2000) { y = ((2000 -
		 * visionThread.rect1_area) / 1500) * .2; } else { y = 0; }
		 * 
		 * 
		 * // Must be 2 if
		 * ((VisionProcessingThread.pipeline.filterContoursOutput().size() < 2))
		 * { x = 0; y = 0; }
		 */
		// if(visionThread.center_of_board > 195 || visionThread.center_of_board
		// < 110) x = 0;

		// Limits on the translational, rotational, and forward/backward movement variables
		if (x > .6)
			x = .6;
		if (x < -.5)
			x = -.5;
		if (r > .2)
			r = .2;
		if (r < -.2)
			r = -.2;
		if (y > .2)
			y = .2;
		if (y < 0)
			y = 0;

		// Drive functions that take X, Y and R as inputs. Will later be modified to be helper functions in a different class
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

		Outputs.getInstance().leftFrontDrive.set(v_FrontLeft / f);
		Outputs.getInstance().rightFrontDrive.set(v_FrontRight / f);
		Outputs.getInstance().leftBackDrive.set(v_BackLeft / f);
		Outputs.getInstance().rightBackDrive.set(v_BackRight / f);

		// My old (and bad) basic vision code

		/*
		 * // If the left area is bigger than the right, if
		 * (visionThread.rect1_area < visionThread.rect2_area + 150) { // Should
		 * turn to the left
		 * Outputs.getInstance().leftFrontDrive.set(-drive_speed);
		 * Outputs.getInstance().leftBackDrive.set(-drive_speed);
		 * Outputs.getInstance().rightFrontDrive.set(-drive_speed);
		 * Outputs.getInstance().rightBackDrive.set(-drive_speed);
		 * SmartDashboard.putString("DB/String 4", "Turn left"); }
		 * 
		 * // If the right area is bigger than the left, else if
		 * (visionThread.rect1_area + 150 > visionThread.rect2_area) { // Should
		 * turn to the right
		 * Outputs.getInstance().leftFrontDrive.set(drive_speed);
		 * Outputs.getInstance().leftBackDrive.set(drive_speed);
		 * Outputs.getInstance().rightFrontDrive.set(drive_speed);
		 * Outputs.getInstance().rightBackDrive.set(drive_speed);
		 * SmartDashboard.putString("DB/String 4", "Turn right");
		 * 
		 * }
		 * 
		 * //If nothing seen, stop robot. THIS WILL NEED TO BE CHANGED in the
		 * future, as it should actively look for a target else if
		 * ((VisionProcessingThread.pipeline.filterContoursOutput().size() <
		 * 2)){ Outputs.getInstance().leftFrontDrive.set(0);
		 * Outputs.getInstance().leftBackDrive.set(0);
		 * Outputs.getInstance().rightFrontDrive.set(0);
		 * Outputs.getInstance().rightBackDrive.set(0);
		 * SmartDashboard.putString("DB/String 4", "Don't Turn");
		 * 
		 * }
		 * 
		 * // If the areas are equal, and therefore the robot is oriented
		 * correctly, begin left-right orientation else if
		 * (visionThread.rect1_area == visionThread.rect2_area) {
		 * 
		 * // if (visionThread.rect1_x > rect1_x_should) { // Move left
		 * Outputs.getInstance().leftFrontDrive.set(-drive_speed);
		 * Outputs.getInstance().rightBackDrive.set(-drive_speed);
		 * Outputs.getInstance().rightFrontDrive.set(drive_speed);
		 * Outputs.getInstance().leftBackDrive.set(drive_speed); } else if
		 * (visionThread.rect1_x < rect1_x_should) { // Move right
		 * Outputs.getInstance().leftFrontDrive.set(drive_speed);
		 * Outputs.getInstance().rightBackDrive.set(drive_speed);
		 * Outputs.getInstance().rightFrontDrive.set(-drive_speed);
		 * Outputs.getInstance().leftBackDrive.set(-drive_speed); } }
		 */

		// Puts various values in the SmartDashboard for testing, will later be removed
		SmartDashboard.putString("DB/String 0", "rect1_x: " + String.valueOf(visionThread.rect1_x));
		SmartDashboard.putString("DB/String 1", "rect1_y: " + String.valueOf(visionThread.rect1_y));
		SmartDashboard.putString("DB/String 2", "rect1_area: " + String.valueOf(visionThread.rect1_area));
		SmartDashboard.putString("DB/String 5", "rect2_x: " + String.valueOf(visionThread.rect2_x));
		SmartDashboard.putString("DB/String 6", "rect2_y: " + String.valueOf(visionThread.rect2_y));
		SmartDashboard.putString("DB/String 7", "rect2_area: " + String.valueOf(visionThread.rect2_area));

		SmartDashboard.putString("DB/String 3", "Center of Board: " + String.valueOf(visionThread.center_of_board));
		// SmartDashboard.putString("DB/String 8", "center x,y: " + String.valueOf(visionThread.rect2_centerx) + "," + String.valueOf(visionThread.rect2_centery));

		SmartDashboard.putString("DB/String 4", "Area Difference " + String.valueOf(visionThread.area_difference));
	}

	public void disabledPeriodic() {
		SmartDashboard.putString("DB/String 0", "rect1_x: " + String.valueOf(visionThread.rect1_x));
		SmartDashboard.putString("DB/String 1", "rect1_y: " + String.valueOf(visionThread.rect1_y));
		SmartDashboard.putString("DB/String 2", "rect1_area: " + String.valueOf(visionThread.rect1_area));
		SmartDashboard.putString("DB/String 5", "rect2_x: " + String.valueOf(visionThread.rect2_x));
		SmartDashboard.putString("DB/String 6", "rect2_y: " + String.valueOf(visionThread.rect2_y));
		SmartDashboard.putString("DB/String 7", "rect2_area: " + String.valueOf(visionThread.rect2_area));

		SmartDashboard.putString("DB/String 3", "Center of Board: " + String.valueOf(visionThread.center_of_board));
		SmartDashboard.putString("DB/String 8", "center x,y: " + String.valueOf(visionThread.rect2_centerx) + "," + String.valueOf(visionThread.rect2_centery));

		SmartDashboard.putString("DB/String 4", "Area Difference " + String.valueOf(visionThread.area_difference));
	}
}
