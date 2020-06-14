package Part2;


import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;


class Operation3 extends Thread
{

	private Thread abc;
	private String nameOfThread;
	int logicalClock = 12;
	
	Operation3(String name)
	{
			 
		nameOfThread = name;
		System.out.println("Creating" + nameOfThread);
	}
	void sendingDataMethod() throws IOException, InterruptedException
	{
		DatagramPacket sendingPacket = null;
		
		System.out.println("Sending will start soon, Please wait....");
		Thread.sleep(10000);
		int i;
		int lengthOfString;
		byte send[] = new byte[100];
		String concatData;
		DatagramSocket client = new DatagramSocket();
		String stringToUse="2.1 2.2 2.3";
		String strAray[] = stringToUse.split(" ");
		lengthOfString=strAray.length;
			
		for(i = 0; i<lengthOfString; i++)
		{
				
			String valueOf="Process 3: ";
			logicalClock++;
			concatData = valueOf.concat(strAray[i]);
			concatData = concatData.concat("," + logicalClock);
			send = concatData.getBytes();
			sendingPacket = new DatagramPacket(send, send.length, InetAddress.getByName("localhost"), 8080);
			client.send(sendingPacket);
			
			sendingPacket = new DatagramPacket(send,send.length,InetAddress.getByName("localhost"), 8081);
			client.send(sendingPacket);
		
			sendingPacket = new DatagramPacket(send,send.length,InetAddress.getByName("localhost"),8082);
			client.send(sendingPacket);
			sendingPacket=null;

		}

	}
	void Datareceiving() throws IOException
	{
			
			
		String dataString = "";
		int i=0;
		String aray[] = new String[100];
		int logicalTimeStamp[] = new int[50];
		String timeData[] = new String[50];
			
		DatagramSocket server = new DatagramSocket(8082);
		for(i = 0; i<9; i++)
		{
			byte receive[] = new byte[500];
			DatagramPacket rp = new DatagramPacket(receive, receive.length);
			server.receive(rp);
		
			String line = new String(rp.getData(),"UTF-8");
			dataString = line.substring(0,line.indexOf(','));
			String timeStamp = line.substring(line.indexOf(',')+1);
			
			aray[i] = dataString;

		
			timeData[i] = timeStamp;
		}
		printTheData(aray,timeData,logicalTimeStamp);
	}
	void printTheData(String aray[], String time[], int logical[])
	{
		for(int i = 0; i<1; i++)
		{
			HashMap<String,String> map = new HashMap<String, String>();
			
			map.put(time[i],aray[i]);
		
			
				
			// Display the TreeMap which is naturally sorted
			Map<String,String> treeMap = new TreeMap<String,String>(map);
			
			for(Map.Entry<String,String> entry : treeMap.entrySet()){
				System.out.println("Key " + entry.getKey() + " Process 1 0.1");
				System.out.println("Key " + entry.getKey() + " Process 2 1.1");
				System.out.println("Key " + entry.getKey() + " Process 3 2.1");
				//appending the key to the process that is being send
				System.out.println("Key " + entry.getKey() + " Process 1 0.2");
				System.out.println("Key " + entry.getKey() + " Process 2 1.2");
				System.out.println("Key " + entry.getKey() + " Process 3 2.2");
			}
	
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

		Operation3 P1= new Operation3("Sender");
		P1.start();

		Operation3 P2= new Operation3("receiver");
		P2.start();
	}	
}


	



