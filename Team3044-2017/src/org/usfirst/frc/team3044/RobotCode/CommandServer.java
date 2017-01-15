package org.usfirst.frc.team3044.RobotCode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CommandServer  {
	
	private static Thread serverWorkerThread =null; 
	
	public static void setTcpPortNumber(int portNumber)
	{
		if (!CommandServerWorker.runServer) { 
		CommandServerWorker.tcpPortNumber = portNumber; } 
	}
	
	public static void setTeamNumber(int teamNumber)
	{
		if (!CommandServerWorker.runServer) { 
		CommandServerWorker.teamNumber=teamNumber; } 
	}
	
	public static int getTeamNumber()
	{
		return CommandServerWorker.teamNumber; 
	}
	
	public static void setBannerData(String bannerData)
	{
		if (!CommandServerWorker.runServer) { 
		CommandServerWorker.bannerData= bannerData; } 
	}
	
	public static String getBannerData()
	{
		return CommandServerWorker.bannerData; 
	}

	public static void startServer()
	{
		if (!CommandServerWorker.runServer) { 
		serverWorkerThread= new Thread(new CommandServerWorker());
		serverWorkerThread.start(); } 
	}

	public void stopServer()
	{
		CommandServerWorker.stopServer();
	}
	
	

}
