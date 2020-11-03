
/**
 * Client
 */
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messArea = new JTextArea();
    private JTextField messInput = new JTextField();
    private Font font = new Font("Roboto", Font.ITALIC, 20);

    // Constructor
    public Client() {

        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",8000);
            System.out.println("connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            creatGUI();
            handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void creatGUI() {

        this.setTitle("Client Messenger");
        this.setSize(500, 500);
        this.setLocation(800, 150);
        // this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coding for the components
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

    public void startReading(){
            
        // thread-read data 
        Runnable r1 = ()->{
            System.out.println("Reader Started");

            try{
            while(true){
            
                String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(null, "Server Terminated the Chat", "Disconnect!..", JOptionPane.ERROR_MESSAGE);
                    messInput.setEnabled(false);
                    socket.close();
                    break;
                }

                // System.out.println("Server: "+msg);
                messArea.append("SERVER : "+msg+"\n");
            
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