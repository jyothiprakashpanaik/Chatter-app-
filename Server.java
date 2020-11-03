/**
 * Server
 */
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.*;


public class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //Constructor
    public Server(){

        try{
            server = new ServerSocket(8000);
            System.out.println("server is ready to accept connection");
            System.out.println("Waiting");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }
        catch(Exception e) {
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
                    System.out.println("Client terminated the chat");

                    socket.close();
                    break;
                }

                System.out.println("Client: "+msg);
            
            }
            }
            catch(Exception e){
                System.out.println("connection closed");;
            }

        };

        new Thread(r1).start();

    }

    public void startWriting(){

        //thread-wright data
        Runnable r2 = () -> {
            System.out.println("Writter Started");

            try{
            while(true &&  !socket.isClosed()){
            
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
                System.out.println("connection closed");
            }

        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is Server");
        new Server();
    }
}