package org.usfirst.frc.team3044.RobotCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CommandServerWorker implements Runnable {
	
	private ServerSocket serverSocket = null;
	private Socket workerSocket=null; 
	private String lineData="";
	static boolean runServer = false; 
	static int tcpPortNumber= 5800; 
	static String bannerData = "RMIS";
	static int teamNumber = 3044; 
	
	public static void stopServer()
	{
		runServer=false; 
	}
	
	public boolean isClientConnected()
	{
		return (workerSocket != null && workerSocket.isConnected()); 
	}
	
	@Override 
	public void run() 
	{
		runServer=true; 
		try { 
			while (runServer)
			{
				serverSocket = new ServerSocket(tcpPortNumber); 
			
				workerSocket= serverSocket.accept();
				
				BufferedReader socketReader= 
						new BufferedReader ( 
						new InputStreamReader(workerSocket.getInputStream()));
						
				BufferedWriter socketWriter= 
						new BufferedWriter ( 
						 new PrintWriter   (workerSocket.getOutputStream()));
				
				
				while (workerSocket.isConnected())
				{
					lineData = socketReader.readLine(); 
					// NNR up to here is good 
					
				}
			}		
		}
		catch (Exception e)
		{
			System.out.println("Exception thrown in CommandServer.startServer()"); 
			System.out.println(e.getMessage()); 
			System.out.println(e.getStackTrace()); 
			runServer=false; 
			
			if (workerSocket != null && workerSocket.isConnected()) 
			{
				try { 
				workerSocket.close(); } 
				catch (Exception ex) {} 
			}
			
			if (serverSocket !=null && !serverSocket.isClosed())
			{
				try { 
				serverSocket.close(); }
				catch (Exception ex2) { }
			}
		}
	}

}
