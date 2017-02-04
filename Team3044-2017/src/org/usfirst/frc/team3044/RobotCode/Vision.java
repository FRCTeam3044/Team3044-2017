package org.usfirst.frc.team3044.RobotCode;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import com.ctre.CANTalon;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Vision extends IterativeRobot {
	private static final int IMG_WIDTH = 320;

	private static final int IMG_HEIGHT = 240;
	private VisionThread visionThread;
	private double centerX = 0.0;
	private RobotDrive drive;
	private final Object imgLock = new Object();

	GripPipeline pipeline = new GripPipeline(); 
	@Override
	public void robotInit() {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();  //Info on this at https://www.chiefdelphi.com/forums/showpost.php?p=1425235&postcount=6
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);							//Above, put camera name inside () of startAutomaticCapture() in quotes
		
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
			if (!pipeline.filterContoursOutput().isEmpty()) {
				
				/**  This will be the main vision processing algorthim implementation **/
				
				if (pipeline.filterContoursOutput().size() > 1) { 
					
					Rect[] rectangles= new Rect[pipeline.filterContoursOutput().size()];
					
					for (int i=0; i < pipeline.filterContoursOutput().size(); i++ )
					{
						Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));
						
						synchronized (imgLock) {
							centerX = r.x + (r.width / 2);
						}
					}
				}
			}
		});
		visionThread.start();
		drive = new RobotDrive(1, 2);
	}

	@Override
	public void autonomousPeriodic() {
		

		
		/*
		double centerX;
		synchronized (imgLock) {
			centerX = this.centerX;
		}
		double turn = centerX - (IMG_WIDTH / 2);
		drive.arcadeDrive(-0.6, turn * 0.005);*/
	}
}
