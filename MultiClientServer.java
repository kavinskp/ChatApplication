
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class MultiClientServer extends ClientThread
{
    private static ServerSocket serverSocket=null;
    private static Socket clientSocket=null;
    private static final int maxxClientsCount=3;
    private static final ClientThread[] threads=new ClientThread[maxxClientsCount];
    
    public static void main(String args[]) throws IOException{
        int portNumber=new Scanner(System.in).nextInt();
        serverSocket=new ServerSocket(portNumber);
        while(true){
            clientSocket=serverSocket.accept();
            System.out.println("Accepted Client");
            int i=0;
            for(i=0;i<maxxClientsCount;i++){
                if(threads[i]==null){
                    (threads[i]=new ClientThread(clientSocket,threads)).start();
                    break;
                }
            }
            if(i==maxxClientsCount){
                PrintStream os = new PrintStream(clientSocket.getOutputStream());
                os.println("Server busy");
                os.close();
                clientSocket.close();
            }
            
        }
    }

    public MultiClientServer(Socket clientSocket, ClientThread[] threads) {
        super(clientSocket, threads);
    }
}