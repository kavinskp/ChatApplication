//
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread
{
    
    
    private Socket clientSocket = null;
    private final ClientThread threads[];
    private PrintStream os=null;
    private DataInputStream is;
    private String name;
    int maxClients,i;
    
    ClientThread(Socket s,ClientThread[] threads)
    {
        this.clientSocket=s;
        this.threads=threads;
        this.maxClients = threads.length;
    }
    public void run()
    {
        //System.out.println("ClientThread.run()");
        int maxClientsCount = this.maxClients;
        ClientThread[] threads = this.threads;
        try {
            is= new DataInputStream(clientSocket.getInputStream());
            os= new PrintStream(clientSocket.getOutputStream());
            os.println("Enter Your name: ");
            name = is.readLine().trim();
            synchronized(this){
//                for(i=0;i<maxClients;i++)
//                {
//                    if(threads[i]!=null && threads[i]==this)
//                    {
//                        os.println("*****Hi "+name+" Welcome!!********");
//                        threads[i].name=name;
//                    }
//                }
                for(i=0;i<maxClients;i++)
                {
                    if(threads[i]!=null && threads[i]!=this)
                    {
                        threads[i].os.println("****New User "+name+" Entered the Chat Room********");
                    }
                }
            }
            os.println("*****Enter  /quit to leave ********");
            while(true)
            {
                String line = is.readLine();
                if(line.startsWith("/quit"))
                    break;
                synchronized (this) {
                    System.out.println("<" + name + ">" + line);
                    for (int i = 0; i < maxClients; i++) {
                        if (threads[i] != null && threads[i].name != null) {
                            threads[i].os.println("<" + name + "> " + line);
                        }
                    }
                }
            }
            synchronized (this) {
                for (int i = 0; i < maxClients; i++) {
                    if (threads[i] != null && threads[i] != this
                            && threads[i].name != null) {
                        threads[i].os.println("*** The user " + name
                                + " is leaving the chat room !!! ***");
                    }
                }
            }
            os.println("*** Bye " + name + " ***");
            synchronized (this) {
                for (int i = 0; i < maxClients; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }
            }
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}



//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.net.Socket;
//
//class ClientThread extends Thread {
//    //Helper Class
//  private DataInputStream is = null;
//  private PrintStream os = null;
//  private Socket clientSocket = null;
//  private final ClientThread[] threads;
//  private int maxClientsCount;
//
////    
//  public ClientThread(Socket clientSocket, ClientThread[] threads) {
//    this.clientSocket = clientSocket;
//    this.threads = threads;
//    maxClientsCount = threads.length;
//  }
//
//  public void run() {
//    int maxClientsCount = this.maxClientsCount;
//    ClientThread[] threads = this.threads;
//
//    try {
//      /*
//       * Create input and output streams for this client.
//       */
//      is = new DataInputStream(clientSocket.getInputStream());
//      os = new PrintStream(clientSocket.getOutputStream());
//      os.println("Enter your name.");
//      String name = is.readLine().trim();
//      os.println("Hello " + name
//          + " to our chat room.\nTo leave enter /quit in a new line");
//      for (int i = 0; i < maxClientsCount; i++) {
//        if (threads[i] != null && threads[i] != this) {
//          threads[i].os.println("*** A new user " + name
//              + " entered the chat room !!! ***");
//        }
//      }
//      while (true) {
//        String line = is.readLine();
//        if (line.startsWith("/quit")) {
//          break;
//         
//        }
//        System.out.println("<" + name + ">" + line);
//        for (int i = 0; i < maxClientsCount; i++) {
//          if (threads[i] != null) {
//            threads[i].os.println("<" + name + "> " + line);
//          }
//        }
//      }
//      for (int i = 0; i < maxClientsCount; i++) {
//        if (threads[i] != null && threads[i] != this) {
//          threads[i].os.println("*** The user " + name
//              + " is leaving the chat room !!! ***");
//        }
//      }
//      os.println("*** Bye " + name + " ***");
//
//      /*
//       * Clean up. Set the current thread variable to null so that a new client
//       * could be accepted by the server.
//       */
//      for (int i = 0; i < maxClientsCount; i++) {
//        if (threads[i] == this) {
//          threads[i] = null;
//        }
//      }
//
//      /*
//       * Close the output stream, close the input stream, close the socket.
//       */
//      is.close();
//      os.close();
//      clientSocket.close();
//    } catch (IOException e) {
//    }
//  }
//}