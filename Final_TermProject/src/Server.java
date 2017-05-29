import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server {
	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
		Setting set = new Setting();
		ServerSocket echoServer = null;
		String line;
		DataInputStream is;
		PrintStream os;
		Socket clientSocket = null;

		try {
			echoServer = new ServerSocket(3333);
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("The server started. To stop it press <CTRL><C>.");
		try {
			clientSocket = echoServer.accept();
			is = new DataInputStream(clientSocket.getInputStream());

			os = new PrintStream(clientSocket.getOutputStream());

			while (true) {
				line = is.readLine();
				if (line.startsWith("call")) {
					String tok[] = line.split(" "); // divide message each
													// space
					os.println(set.callfloor(Integer.parseInt(tok[1])));
				}
				if (line.startsWith("update")) {
					String tok[] = line.split(" "); // divide message each
													// space
					set.update(Integer.parseInt(tok[1]), Integer.parseInt(tok[2]),Double.parseDouble(tok[3]));
					os.println(set.callfloor(Integer.parseInt(tok[1])));
				}

			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
