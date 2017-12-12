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
       
        int maxClientsCount = this.maxClients;
        ClientThread[] threads = this.threads;
        try {
            is= new DataInputStream(clientSocket.getInputStream());
            os= new PrintStream(clientSocket.getOutputStream());
            os.println("Enter Your name: ");
            name = is.readLine().trim();
            synchronized(this){
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
