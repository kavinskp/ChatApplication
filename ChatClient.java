
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

class ChatClient implements Runnable
{
    private static Socket clientSocket=null;
    private static PrintStream os = null;
    private static DataInputStream is=null;
    private static BufferedReader inputLine=null;
    private static boolean closed = false;
    
    public static void main(String[] args) throws IOException {
        
        int portNumber = new Scanner(System.in).nextInt();
        String host="localhost";
        clientSocket = new Socket(host, portNumber);
        inputLine = new BufferedReader(new InputStreamReader(System.in));
        os = new PrintStream(clientSocket.getOutputStream());
        is = new DataInputStream(clientSocket.getInputStream()); 
       
        if (clientSocket != null && os != null && is != null) {
           
        new Thread((Runnable) new ChatClient()).start();
        while(!closed){
              os.println(inputLine.readLine().trim());
        }
        os.close();
        is.close();
        clientSocket.close();
     
          
      }
    }
    
    public void run(){
        String response;
        try{
            while((response=is.readLine())!=null){
            System.out.println(response);
            
        }
        closed=true;
    }
        catch(IOException e){
            System.err.println("IOException "+e);
        }
    }
    
    
}
