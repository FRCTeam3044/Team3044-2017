/*Colin MacDonald
 * 3-13-17
 * Implements the GripPipeline and VisionProcessingThread to find the gear deposit station		
 * Maximum range of 11 feet		
 * Minimum range of 1 foot		
*/
package org.usfirst.frc.team3044.RobotCode;

import org.usfirst.frc.team3044.Reference.Inputs;
import org.usfirst.frc.team3044.Reference.Outputs;

import com.ctre.CANTalon;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

public class Vision {

	public static AxisCamera FrontCamera;
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private RobotDrive drive;
	private final Object imgLock = new Object();
	private VisionProcessingThread visionThread;
	int state = 1;
	int timeState = 0;
	int count = 0;
	public static boolean visionDone = false;

	Timer time = new Timer();
	public CANTalon GearCANTalon;
	public Outputs out = Outputs.getInstance();

	public void robotInit() {
		// CAMERA_IP
		// FrontCamera = new AxisCamera("Gear Camera", "10.30.44.11");
		// FrontCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		GearCANTalon = Outputs.getInstance().GearCANTalon;
		FrontCamera = CameraServer.getInstance().addAxisCamera("10.30.44.11");

		// USB camera
		// CameraServer.getInstance().startAutomaticCapture();

		// CameraServer.getInstance().startAutomaticCapture(Vision.FrontCamera);
		CameraServer.getInstance().startAutomaticCapture().setResolution(640, 480);

		// FrontCamera = CameraServer.getInstance().addAxisCamera(host)
		// Object O = CameraServer.getInstance();
		// if (FrontCamera ==null) {System.out.println("Front camera is null!!!!"); }
		visionThread = new VisionProcessingThread();
		// visionThread.start();
		
		

	}

	public void stopVisionThread() {
		visionThread.Run = false;
	}

	public void startVisionThread() {
		visionThread = new VisionProcessingThread();
		visionThread.start();
		state = 1;
	}

	final int ALIGN = 0; // Aligns the robot and does not move forward (currently not used)
	final int APPROACH = 1; // Moves forward and aligns at a slower speed
	final int FINISH = 2; // Close to gear deposit station, switches to time
	final int GEAR = 3; // and deposits gear
	final int STOPGEAR = 4; // Stops the gear motor
	final int REVERSE = 5; // Backs up, hands control off to timed movement (for shooter or baseline crossing)

