package org.usfirst.frc.team3044.RobotCode;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import com.ctre.CANTalon;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Vision extends IterativeRobot {
	private static final int IMG_WIDTH = 320;

	private static final int IMG_HEIGHT = 240;
	private VisionThread visionThread;
	
	int rect1_x;
	int rect2_x; 
	int rect1_y; 
	int rect2_y; 
	double rect1_area; 
	double rect2_area;  
	
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
					}
					synchronized (imgLock) {
						rect1_x=  rectangles[0].x; 
						rect1_y = rectangles[0].y; 
						rect1_area = rectangles[0].area(); 
						rect2_x=  rectangles[1].x; 
						rect2_y = rectangles[1].y; 
						rect2_area = rectangles[1].area(); 
					}
				}
			}
		});
		visionThread.start();
		drive = new RobotDrive(1, 2);
	}

	@Override
	public void autonomousPeriodic() {
		
		synchronized (imgLock) { 
			SmartDashboard.putString("DB/String 1", "rect1_x: " +  String.valueOf(rect1_x)); 
			SmartDashboard.putString("DB/String 2", "rect1_y: " +  String.valueOf(rect1_y)); 
			SmartDashboard.putString("DB/String 3", "rect1_area: " +  String.valueOf(rect1_area)); 
			SmartDashboard.putString("DB/String 5", "rect2_x: " +  String.valueOf(rect2_x));
			SmartDashboard.putString("DB/String 6", "rect1_y: " +  String.valueOf(rect1_y));
			SmartDashboard.putString("DB/String 1", "rect1_area: " +  String.valueOf(rect1_area)); 
		}
		
	}
}
