import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Setting {
	Connection con;
	private double a;

	Setting() {
		a = 0.3;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/elevator", "root", "ss19484255");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void update(int name, int floor,double setF) {
		double Floor = setF;
		Floor = Floor*(1-a) + a*floor;
	
		try{
		Statement stmt = con.createStatement();
		stmt.executeUpdate("update elevator set floor = "+Floor+"where name ="+name+";");	
	
		stmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public double callfloor(int name) {
		double floor = 0;
		try {
	
			Statement stmt2 = con.createStatement();
			ResultSet rs = stmt2.executeQuery("select floor from elevator where name = '" + name + "';");
			ResultSetMetaData rsmd = rs.getMetaData();

			int colnum = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= colnum; i++)
					floor = rs.getDouble(i);
			}
			rs.close();
			stmt2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return floor;
	}

}