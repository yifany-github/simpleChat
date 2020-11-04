// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.security.MessageDigest;



/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginid;
  boolean connectPermission=false;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginid,String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginid=loginid;
    this.clientUI = clientUI;
    openConnection();
    sendToServer("#login<"+loginid+">");
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
 * @throws IOException 
   */
  public void handleMessageFromServer(Object msg)
  {
	  Object confirm="#confirmConnect"+"<"+loginid+">";
	  
	  if(msg.equals(confirm)){
		  connectPermission=true;
	  }
	  if(connectPermission) {
		  clientUI.display(msg.toString()); 	  
	  }else {
		  System.out.println("Server declined the connection");
		  System.out.println("Client Shut Down");
		  System.exit(1);
	  }
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
 * @throws IOException 
   */
  public void handleMessageFromClientUI(String message) 
  {
	  
			 if(message.charAt(0)=='#') {
					try {
						processCommand(message);
					}catch(IOException ex) {
						System.out.println("error");
					}
					
				}else {
					try
				    {
				      sendToServer(message);
				      System.out.println("Sent it");
				    }
				    catch(IOException e)
				    {
				      clientUI.display
				        ("Could not send message to server.  Terminating client.");
				      quit();
				    }
				}
			
		
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
   * This method is responsible for showing a connection closed.
   */
  @Override protected void connectionClosed() {
	  System.out.println("Connection is Closed.");
  }
  /**
   * This method is responsible for showing the server has shut down.
   */
  @Override protected void connectionException(Exception exception) {
	  System.out.println("Server Has Shut Down! Quitting...");
  }
  
  /**
   * This method is responsible for handle command info.
   */
  
  public void processCommand(String message) throws IOException {
	  String dataString="";
	 if(message.contains("<")) {
		 dataString=message.substring(9,message.length()-1);
		 message=message.substring(0,8);
	 }
	 
	switch (message) {
		case "#quit": 
			if(isConnected()) {
				System.out.println("Please log off first.");
		}else {
			System.out.println("Shut down.....");
			System.exit(0);
		}
			break;
		case "#logoff":
			closeConnection();
			System.out.println("Disconnected.");
			break;
		case "#sethost":
			if(isConnected()) {
			System.out.println("Please log off first.");
			}else {
			try {
				setHost(dataString);
				System.out.println("Host set to "+dataString);
			}catch (Exception e) {
				System.out.println("Please input valid host.");
			}
		}
			break;
		case "#setport":
			if(isConnected()) {
				System.out.println("Please log off first.");
			}else {
			try {
				
				setPort(Integer.parseInt(dataString));
				System.out.println("Port set to "+dataString);
			}catch (Exception e) {
				System.out.println("Please input valid port.");
			}
		}
		
			break;
		case "#login":
			//if(isConnected()) {
			//System.out.println("Already Connected.");
			//}else {
			openConnection();
			sendToServer("#login");
			System.out.println("Connected.");
			//}
			break;
		case "#gethost":
			String host=getHost();
			System.out.println("Host: "+host);
			break;
		case "#getport":
			int port=getPort();
			System.out.println("Port: "+port);
			break;
		default:
			throw new IllegalArgumentException("Please input a valid command.");
	}
  }
  
  
}
//End of ChatClient class
