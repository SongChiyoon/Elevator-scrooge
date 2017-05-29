import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.net.ServerSocket;

/* Server Class for Objects of RemoteContol */
/* Used in Elevator Objects */
public class remoteServer {

   // The server socket.
   private static ServerSocket serverSocket = null;
   // The client socket.
   private static Socket clientSocket = null;

   private static final int maxClientsCount = 10;
   private static final clientThread[] threads = new clientThread[maxClientsCount];
   private static scheduler mysch;

   public remoteServer(scheduler sc) {
      mysch = sc;

      // The default port number.
      int portNumber = 2222;

      /*
       * Open a server socket on the portNumber (default 2222). Note that we
       * can not choose a port less than 1023 if we are not privileged users
       * (root).
       */
      try {
         serverSocket = new ServerSocket(portNumber);
      } catch (IOException e) {
         System.out.println(e);
      }

      /*
       * Create a client socket for each connection and pass it to a new
       * client thread.
       */
      while (true) {
         try {
            clientSocket = serverSocket.accept();
            int i = 0;
            for (i = 0; i < maxClientsCount; i++) {
               if (threads[i] == null) {
                  (threads[i] = new clientThread(clientSocket, threads, mysch)).start();
                  break;
               }
            }
            if (i == maxClientsCount) {
               PrintStream os = new PrintStream(clientSocket.getOutputStream());
               os.println("Server too busy. Try later.");
               os.close();
               clientSocket.close();
            }
         } catch (IOException e) {
            System.out.println(e);
         }
      }

   }


   class clientThread extends Thread {

      private DataInputStream is = null;
      private PrintStream os = null;
      private Socket clientSocket = null;
      private final clientThread[] threads;
      private int maxClientsCount;

      private String tname; // name of client thread
      private String block[] = new String[10]; // blocked people's name
      private int blocknum; // number of blocked people
      scheduler mysch;

      public clientThread(Socket clientSocket, clientThread[] threads, scheduler sc) {
         this.clientSocket = clientSocket;
         this.threads = threads;
         maxClientsCount = threads.length;
         mysch = sc;

      }

      public void run() {
         int maxClientsCount = this.maxClientsCount;
         clientThread[] threads = this.threads;

         try {
            /*
             * Create input and output streams for this client.
             */
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            os.println("Enter your name.");
            String name = is.readLine().trim(); 
            tname = name;
            os.println("Hello " + name + " to our chat room.\nTo leave enter /quit in a new line");
            for (int i = 0; i < maxClientsCount; i++) {
               if (threads[i] != null && threads[i] != this) {
                  threads[i].os.println("*** A new user " + name + " entered the chat room !!! ***");
               }
            }
            blocknum = 0;
            Random generator = new Random();
            while (true) {
               String line = is.readLine();
               if (line.equals("exit")) {
                  break;
               }
               String tok[] = line.split(" ");
               int callfloor = Integer.parseInt(tok[0]);
               int desfloor = Integer.parseInt(tok[1]);
               int ran = generator.nextInt(200);
            if (mysch.mode == 2) {
               if (ran % 2 == 0)
                  mysch.scheduling(callfloor, desfloor, 350 + ran);
               else
                  mysch.scheduling(callfloor, desfloor, 350 - ran);
            }
            else
            {
               if(ran%2 == 0)
                     mysch.nomalscheduling(callfloor, desfloor, 350 + ran);
                  else
                     mysch.nomalscheduling(callfloor, desfloor, 350 - ran);
            }
         }
            
            for (int i = 0; i < maxClientsCount; i++) {
               if (threads[i] == this) {
                  threads[i] = null;
               }

               is.close();
               os.close();
               clientSocket.close();
            }
         }catch(

      IOException e)

      {
      }
   }
}}