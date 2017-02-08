package org.usfirst.frc.team3044.RobotCode;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import com.ctre.CANTalon;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Vision extends IterativeRobot {

	public static AxisCamera FrontCamera;
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private RobotDrive drive;
	private final Object imgLock = new Object();
	private VisionProcessingThread visionThread;

	@Override
	public void robotInit() {
		// CAMERA_IP
		FrontCamera = CameraServer.getInstance().addAxisCamera("10.30.44.11");
		FrontCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		visionThread = new VisionProcessingThread(Vision.FrontCamera);
		visionThread.start();
	}

	@Override
	public void autonomousPeriodic() {

		SmartDashboard.putString("DB/String 0", "rect1_x: " + String.valueOf(visionThread.rect1_x));
		SmartDashboard.putString("DB/String 1", "rect1_y: " + String.valueOf(visionThread.rect1_y));
		SmartDashboard.putString("DB/String 2", "rect1_area: " + String.valueOf(visionThread.rect1_area));
		SmartDashboard.putString("DB/String 5", "rect2_x: " + String.valueOf(visionThread.rect2_x));
		SmartDashboard.putString("DB/String 6", "rect2_y: " + String.valueOf(visionThread.rect2_y));
		SmartDashboard.putString("DB/String 7", "rect2_area: " + String.valueOf(visionThread.rect2_area));
		
		SmartDashboard.putString("DB/String 3", "center x,y: " + String.valueOf(visionThread.center_1_x) + "," + String.valueOf(visionThread.center_1_y));
		SmartDashboard.putString("DB/String 8", "center x,y: " + String.valueOf(visionThread.center_2_x) + "," + String.valueOf(visionThread.center_2_y));
	}
}
