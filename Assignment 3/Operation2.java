package Part2;

import java.util.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 

class Operation2 extends Thread
{

	private Thread abc;
	private String nameOfThread;
	int logicalClock = 10;
	private ReentrantLock lock = new ReentrantLock();

	Operation2(String name)
	{
			 
		nameOfThread = name;
		System.out.println("Creating" + nameOfThread);
	}
	void sendingDataMethod() throws IOException, InterruptedException
	{
		DatagramPacket sendingPacket = null;
			
		System.out.println("Sending starting in 10 secs...");
		Thread.sleep(10000);
		int i;
		int lengthOfString;
		byte send[] = new byte[100];
	 	String concatData;
		DatagramSocket client = new DatagramSocket();
		String stringToUse="You can have either an apple or an orange";
		String strAray[] = stringToUse.split(" ");
		lengthOfString=strAray.length;
		
		for(i = 0; i<lengthOfString; i++)
		{
			
			String valueOf="Process 2: ";
			logicalClock++;
			long timeInMillis = System.currentTimeMillis();
			concatData = valueOf.concat(strAray[i]);
			concatData = concatData.concat("," + String.valueOf(timeInMillis));
			send = concatData.getBytes();
			sendingPacket = new DatagramPacket(send, send.length, InetAddress.getByName("localhost"), 8080);
			client.send(sendingPacket);
	
			sendingPacket = new DatagramPacket(send,send.length,InetAddress.getByName("localhost"), 8081);
			client.send(sendingPacket);  
		
			sendingPacket = new DatagramPacket(send,send.length,InetAddress.getByName("localhost"),8082);
			client.send(sendingPacket);
			sendingPacket=null;
			Thread.sleep(1000); 
		}

	}
	void Datareceiving() throws IOException
	{
			
			
		String dataString = "";
		int i=0;
		String aray[] = new String[100];
		int logicalTimeStamp[] = new int[50];
		List<Date> timeStamps = new ArrayList<Date>();
		int timeData[] = new int[50];
			
		DatagramSocket server = new DatagramSocket(8081);
		for(i = 0; i<27; i++)
		{
			byte receive[] = new byte[500];
			DatagramPacket rp = new DatagramPacket(receive, receive.length);
			server.receive(rp);
			String line = new String(rp.getData(),"UTF-8");
			// below is the important line to print and understand what we actually getting
			//System.out.println(" the: " + line);
			
			dataString = line.substring(0,line.indexOf(','));
			String timeStamp = line.substring(line.indexOf(',')+1);

			aray[i] = dataString;

			timeStamps.add(new Date(Long.parseLong(timeStamp.trim())));
		}
		printTheData(aray,timeData,timeStamps);
	}
	void printTheData(String aray[], int time[], List logical)
	{
		lock.lock();
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i = 0; i<27; i++)
			{
			System.out.println("The lock is set to 1 to acquire the resource. ");
			System.out.println(String.format("%s :     %s", aray[i], dateFormat.format(logical.get(i))));

			System.out.println("The lock is set to 0 to realize the resource. ");
			System.out.println(" ");
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	public void run()
	{
		
		System.out.println("Running " +  nameOfThread );
		try
		{
			if(nameOfThread.equalsIgnoreCase("Sender"))
			{
				sendingDataMethod();
			}
			else
			{
				Datareceiving();
			}

		}
		catch(SocketException e)
		{
			System.out.println("We are in excepetion");
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Thread " + nameOfThread + " exiting");
	}
	public void start()
	{

		System.out.println("Starting " + nameOfThread);
		if(abc == null)
		{
		      	 
		  	 abc = new Thread(this, nameOfThread);
		     abc.start();
		}
	}
	public static void main(String[] args) throws IOException
	{
		BufferedReader bufferSpace = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Press Enter to start the threads");
		bufferSpace.readLine();

		Operation2 P1= new Operation2("Sender");
		P1.start();

		Operation2 P2= new Operation2("receiver");
		P2.start();
	}	
}


	



