package org.usfirst.frc.team3044.Reference;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSourceType;

public class DummyTacho extends Counter{
	private double lastGoodValue = 0;
	private double currentValue = 0;
	private double droppedPulses = 0;
	public DummyTacho(DigitalInput i){
		super(i);
		this.setDistancePerPulse(1);
		this.setPIDSourceType(PIDSourceType.kRate);
		this.setSamplesToAverage(5);
		
	}
	
	
	
	@Override
	public double pidGet(){
		currentValue = getRate();
		if(Math.abs(currentValue - lastGoodValue) < 200){
			lastGoodValue = currentValue;
			
		}{
			droppedPulses += 1;
		}

		return lastGoodValue;
		
		
	}
	
	public double getDroppedPulses(){
		return this.droppedPulses;
	}

}
