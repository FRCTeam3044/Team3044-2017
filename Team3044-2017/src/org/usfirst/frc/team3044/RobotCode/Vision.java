package org.usfirst.frc.team3044.RobotCode;

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
	private static final int IMG_WIDTH = 320;

	private static final int IMG_HEIGHT = 240;
	private VisionThread visionThread;
	
	int rect1_x;
	int rect2_x; 
	int rect1_y; 
	int rect2_y; 
	double rect1_area; 
	double rect2_area;
	int n_rectangles ; 
	int n_countours; 
	
	private RobotDrive drive;
	private final Object imgLock = new Object();


	@Override
	public void robotInit() {
		AxisCamera camera = CameraServer.getInstance().addAxisCamera("cam0");
		
		//CameraServer.getInstance().startAutomaticCapture();  //Info on this at https://www.chiefdelphi.com/forums/showpost.php?p=1425235&postcount=6
		
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);							//Above, put camera name inside () of startAutomaticCapture() in quotes
		
		
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
			
			Mat image=new Mat(); 
			
			CameraServer.getInstance().getVideo("cam0").grabFrame(image);
			
			pipeline.process(image);
				
			if (!pipeline.filterContoursOutput().isEmpty()) {
				
				/**  This will be the main vision processing algorthim implementation **/
				
				if (pipeline.filterContoursOutput().size() > 1) { 
					
					Rect[] rectangles= new Rect[pipeline.filterContoursOutput().size()];
					
					this.n_countours= pipeline.filterContoursOutput().size(); 
					
					for (int i=0; i < pipeline.filterContoursOutput().size(); i++ )
					{
						Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));
					}
					synchronized (imgLock) {
						this.rect1_x=  rectangles[0].x; 
						this.rect1_y = rectangles[0].y; 
						this.rect1_area = rectangles[0].area(); 
						this.rect2_x=  rectangles[1].x; 
						this.rect2_y = rectangles[1].y; 
						this.rect2_area = rectangles[1].area(); 
						this.n_rectangles = rectangles.length;
						
					}
				}
			}
		});
		visionThread.start();
	}

	@Override
	public void autonomousPeriodic() {
		
		
		
		synchronized (imgLock) { 
			SmartDashboard.putString("DB/String 0", "rect1_x: " +  String.valueOf(this.rect1_x)); 
			SmartDashboard.putString("DB/String 1", "rect1_y: " +  String.valueOf(this.rect1_y)); 
			SmartDashboard.putString("DB/String 2", "rect1_area: " +  String.valueOf(this.rect1_area)); 
			SmartDashboard.putString("DB/String 3", "n_rectangles: " +  String.valueOf(this.n_rectangles)); 
			SmartDashboard.putString("DB/String 4", "n_contours: " +  String.valueOf(this.n_countours)); 
			
			SmartDashboard.putString("DB/String 5", "rect2_x: " +  String.valueOf(this.rect2_x));
			SmartDashboard.putString("DB/String 6", "rect2_y: " +  String.valueOf(this.rect2_y));
			SmartDashboard.putString("DB/String 7", "rect2_area: " +  String.valueOf(this.rect2_area)); 
		}
		
	}
}
