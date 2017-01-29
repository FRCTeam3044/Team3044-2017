package org.usfirst.frc.team3044.RobotCode;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.usfirst.frc.team3044.Reference.*;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//
public class shooter {
	PowerDistributionPanel pdp = new PowerDistributionPanel();

	FirstController controller = FirstController.getInstance();

	AnalogInput DummyTacho = new AnalogInput(0);

	public static int BUTTON_Y = 4;

	public CANTalon shooter;
	public CANTalon agitator;
	public CANTalon angle;
	File f;
	BufferedWriter bw;
	FileWriter fw;
	int i;
	private Outputs comp = Outputs.getInstance();

	public void driveInit() {

	}

	public void driveAutoPeriodic() {
	}
	
	public void driveTeleopPeriodic() {
		{
		AnalogInput DummyTacho = new AnalogInput(0);
		int bits;
		DummyTacho.setOversampleBits(4);
		bits = DummyTacho.getOversampleBits();
		DummyTacho.setAverageBits(2);
		bits = DummyTacho.getAverageBits();
		int averageRaw = DummyTacho.getAverageValue();
		
		controller.getInstance();
			boolean aButton = false;
			boolean bButton = false;
			boolean xButton = false;
			boolean yButton = false;
		if (aButton == true){ 
			shooter.set(100);
		if(Math.abs(averageRaw) > 200){
				agitator.set(50);
		}
		}
		if (bButton == true){ 
			shooter.set(75);
			if(Math.abs(averageRaw) > 150){
				agitator.set(50);
		}
			}
		if (xButton == true){ shooter.set(50);{
			if(Math.abs(averageRaw) > 100){
				agitator.set(50);
		}
		
		}
	
		
		{
		SmartDashboard.putString("DB/String 5", String.valueOf("Left Front " + Outputs.getInstance().leftFrontDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 6", String.valueOf("Right Front " + Outputs.getInstance().rightFrontDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 7", String.valueOf("Left Back " + Outputs.getInstance().leftBackDrive.getOutputCurrent()));
		SmartDashboard.putString("DB/String 8", String.valueOf("Right Back " + Outputs.getInstance().rightBackDrive.getOutputCurrent()));
		i++;}}}}
		
	

	public void testInit() {
		try {
			// bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