	public void autonomousPeriodic() {
		double x = 0, y = 0, r = 0;
		count++;
		SmartDashboard.putString("DB/String 8", String.valueOf(state));

		// Uses an object
		synchronized (imgLock) {

			SmartDashboard.putNumber("Area difference", visionThread.area_difference);
			SmartDashboard.putNumber("Board center", visionThread.center_of_board);

			// Start of the cases
			switch (state) {

			// Aligns the robot and does not move forward
			case ALIGN: // Case 0 (not used currently) <------------------NOT USED

				switch (timeState) {
				case 0:
					y = .45;
					time.start();
					timeState = 1;
					break;
				case 1:
					if (time.get() == .5) {
						y = 0;
						timeState = 500;
					}
					break;
				}

				// If the center is to the right, translate to the right at a scaled speed
				if (visionThread.center_of_board < 150) {
					x = -(Math.abs(visionThread.center_of_board - 160) / 40) * .6;
					x -= .1;
					System.out.println("1");

					// If the center is to the left, translate to the left at a scaled speed
				} else if (visionThread.center_of_board > 170) {
					x = (Math.abs(visionThread.center_of_board - 160) / 40) * .6;
					x += .1;
					System.out.println("2");

					// If the center of the board is in the center of the image, don't translate
				} else {
					x = 0;
					System.out.println("3");

				}
				// If the difference in area is more than 50, correct it.
				if (Math.abs(visionThread.area_difference) > 50) {

					// Scaled rotation - the more tilted the target is, the faster the robot rotates
					// r = -((visionThread.area_difference / 350) * .2);
					System.out.println("4");

				} else
					r = 0;
				System.out.println("5");

				// If the robot is aligned, move onto the next state
				if (visionThread.center_of_board > 150 && visionThread.center_of_board < 170 && Math.abs(visionThread.area_difference) <= 50) {
					state++;
					System.out.println("6");
				}

				// If less than 2 contours are seen, don't move. Will be changed so that the robot actively seeks out the target.
				if ((VisionProcessingThread.pipeline.filterContoursOutput().size() < 2)) {
					x = 0;
					y = 0;
					r = 0;
					System.out.println("7");
				}

				break;

			case APPROACH: // Case 1

				// If the area difference is less than 50, the center of the board is between 150 and 170, and the rectangle area is less than 2000, drive forward
				if (Math.abs(visionThread.area_difference) <= 50 && visionThread.center_of_board > 150 && visionThread.center_of_board < 170 && visionThread.rect1_area < 2000
						&& visionThread.rect1_area > 1000) {
					y = ((2000 - visionThread.rect1_area) / 1500) * .3;
					y += .1;
					System.out.println("8 vision forwards");
				} else if (visionThread.rect1_area < 1000) {
					y = .3;

				} else {

					// If they are not true, don't drive forward
					y = 0;
					System.out.println("9 vision no forwards");

					// Translate and rotate at a decreased speed than the first case
					if (visionThread.center_of_board < 150) {
						x = -(Math.abs(visionThread.center_of_board - 160) / 40) * .6;
						x -= .1;
						System.out.println("10 vision translate left");

						// If the center is to the left, translate to the left at a scaled speed
					} else if (visionThread.center_of_board > 170) {
						x = (Math.abs(visionThread.center_of_board - 160) / 40) * .6;
						x += .1;
						System.out.println("11 vision translate right");

						// If the center of the board is in the center of the image, don't translate
					} else {
						x = 0;
						System.out.println("12 vision no translate");
					}
					// If the difference in area is more than 50, correct it.
					if (Math.abs(visionThread.area_difference) > 50) {

						// Scaled rotation - the more tilted the target is, the faster the robot rotates
						// r = -((visionThread.area_difference / 350) * .2);
						System.out.println("13 vision rotate");

					} else
						r = 0;
					System.out.println("14 vision no rotate");
				}

				// If less than 2 contours are seen, don't move. Will be changed so that the robot actively seeks out the target.
				if ((VisionProcessingThread.pipeline.filterContoursOutput().size() < 2 || visionThread.rect1_area > 2000)) {
					x = 0;
					y = .2;
					r = 0;
					System.out.println("15 vision no mavement at all");
				}
				if (visionThread.rect1_area > 1000 || visionThread.rect2_area > 1000) {
					count = 0;
					state++;
				}
				
				if (count>250){
					state=3;
					count=0;
				}

				// Limits on the translational, rotational, and forward/backward movement variables, adjusted to the decreased movement
				if (x > .6)
					x = .6;
				if (x < -.6)
					x = -.6;
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
			case FINISH: // Case 2
				System.out.println("16");
				count++;
				y = .3;
				if (count >= 75) {
					y = 0;
					state = GEAR;
				}
				break;

			case GEAR: // Case 3
				GearCANTalon.set(1);
				count = 0;
				state = 4;
				break;
			case STOPGEAR: // Case 4
				count++;
				if (!Inputs.limitSwitchIn.get() || count>=400) {
					GearCANTalon.set(0);
					count = 0;
					state++;
				}
				break;
			case REVERSE: // Case 5
				count++;
				y = -.5;
				if (count >= 75) {
					y = 0;
					visionDone = true;
				}
				break;
			}

		}
		// If the A button is pressed, drive forwards and continue adjusting

		/*
		 * if (SecondController.getInstance().getRawButton(SecondController.BUTTON_A)) {
		 * y = ((2000 - visionThread.rect1_area) / 1500) * .2;
		 * } else {
		 * y = 0;
		 * }
		 */
		// if(visionThread.center_of_board > 195 || visionThread.center_of_board < 110) x = 0;

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
		if (y < -.5)
			y = .5;

		// Drive functions that take X, Y and R as inputs. Will later be modified to be helper functions in a different class
		double v_FrontLeft = r - y - x;
		double v_FrontRight = r + y - x;
		double v_BackLeft = r - y + x;
		double v_BackRight = r + y + x;

		// Uses the double, f, to regulate the algorithm for when it later used to power motors

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

		SmartDashboard.putString("DB/String 0", "rect1_x: " + String.valueOf(visionThread.rect1_x));
		SmartDashboard.putString("DB/String 1", "rect1_y: " + String.valueOf(visionThread.rect1_y));
		SmartDashboard.putString("DB/String 2", "rect1_area: " + String.valueOf(visionThread.rect1_area));
		SmartDashboard.putString("DB/String 5", "rect2_x: " + String.valueOf(visionThread.rect2_x));
		SmartDashboard.putString("DB/String 6", "rect2_y: " + String.valueOf(visionThread.rect2_y));
		SmartDashboard.putString("DB/String 7", "rect2_area: " + String.valueOf(visionThread.rect2_area));
		SmartDashboard.putString("DB/String 3", "Area difference: " + String.valueOf(visionThread.area_difference));
		SmartDashboard.putString("DB/String 4", "Center of board: " + String.valueOf(visionThread.center_of_board));

		System.out.println("count timer is " + count);

		// SmartDashboard.putString("DB/String 3", "center x,y: " + String.valueOf(visionThread.center_1_x) + "," + String.valueOf(visionThread.center_1_y));
		// SmartDashboard.putString("DB/String 8", "center x,y: " + String.valueOf(visionThread.center_2_x) + "," + String.valueOf(visionThread.center_2_y));
	}

	public void disabledPeriodic() {
		SmartDashboard.putString("DB/String 0", "rect1_x: " + String.valueOf(visionThread.rect1_x));
		SmartDashboard.putString("DB/String 1", "rect1_y: " + String.valueOf(visionThread.rect1_y));
		SmartDashboard.putString("DB/String 2", "rect1_area: " + String.valueOf(visionThread.rect1_area));
		SmartDashboard.putString("DB/String 5", "rect2_x: " + String.valueOf(visionThread.rect2_x));
		SmartDashboard.putString("DB/String 6", "rect2_y: " + String.valueOf(visionThread.rect2_y));
		SmartDashboard.putString("DB/String 7", "rect2_area: " + String.valueOf(visionThread.rect2_area));
		SmartDashboard.putString("DB/String 3", "Area difference: " + String.valueOf(visionThread.area_difference));
		SmartDashboard.putString("DB/String 4", "Center of board: " + String.valueOf(visionThread.center_of_board));

		// SmartDashboard.putString("DB/String 3", "center x,y: " + String.valueOf(visionThread.center_1_x) + "," + String.valueOf(visionThread.center_1_y));
		// SmartDashboard.putString("DB/String 8", "center x,y: " + String.valueOf(visionThread.center_2_x) + "," + String.valueOf(visionThread.center_2_y));
	}
}
