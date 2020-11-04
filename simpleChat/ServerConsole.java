import java.io.IOException;
import java.util.*;



import common.ChatIF;

public class ServerConsole implements ChatIF{
	final public static int DEFAULT_PORT = 5555;

	EchoServer server;
	Scanner fromConsole;
	public ServerConsole(int port) {
		
		server=new EchoServer(port);
		fromConsole=new Scanner(System.in);
		try {
			server.listen();
		}catch(IOException e){
			System.out.println("Error: Can't setup connection!"
	                + " Terminating Server console.");
	      System.exit(1);
		}
	}
	
	
	public static void main(String[] args) 
	  {
	    int port =0;
	  
	    try {
	    	port=Integer.parseInt(args[0]);//Convert the 1nd command line to port number
	    }
	    catch(ArrayIndexOutOfBoundsException e) {
	    	port=DEFAULT_PORT;//if not command line input, set port as default
	    }catch (NumberFormatException e) {
			System.out.println("Please Input Number for Port.");//Check the input is number
		}
	    
	    ServerConsole console=new ServerConsole(port);
	    console.accept();  //Wait for console data
	  }

	@Override
	public void display(String message) {
		if(message.charAt(0)=='#') {
			try {
				server.processCommand(message);
			}catch (Exception e) {
				System.out.println("Illegal input.");
			}
		}else {
			server.sendToAllClients("SERVER MSG> " + message);
			System.out.println("' " + message +" ' Send to all clients");
			
		}
	}
	
	public void accept() {
		try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        display(message);
	      }
	    } 
	    catch (Exception e) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	}
	
	

}
