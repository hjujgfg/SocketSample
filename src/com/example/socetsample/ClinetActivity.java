package com.example.socetsample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClinetActivity extends Activity {

	private EditText serverIp;
	 
    private Button connectPhones;
    private Button disconnect;
    private String serverIpAddress = "";
    AtomicLong totalTime;
    private boolean connected = false;
    private boolean bb;
    private Handler handler = new Handler();
    private TextView tex;
    private String incomingText;
    Context context;
    Sercel ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinet);
        context = this;
        serverIp = (EditText) findViewById(R.id.editText1);
        serverIp.setText("192.168.1.103");
        connectPhones = (Button) findViewById(R.id.button1);
        connectPhones.setOnClickListener(connectListener);
        ss = new Sercel(0, 0);
        disconnect = (Button) findViewById(R.id.clientdisconnect);
        disconnect.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				connected = false;
				if (cThread != null) {
					cThread.interrupt();
				}
			}
		});
        tex = (TextView) findViewById(R.id.textView1); 
        bb = true;
        incomingText = "weqw";
        tex.setText(incomingText);
       
    }
    Thread cThread;
    Thread chngr;
    private OnClickListener connectListener = new OnClickListener() {
    	public void onClick(View v) {
            if (!connected) {
                serverIpAddress = serverIp.getText().toString();
                if (!serverIpAddress.equals("")) {                	
                	connected= true;
                    cThread = new Thread(new ClientThreadRan());
                    cThread.start();
                    chngr = new Thread(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							while(connected) {
								ss.field2 ++;
								handler.post(new Runnable() {
									
									public void run() {
										// TODO Auto-generated method stub
										tex.setText(ss.toString());
									}
								});
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}							
						}
					});
                    chngr.start();
                }
            }
        }		
    };
    String st;
    Socket socket;
    long startTime;
	long endTime;
	String outputString;
    public class ClientThread implements Runnable {
 
        public void run() {
        	PrintWriter out = null;
    		BufferedReader input = null;
    		try {
    			//creates a new Socket object and names it socket.
    			//Establishes the socket connection between the client & server
    			//name of the machine & the port number to which we want to connect
    			socket = new Socket(serverIpAddress, ServerActivity.SERVERPORT);
    			//if (printOutput) {
    				System.out.print("Establishing connection.");
    			//}
    			//opens a PrintWriter on the socket input autoflush mode
    			out = new PrintWriter(socket.getOutputStream(), true);

    			//opens a BufferedReader on the socket
    			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    			 System.out.println("\nRequesting output for the '" +"' command from " + serverIpAddress);
    			 
    			// get the current time (before sending the request to the server)
    			startTime = System.currentTimeMillis();

    			// send the command to the server
    			out.println("shit");
    			System.out.println("Sent output");

    			// read the output from the server
    			
    			while (((outputString = input.readLine()) != null) && (!outputString.equals("END_MESSAGE"))) {
    				System.out.println(outputString);
    				handler.post(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							Toast t = Toast.makeText(context, outputString, Toast.LENGTH_SHORT);
							t.show();
						}
					});
    			}
    			/*ObjectInputStream ois = (ObjectInputStream) socket.getInputStream();
    			
    			try {
					ArrayList<Sercel> tmp = (ArrayList<Sercel>) ois.readObject();
					for (Sercel x : tmp) {
						System.out.print("");
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
    			// get the current time (after connecting to the server)
    			endTime = System.currentTimeMillis();
    			// endTime - startTime = the time it took to get the response from the sever
    			//totalTime.addAndGet(endTime - startTime);

    		}
    		catch (UnknownHostException e) {
    			System.err.println("Unknown host: " + serverIpAddress);
    			System.exit(1);
    		}
    		catch (ConnectException e) {
    			System.err.println("Connection refused by host: " + serverIpAddress);
    			System.exit(1);
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    		// finally, close the socket and decrement runningThreads
    		finally {
    			System.out.println("closing");
    			try {
    				socket.close();
    				//runningThreads.decrementAndGet();
    				System.out.flush();
    			}
    			catch (IOException e ) {
    				System.out.println("Couldn't close socket");
    			}
    		}
        }
    }
    public class ClientThreadSer implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			PrintWriter out = null;
    		BufferedReader input = null;
    		ObjectInputStream ois;
    		try {
    			//creates a new Socket object and names it socket.
    			//Establishes the socket connection between the client & server
    			//name of the machine & the port number to which we want to connect
    			socket = new Socket(serverIpAddress, ServerActivity.SERVERPORT);
    			//if (printOutput) {
    				System.out.print("Establishing connection.");
    			//}
    			//opens a PrintWriter on the socket input autoflush mode
    			ois = new ObjectInputStream(socket.getInputStream());
    			/*ObjectInputStream ois = (ObjectInputStream) socket.getInputStream();
    			*/
    			try {
					ArrayList<Sercel> tmp = (ArrayList<Sercel>) ois.readObject();
					for (Sercel x : tmp) {
						System.out.print(" " + x.field1);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			// get the current time (after connecting to the server)
    			endTime = System.currentTimeMillis();
    			// endTime - startTime = the time it took to get the response from the sever
    			totalTime = new AtomicLong();
    			totalTime.addAndGet(endTime - startTime);
    			handler.post(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						Toast t = Toast.makeText(context, totalTime.toString(), Toast.LENGTH_LONG);
						t.show();
					}
				});

    		}
    		catch (UnknownHostException e) {
    			System.err.println("Unknown host: " + serverIpAddress);
    			System.exit(1);
    		}
    		catch (ConnectException e) {
    			System.err.println("Connection refused by host: " + serverIpAddress);
    			System.exit(1);
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    		// finally, close the socket and decrement runningThreads
    		finally {
    			System.out.println("closing");
    			try {
    				socket.close();
    				//runningThreads.decrementAndGet();
    				System.out.flush();
    			}
    			catch (IOException e ) {
    				System.out.println("Couldn't close socket");
    			}
    		}
		}
    	
    }
    public class ClientThreadRan implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			
    		ObjectInputStream ois;
    		ObjectOutputStream oos;    		
    		while(connected) {
    			try {
        			//creates a new Socket object and names it socket.
        			//Establishes the socket connection between the client & server
        			//name of the machine & the port number to which we want to connect
        			socket = new Socket(serverIpAddress, ServerActivity.SERVERPORT);
        			//if (printOutput) {
        				System.out.print("Establishing connection.");
        			//}
        			//opens a PrintWriter on the socket input autoflush mode
        			ois = new ObjectInputStream(socket.getInputStream());
        			oos = new ObjectOutputStream(socket.getOutputStream());
        			//ss.field2 ++;
        			oos.writeObject(ss);
        			Log.i("ClientActivity", "sent " + ss.toString());
        			ss = (Sercel)ois.readObject();
        			Log.i("ClientActivity", "got " + ss.toString());   
        			
        		}
        		catch (UnknownHostException e) {
        			System.err.println("Unknown host: " + serverIpAddress);
        			System.exit(1);
        		}
        		catch (ConnectException e) {
        			System.err.println("Connection refused by host: " + serverIpAddress);
        			System.exit(1);
        		}
        		catch (IOException e) {
        			e.printStackTrace();
        		} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		// finally, close the socket and decrement runningThreads
        		finally {
        			System.out.println("closing");
        			
        			try {
        				socket.close();
        				//runningThreads.decrementAndGet();
        				System.out.flush();
        			}
        			catch (IOException e ) {
        				System.out.println("Couldn't close socket");
        			}
        		}
    		}    		
		}    	
    }
}
