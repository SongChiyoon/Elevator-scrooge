import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class client {
	@SuppressWarnings("resource")
	Socket clientSocket = null;
	DataInputStream is = null;
	PrintStream os = null;
	DataInputStream inputLine = null;

	public client()  {
		BufferedReader inputStream = null;

		String IP;
		int portnum = 3333;

		IP = "localhost"; //database server's IP

		try {
			clientSocket = new Socket(IP, portnum);
			os = new PrintStream(clientSocket.getOutputStream());
			is = new DataInputStream(clientSocket.getInputStream());
			inputLine = new DataInputStream(new BufferedInputStream(System.in));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to host");
		}
	}
	double callToServer(int name)
	{
		double result=0;
		os.println("call "+name);
		try {
			String responseLine = is.readLine();
			result = Double.parseDouble(responseLine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	void updateToServer(int name,int floor,double setF)
	{
		os.println("update "+name+" "+floor+" "+setF);
	}
	void disconnect() {
		os.close();
		try {
			is.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}