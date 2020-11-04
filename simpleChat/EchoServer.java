// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  String loginid;
  boolean allowConnect=false;
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  String msgString=msg.toString();
	
	  System.out.println(msgString);
	  
	  
	  //set client info for login id 
	if(msgString.contains("#login<")) {
		loginid=msgString.substring(7,msgString.length()-1);
		try {
			client.sendToClient("#confirmConnect"+"<"+loginid+">");//send back confirmation for connecting
		} catch (Exception e) {
			// TODO: handle exception
		}
		client.setInfo("loginid", loginid);
	}
	
	// set the indicator for first #login input
	Object tempObject="#login";
	Object indicator="1";
	if(msg.equals(tempObject)) {
		
			if(indicator.equals(client.getInfo("authrity"))) {
				sendToAllClients(loginid+"<< Error Connecting, close connection  >>");
				try {
					client.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else {
				client.setInfo("authrity", indicator);
			}	
	}
	
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients("to <"+client.getInfo("loginid")+"> "+msg);
  }
    
  public void processCommand(String message) throws IOException {
	  String dataString="";
	  if(message.contains("<")) {
			 dataString=message.substring(9,message.length()-1);
			 message=message.substring(0,8);
		 }
	switch (message) {
		case "#quit": 
			if (isListening()) {
				System.out.println("Please stop listening first.");
			}else {
				close();
				System.out.println("Server has shutted down");
				System.exit(0);
			}
			break;
		case "#stop":
			stopListening();
			System.out.println("Server has stopped listening");
			break;
		case "#close":
			close();
			System.out.println("Server has closed");
			break;
		case "#setport":
			if (isListening()) {
				System.out.println("Please stop listening first.");
			}else {
				try {
					
					setPort(Integer.parseInt(dataString));
					System.out.println("Port set to "+dataString);
				}catch (Exception e) {
					System.out.println("Please input valid port.");
				}
			}
			break;
		case "#start":
			if (isListening()) {
				System.out.println("Already started listening.");
			}else {
				listen();
			}
			break;
		case "#getport":
				int port=getPort();
				System.out.println("Port: "+port);
			break;
		default:
			throw new IllegalArgumentException("Please input a valid command.");
		}
  }
	  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
 EchoServer sv=new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  
  /**
   * This method is responsible for showing the server has a connection.
   */
  @Override  
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A client has connected to the server.");
  }
  
  /**
   * This method is responsible for showing the server has a disconnection.
   */
  
  @Override 
  protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("A client has disconnected to the server.");
  }

  public void saveId(ConnectionToClient client) {
	  
	  
  }
  
}
//End of EchoServer class
