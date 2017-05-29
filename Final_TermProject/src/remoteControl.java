import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/* Class that controlls Elevator using socket */
public class remoteControl {
   private static Socket clientSocket = null;
   private static PrintStream os = null;
   private static DataInputStream is = null;

   private static BufferedReader inputLine = null;
   private static boolean closed = false;
   public static String Input="";
   remoteGUI rG = new remoteGUI(this);

   public remoteControl(int portNumber, String host) {
      try {
         clientSocket = new Socket(host, 2222);
         inputLine = new BufferedReader(new InputStreamReader(System.in));
         os = new PrintStream(clientSocket.getOutputStream());
         is = new DataInputStream(clientSocket.getInputStream());
      } catch (UnknownHostException e) {
         System.out.println("Don't know about host " + host + "\n");
      } catch (IOException e) {
         System.out.println("Couldn't get I/O for the connection to the host " + host + "\n");
      }
      os.println("");
      if (clientSocket != null && os != null && is != null) {
         while (!closed) {
            String input;
            input = Input; 
            if (!input.equals("")) {
            }
         }
      }
   }

   public void getInput(int src, int dest) {
      this.Input = "" + src + " " + dest;
      if (this.Input.equals("1 1")) {
         try {
            is.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         os.close();
         try {
            clientSocket.close();
         } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
         }
         System.exit(1);
      }
      os.println(Input);
      System.out.println(this.Input);
   }

   public static void main(String[] args) {
      remoteControl re = new remoteControl(2222, "localhost");  
   }
}