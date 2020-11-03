
/**
 * Server
 */
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.*;


public class Server extends JFrame {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declar components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messArea = new JTextArea();
    private JTextField messInput = new JTextField();
    private Font font = new Font("Roboto", Font.ITALIC, 20);
    
    //Constructor
    public Server(){

        try{
            server = new ServerSocket(8000);
            System.out.println("server is ready to accept connection");
            System.out.println("Waiting");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            creatGUI();
            handleEvents();
            startReading();
            // startWriting();

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void handleEvents() {

        messInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub


            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key released"+e.getKeyCode());
                if (e.getKeyChar()==10){
                    // System.out.println("You have pressed enter");
                    String contentToSend = messInput.getText();
                    messArea.append("ME : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messInput.setText("");
                }

            }

        });
    }
    private void creatGUI() {

        this.setTitle("Server Messanger");
        this.setSize(500,500);
        this.setLocation(100, 150);
        // this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        heading.setFont(font);
        messArea.setFont(font);
        messInput.setFont(font);
        heading.setIcon(new ImageIcon("icon.png"));

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messArea.setEditable(false);
        messInput.setHorizontalAlignment(SwingConstants.CENTER);
        // frame layout
        this.setLayout(new BorderLayout());

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messInput, BorderLayout.SOUTH);

        this.setVisible(true);



    }

    public void startReading() {
            
        // thread-read data 
        Runnable r1 = ()->{
            System.out.println("Reader Started");

            try{
            while(true){
            
                String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Client terminated the chat");
                    JOptionPane.showMessageDialog(null, "Client Terminated the Chat", "Disconnect!..", JOptionPane.ERROR_MESSAGE);
                    messInput.setEnabled(false);
                    socket.close();
                    break;
                }

                // System.out.println("Server: "+msg);
                messArea.append("CLIENT : "+msg+"\n");
            
            }
            }
            catch(Exception e){
                System.out.println("Connect out");
                messArea.append("Connection Out\n");
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