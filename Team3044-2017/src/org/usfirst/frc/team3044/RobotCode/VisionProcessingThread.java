package org.usfirst.frc.team3044.RobotCode;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionProcessingThread extends Thread {

	static AxisCamera FrontCamera;
	static UsbCamera ClimberCamera;
	// static UsbCamera ClimberCamera;
	static GripPipeline pipeline = new GripPipeline();

	// All the variables needed to process things in vision
	public int rect1_x;
	public int rect2_x;
	public int rect1_y;
	public int rect2_y;
	public int rect1_width;
	public int rect2_width;
	public double rect1_area;
	public double rect2_area;
	double area_difference;
	public double rect1_centerx;
	public double rect1_centery;
	public double rect2_centerx;
	public double rect2_centery;
	double center_of_board;
	public int n_rectangles;

	// UsbCamera usbCamera = new UsbCamera("ClimberCamera", 0);

	public VisionProcessingThread(AxisCamera frontCamera) {
		FrontCamera = frontCamera;
		
		
	}

	
	
	/*
	 * public VisionProcessingThread(UsbCamera climberCamera) {
	 * ClimberCamera = climberCamera;
	 * }
	 */
	public void run() {
		while (true) {

			// For testing that this thread is running by putting the time in the dashboard
			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");
			SmartDashboard.putString("DB/String 9", "VPT: " + f.format(new Date()));

			Mat image = new Mat();
			Mat image2 = new Mat();

			try {
				// CameraServer.getInstance().startAutomaticCapture(ClimberCamera);
				CameraServer.getInstance().getVideo(FrontCamera).grabFrame(image);
			

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (image.empty())
				continue;

			pipeline.process(image);

			synchronized (Vision.imgLock) {
				// Checks to make sure there are contours to be found
				if (!pipeline.filterContoursOutput().isEmpty()) {

					// Checks to make sure there is more than one contour.
					if (pipeline.filterContoursOutput().size() > 1) {

						// Creates an array of rectangles
						Rect[] rectangles = new Rect[pipeline.filterContoursOutput().size()];

						// Processes the image with grip
						for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {
							rectangles[i] = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));
						}

						// Sets the variables to what was found by the array of rectangles
						rect1_x = rectangles[0].x;
						rect1_y = rectangles[0].y;
						rect2_x = rectangles[1].x;
						rect2_y = rectangles[1].y;
						rect1_area = rectangles[0].area();
						rect2_area = rectangles[1].area();
						n_rectangles = rectangles.length;
						rect1_width = rectangles[0].width;
						rect2_width = rectangles[1].width;
						rect1_centerx = rect1_x + (rectangles[0].width / 2);
						rect1_centery = rect1_y + (rectangles[0].height / 2);
						rect2_centerx = rect2_x + (rectangles[1].width / 2);
						rect2_centery = rect2_y + (rectangles[1].height / 2);
						area_difference = (rect1_area - rect2_area);

						// Makes sure that the rectangle on the left is ALWAYS rectangle 1
						boolean r1 = (rect1_x < rect2_x);
						if (r1) {
							center_of_board = (rect1_x + rect2_x + rect2_width) / 2;
						} else {
							center_of_board = (rect2_x + rect1_x + rect1_width) / 2;
						}

					}
				}
			}
		}
	}

}
