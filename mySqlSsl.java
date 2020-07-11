// mySqlSsl.java
// object create mysql ssl connection

import java.sql.*;
import java.util.*;
import java.io.*;

public class mySqlSsl{
	protected Connection con = null;
	protected HashMap<String, String> generalInfo = new HashMap<String, String>();

	//  constructor
	public mySqlSsl(){
		f_initial("DBdata.dat");
	}

	//  constructor
	public mySqlSsl(String sFileName){
		f_initial(sFileName);
	}

	//  collect Server information
	protected void getServerInfo(){
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SHOW SESSION VARIABLES LIKE 'version%'");

			//  test coding
			//  System.out.println(stmt.toString());

			while(rs.next()){
				generalInfo.put(rs.getString("Variable_name"), rs.getString("Value"));
			}

			generalInfo.put("ssl_cipher", getCipherString());

			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT TIMEDIFF(NOW(), UTC_TIMESTAMP) as timezone");
			rs.next();
			generalInfo.put("timezone", rs.getString("timezone"));

			rs.close();
			stmt.close();

		}catch(Exception e){
			System.out.println(e);
		}
	}

	public void printServerInfo(){
		System.out.println("General server information");
		System.out.println("Product    : " + generalInfo.get("version_comment"));
		System.out.println("version    : " + generalInfo.get("version"));
		System.out.println("OS         : " + generalInfo.get("version_compile_os") + " " + generalInfo.get("version_compile_machine"));
		System.out.println("Time Zone  : " + generalInfo.get("timezone"));
		System.out.println("ssl_cipher : " + generalInfo.get("ssl_cipher"));
		System.out.println();
	}

	//  initialize function
	private void f_initial(String sFileName){
		try{
			Properties prop = new Properties();
			//  loading database connection information
			prop.load(new FileInputStream(sFileName));

			//  database connection properties
			Properties conn_prop = new Properties();
			conn_prop.setProperty("user", prop.getProperty("user"));
			conn_prop.setProperty("password", prop.getProperty("password"));
			conn_prop.setProperty("characterEncoding", prop.getProperty("characterEncoding"));
			conn_prop.setProperty("verifyServerCertificate", prop.getProperty("verifyServerCertificate"));
			conn_prop.setProperty("useSSL", prop.getProperty("useSSL"));
			conn_prop.setProperty("requireSSL", prop.getProperty("requireSSL"));

			//  create connection
			con = DriverManager.getConnection(prop.getProperty("url"), conn_prop);

			getServerInfo();
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public Connection getConnection(){
		return con;
	}

	public String getCipherString(){
		String resultString = null;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SHOW SESSION STATUS LIKE 'Ssl_cipher'");
			rs.next();
			resultString = rs.getString("Value");

			rs.close();
			stmt.close();
		} catch(Exception e){
			System.out.println(e);
		}

		return resultString;
	}
}
