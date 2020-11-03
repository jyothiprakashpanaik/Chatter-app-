
/**
 * Client
 */
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.*;


public class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Constructor
    public Client(){
        
        try{
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",8000);
            System.out.println("connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
    }

    public void startReading(){
            
        // thread-read data 
        Runnable r1 = ()->{
            System.out.println("Reader Started");

            try{
            while(true){
            
                String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    socket.close();
                    break;
                }

                System.out.println("Server: "+msg);
            
            }
            }
            catch(Exception e){
                System.out.println("Connect out");
            }

        };

        new Thread(r1).start();

    }

    public void startWriting(){

        //thread-wright data
        Runnable r2 = () -> {
            System.out.println("Writter Started");
            try{
            while(true && !socket.isClosed()){
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                String content = br1.readLine();
                out.println(content);
                out.flush();

                if (content.equals("exit")){
                    socket.close();
                    break;
                }
                
            }
            }
            
            catch(Exception e){
                System.out.println("Connection Out");
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is Client");
        new Client();
    }
}