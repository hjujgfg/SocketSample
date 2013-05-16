package com.example.socetsample;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.Runnable;;
public class ServerActivity extends Activity {

	 private TextView serverStatus;
	 
	    // DEFAULT IP
	    public static String SERVERIP = "10.0.2.15";
	    
	    // DESIGNATE A PORT
	    public static final int SERVERPORT = 8080;
	 
	    private Handler handler = new Handler();
	 
	    private ServerSocket serverSocket;
	    private Button bb;
	    private Context context;
	    ArrayList<Sercel> arr;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_server);
	        serverStatus = (TextView) findViewById(R.id.server_status);
	        bb = (Button)findViewById(R.id.serverbut);
	        context = this;
	        connection = true;
	        arr = new ArrayList<Sercel>();
	        for (int i = 0; i < 500; i ++) {
	        	arr.add(new Sercel(500 - i, 0));
	        }
	        bb.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(context, ClinetActivity.class);
					startActivity(i);
					finish();
				}
			});
	        Button bbb = (Button)findViewById(R.id.serverbutstop);
	        bbb.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					connection = false;
				}
			});
	        SERVERIP = getLocalIpAddress();
	        
	        Thread fst = new Thread(new ServerThreadRan());
	        
	        fst.start();
	    }
	    int counter;
	    String line;
	    PrintWriter output;
	    BufferedReader input;
	    Socket client;
	    String inString;
	    String outString = "yeah, shit((";
	    boolean connection;
	    public class ServerThread implements Runnable {
	 
	        public void run() {
	        	System.out.print("Accepted connection. ");
	        	
				System.out.println("Server listening on port 15432");
				while (connection){
		    		try {
		    			ServerSocket socket = new ServerSocket(SERVERPORT);
		    			client = socket.accept();
		    			// open a new PrintWriter and BufferedReader on the socket
		    			counter ++;
		    			output = new PrintWriter(client.getOutputStream(), true);
		    			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		    			System.out.print("Reader and writer created. ");
	
		    			
		    			// read the command from the client
		    		        while  ((inString = input.readLine()) == null);
		    			System.out.println("Read command " + inString);
		    			handler.post(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								Toast t = Toast.makeText(context, "got^ " + inString + " #" +counter, Toast.LENGTH_SHORT);
								t.show();
							}
						});
		    		        
		    			// run the command using CommandExecutor and get its output
		    			
		    			if (counter == 10)  outString = "END_MESSAGE";
		    			else outString += counter;
		    			System.out.println("Server sending result to client");
		    			// send the result of the command to the client
		    			handler.post(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								Toast t = Toast.makeText(context, "qqwqw " + outString + " #" +counter, Toast.LENGTH_SHORT);
								t.show();
							}
						});
		    			output.println(outString);
		    			//output.println(arr);
		    		}
		    		catch (IOException e) {
		    			e.printStackTrace();
		    		} 
		    		finally {
		    			// close the connection to the client
		    			try {
		    				client.close();
		    			}
		    			catch (IOException e) {
		    				e.printStackTrace();	
		    			}			
		    			System.out.println("Output closed.");
		    		}
	        	}
	        }
	    }
	    public class ServerThreadSer implements Runnable{
	    	ObjectOutputStream oos; 
			public void run() {
				// TODO Auto-generated method stub
				System.out.print("Accepted connection. ");
	        	
				System.out.println("Server listening on port 15432");
				while (connection){
		    		try {
		    			ServerSocket socket = new ServerSocket(SERVERPORT);
		    			client = socket.accept();
		    			// open a new PrintWriter and BufferedReader on the socket
		    			counter ++;
		    			oos = new ObjectOutputStream(client.getOutputStream());
		    			
		    			System.out.print("Reader and writer created. ");
	
		    			System.out.println("Writing");
		    			oos.writeObject(arr);
		    		    oos.flush();
		    		    oos.close();
		    		    connection = false;
		    		    System.out.println("ready");
		    			// run the command using CommandExecutor and get its output
		    			
		    			
		    			//output.println(arr);
		    		}
		    		catch (IOException e) {
		    			e.printStackTrace();
		    		} 
		    		finally {
		    			// close the connection to the client
		    			try {
		    				client.close();
		    			}
		    			catch (IOException e) {
		    				e.printStackTrace();	
		    			}			
		    			System.out.println("Output closed.");
		    		}
	        	}
			}
	    	
	    }
	    Sercel ss;
	    public class ServerThreadRan implements Runnable{
	    	ObjectOutputStream oos; 
	    	ObjectInputStream ois; 
	    	
			public void run() {
				// TODO Auto-generated method stub
				System.out.print("Accepted connection. ");
	        	ss = new Sercel(0, 0);
				System.out.println("Server listening on port " +SERVERPORT);
				while (connection){
		    		try {
		    			ServerSocket socket = new ServerSocket(SERVERPORT);
		    			client = socket.accept();
		    			// open a new PrintWriter and BufferedReader on the socket
		    			counter ++;
		    			oos = new ObjectOutputStream(client.getOutputStream());
		    			ois = new ObjectInputStream(client.getInputStream());
		    			System.out.print("Reader and writer created. ");
		    			ss = (Sercel)ois.readObject();
		    			Log.i("ServerActivity", "got " + ss.toString());     
		    			ss.field1 ++;
		    			System.out.println("Writing");
		    			oos.writeObject(ss);
		    			Log.i("ServerActivity", "sent " + ss.toString());
		    			handler.post(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								serverStatus.setText(ss.toString());
							}
						});
		    		    oos.flush();
		    		    oos.close();		    		    
		    		    System.out.println("ready");
		    			// run the command using CommandExecutor and get its output
		    			
		    			
		    			//output.println(arr);
		    		}
		    		catch (IOException e) {
		    			e.printStackTrace();
		    		} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		    		finally {
		    			// close the connection to the client
		    			try {
		    				client.close();
		    			}
		    			catch (IOException e) {
		    				e.printStackTrace();	
		    			}			
		    			System.out.println("Output closed.");
		    		}
	        	}
			}
	    	
	    }
	    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
	    private String getLocalIpAddress() {
	        try {
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = en.nextElement();
	                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();
	                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
	                }
	            }
	        } catch (SocketException ex) {
	            Log.d("ServerActivity", ex.toString());
	        }
	        return null;
	    }
	 
	    @Override
	    protected void onStop() {
	        super.onStop();
	        try {
	             // MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
	        	if (serverSocket != null) serverSocket.close();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	    }
	 
	}
