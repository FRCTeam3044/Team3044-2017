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

	static AxisCamera FrontCamera;
	static GripPipeline pipeline = new GripPipeline();
	public int rect1_x;
	public int rect2_x;
	public int rect1_y;
	public int rect2_y;
	public double rect1_area;
	public double rect2_area;
	public int n_rectangles;
	public double center_1_x; 
	public double center_1_y;
	
	public double center_2_x; 
	public double center_2_y; 
	
	
	public VisionProcessingThread(AxisCamera frontCamera) {
		FrontCamera = frontCamera;
	}

	public void run() {
		while (true) {

			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");
			SmartDashboard.putString("DB/String 9", "VPT: " + f.format(new Date())); 
			
			Mat image = new Mat();

			CameraServer.getInstance().getVideo(FrontCamera).grabFrame(image);

			
			pipeline.process(image);

			if (!pipeline.filterContoursOutput().isEmpty()) {

				if (pipeline.filterContoursOutput().size() > 1) {

					Rect[] rectangles = new Rect[pipeline.filterContoursOutput().size()];

					for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {
						rectangles[i] = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));
					}
					
					
					rect1_x = rectangles[0].x;
					rect1_y = rectangles[0].y;
					rect1_area = rectangles[0].area();
					rect2_x = rectangles[1].x;
					rect2_y = rectangles[1].y;
					rect2_area = rectangles[1].area();
					n_rectangles = rectangles.length;
					
					center_1_x = rect1_x + (rectangles[0].width /2 ); 
					center_1_y = rect1_y + (rectangles[0].height /2 ); 
					
					center_2_x = rect2_x + (rectangles[1].width /2 ); 
					center_2_y = rect2_y + (rectangles[1].height /2 ); 
					
							
				}
			}
		}
	}

}
