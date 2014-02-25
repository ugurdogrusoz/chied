package org.gvt;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.ivis.layout.LayoutConstants;
//import org.ivis.layout.TestConstants;


public class Test
{
	public Test()
	{
		
	}
	
	public static void writeLog()
	{
		time = LayoutConstants.time;
		iterations = LayoutConstants.iterations;
		
		log += filename + "," + nodes + "," + clusters +"," + iterations + "," + time + "," + misplaced + "\n";
		System.out.print(log);
		
		time = 0;
		filename = "";
		iterations = 0;
		nodes = 0;
		clusters = 0;
		misplaced = 0;
	}
	
	public static void writeLogFile()
	{
		try
		{
			PrintWriter writer = new PrintWriter("logfile.csv", "UTF-8");
			writer.println(log);
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION OPENING THE FILE");
		}
	}
	
	public static String log = "";
	public static long time = 0;
	public static String filename = "";
	public static int iterations = 0;
	public static int nodes = 0;
	public static int clusters = 0;
	public static int misplaced = 0;
	
}