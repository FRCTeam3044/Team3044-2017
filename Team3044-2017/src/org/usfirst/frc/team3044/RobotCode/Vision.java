package org.usfirst.frc.team3044.RobotCode;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {

	public static AxisCamera FrontCamera;
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	private RobotDrive drive;
	private final Object imgLock = new Object();
	private VisionProcessingThread visionThread;

	public void robotInit() {
		// CAMERA_IP
		//FrontCamera = new AxisCamera("Gear Camera", "10.30.44.11");
		//FrontCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		
		FrontCamera = CameraServer.getInstance().addAxisCamera("10.30.44.11");

		//USB camera
		CameraServer.getInstance().startAutomaticCapture();
		
		//CameraServer.getInstance().startAutomaticCapture(Vision.FrontCamera);
		//CameraServer.getInstance().startAutomaticCapture().setResolution(640, 480);
		// FrontCamera = CameraServer.getInstance().addAxisCamera(host)
		// Object O = CameraServer.getInstance();
		// if (FrontCamera ==null) {System.out.println("Front camera is null!!!!"); }
		visionThread = new VisionProcessingThread();
		//visionThread.start();
	}
	
	public void stopVisionThread(){
		visionThread.Run=false;
	}
	
	public void startVisionThread(){
		visionThread = new VisionProcessingThread();
		visionThread.start();
	}

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

	public void disabledPeriodic() {
		SmartDashboard.putString("DB/String 0", "rect1_x: " + String.valueOf(visionThread.rect1_x));
		SmartDashboard.putString("DB/String 1", "rect1_y: " + String.valueOf(visionThread.rect1_y));
		SmartDashboard.putString("DB/String 2", "rect1_area: " + String.valueOf(visionThread.rect1_area));
		// SmartDashboard.putString("DB/String 3", "Center of Board: " + String.valueOf(visionThread.centerOfBoard));
		// SmartDashboard.putString("DB/String 4", "Area Difference " + String.valueOf(visionThread.areaDifference));
		SmartDashboard.putString("DB/String 5", "rect2_x: " + String.valueOf(visionThread.rect2_x));
		SmartDashboard.putString("DB/String 6", "rect2_y: " + String.valueOf(visionThread.rect2_y));
		SmartDashboard.putString("DB/String 7", "rect2_area: " + String.valueOf(visionThread.rect2_area));
		// SmartDashboard.putString("DB/String 8", "center x,y: " + String.valueOf(visionThread.rect2_centerx) + "," + String.valueOf(visionThread.rect2_centery));
	}
}
