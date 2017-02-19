package org.usfirst.frc.team3044.Reference;

import edu.wpi.first.wpilibj.I2C;

public class NavX {

	private static NavX instance = null;

	//private I2C navX = new I2C( , int deviceAddress);

	private NavX() {
	}

	public static NavX getInstance() {
		if (instance == null) {
			instance = new NavX();
		}
		return instance;
	}

}
