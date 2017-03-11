package org.usfirst.frc.team3044.RobotCode;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionProcessingThread extends Thread {

	public boolean Run;
	// static AxisCamera FrontCamera;
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

	public VisionProcessingThread() {
	}

	public void run() {
		Run = true;
		while (Run) {

			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");
			SmartDashboard.putString("DB/String 9", "VPT: " + f.format(new Date()));

			Mat image = new Mat();

			try {
				// CameraServer.getInstance().getVideo().grabFrame(image);
				CameraServer.getInstance().getVideo(Vision.FrontCamera).grabFrameNoTimeout(image);
			} catch (Exception e) {
				// e.printStackTrace();
			}

			if (image.empty()) {
				System.out.println("Image is empty");
				continue;
			}

			try {
				pipeline.process(image);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!pipeline.filterContoursOutput().isEmpty()) {

				if (pipeline.filterContoursOutput().size() > 1) {

					Rect[] rectangles = new Rect[pipeline.filterContoursOutput().size()];

					Rect leftRectangle;
					Rect rightRectangle;

					for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {
						rectangles[i] = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));

					}

					// find rect with larger x and set rightRectangle = it
					// other goes into leftRectangle
					if (rectangles[0].x > rectangles[1].x) {
						rightRectangle = rectangles[0];
						leftRectangle = rectangles[1];
					} else {
						rightRectangle = rectangles[1];
						leftRectangle = rectangles[0];
					}

					// Sets the variables to what was found by the array of rectangles
					// Rect1 will ALWAYS be the left rectangle, Rect2 will ALWAYS be right rectangle
					rect1_x = leftRectangle.x;
					rect1_y = leftRectangle.y;
					rect2_x = rightRectangle.x;
					rect2_y = rightRectangle.y;
					rect1_area = leftRectangle.area();
					rect2_area = rightRectangle.area();
					n_rectangles = rectangles.length;
					rect1_width = leftRectangle.width;
					rect2_width = rightRectangle.width;
					rect1_centerx = rect1_x + (leftRectangle.width / 2);
					rect1_centery = rect1_y + (leftRectangle.height / 2);
					rect2_centerx = rect2_x + (rightRectangle.width / 2);
					rect2_centery = rect2_y + (rightRectangle.height / 2);
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
