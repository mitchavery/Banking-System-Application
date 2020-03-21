import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	
	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		}
	    catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE\n");
			} catch (Exception e) {
				println(":: TEST - FAILED CONNECTED TO DATABASE");
				e.printStackTrace();
			}
	  }
	
	private static void println(String str) {
		System.out.println(str);
	}

	private static void closeConn(ResultSet rs, Statement stmt, Connection con) {
		try {
			rs.close();                                                                             
			stmt.close();                                                                           
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static boolean newCustomer(String name, String gender, String age, String pin) 
	{
		try{
			int id = -1;
			println(":: CREATE NEW CUSTOMER - RUNNING");
			Class.forName("com.ibm.db2.jcc.DB2Driver");                             
	    	con = DriverManager.getConnection (url, username, password);                 
	  		con.setAutoCommit(false);
			stmt = con.createStatement(); 
			String query = String.format("Insert into P1.CUSTOMER(Name, Gender, Age, Pin) values ('%s', '%s', %s, %s)", name, gender, age, pin); 
			stmt.executeUpdate(query);
			query = "select id from P1.Customer where name='"+name+"' and gender='"+gender+"' and age='"+age+"'";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				id = rs.getInt("ID");
			}
			stmt.close();
			con.commit();
			con.close();
			println(":: CREATE NEW CUSTOMER - SUCCESS\n");
			println("ID: " + id);
			return true;
		}
		catch(Exception e){
			println("Error: Invalid values inputted");
			return false;
		}
	}
	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) 
	{
		try{
			int accNum = -1;
			println(":: OPEN ACCOUNT - RUNNING");
			Class.forName("com.ibm.db2.jcc.DB2Driver");                             
	    	con = DriverManager.getConnection (url, username, password);                 
	  		con.setAutoCommit(false);
			stmt = con.createStatement(); 
			String sqlinsert = String.format("Insert into p1.account(ID, Balance, Type, Status) values (%s, %s, '%s', 'A')", id, amount, type);
			stmt.executeUpdate(sqlinsert);
			String getAccNum = "select Number from p1.account where id ='"+id+"'";
			ResultSet rs = stmt.executeQuery(getAccNum);
			while(rs.next()) {
				accNum = rs.getInt("Number");
			}
			stmt.close();
			con.commit();
			con.close();
			println(":: OPEN ACCOUNT - SUCCESS\n");
			println("Account Number: " + accNum);
		}
		catch(Exception e){
			println("Error in opening account");
		}
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) 
	{
		try{
			println(":: CLOSE ACCOUNT - RUNNING");
			Class.forName("com.ibm.db2.jcc.DB2Driver");                             
	    	con = DriverManager.getConnection (url, username, password);                 
	  		con.setAutoCommit(false);
			stmt = con.createStatement();
			String updatequery = String.format("Update p1.account set balance = 0, status = 'I' where Number = %s and status = 'A' and ID = %s", accNum, p1.loginId);
			stmt.executeUpdate(updatequery);
			stmt.close();
			con.commit();
			con.close();
			println(":: CLOSE ACCOUNT - SUCCESS\n");
		}
		catch(Exception e){
        	println("Error in closing account");
		}
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) 
	{
		try{
			println(":: DEPOSIT - RUNNING");
			Class.forName("com.ibm.db2.jcc.DB2Driver");                             
	    	con = DriverManager.getConnection (url, username, password);                 
	  		con.setAutoCommit(false);
			stmt = con.createStatement(); 
			String query = String.format("Update p1.account set Balance = Balance + %s where Number = %s and status = 'A'", amount, accNum);
			stmt.executeUpdate(query);
			stmt.close();
			con.commit();
			con.close();
			println(":: DEPOSIT - SUCCESS\n");
		}
		catch(Exception e){
			println("Deposit Error");
		}
	}

	public static boolean isOwner(String accountNumber)  {
		try{
			println(":: VALIDATING ACCOUNT NUMBER");
			String sqlquery = "Select ID from p1.account where Number = " + accountNumber;
			Class.forName(driver);                                                                  
			Connection con = DriverManager.getConnection(url, username, password);                  
			Statement stmt = con.createStatement();                                                 
			ResultSet rs = stmt.executeQuery(sqlquery);
			while(rs.next()) {                                                                      
				String id = "" + rs.getInt(1);
				if(id.equals(p1.loginId)){
					println(":: VALIDATION COMPLETE");
					return true;
				} 
			} 
			println(":: INVALID ACCOUNT NUMBER\n");							
			closeConn(rs, stmt, con);
			return false;
		} catch (Exception e) {
			println("Error in validating account");
        	return false;
      }
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) 
	{
		try{
			println(":: WITHDRAW - RUNNING");
			Class.forName("com.ibm.db2.jcc.DB2Driver");                             
	    	con = DriverManager.getConnection (url, username, password);                 
	  		con.setAutoCommit(false);
			stmt = con.createStatement(); 
			String query = String.format("Update p1.account set Balance = Balance - %s where Number = %s and status = 'A'", amount, accNum);
			stmt.executeUpdate(query);
			stmt.close();
			con.commit();
			con.close();
			println(":: WITHDRAW - SUCCESS\n");
		}
		catch(Exception e){
			println("Withdraw Error");
		}
	}

	//authenticate 
	public static boolean login(String id, String pin) {
		try {
			System.out.println(":: LOGGING IN...");
			String sqlquery = String.format("Select id from p1.Customer where id = %s and pin = %s", id, pin);
			Class.forName(driver);                                                                  
			Connection con = DriverManager.getConnection(url, username, password);                  
			Statement stmt = con.createStatement();                                                 
			ResultSet rs = stmt.executeQuery(sqlquery);
			while(rs.next()) {                                                                      
			  int idNum = rs.getInt(1);
			  if(id.equals("" + idNum)){
				  println(":: LOG IN COMPLETE");
				  return true;
				} 
			}         
			println(":: PLEASE INPUT VALID CREDENTIALS");					  
			closeConn(rs, stmt, con);
			return false;
			} catch (Exception e) {
				println("Error in authenticating user");
				return false;
		  	}
		}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) 
	{		
		try{
			println(":: TRANSFER - RUNNING");
			Class.forName("com.ibm.db2.jcc.DB2Driver");                             
	    	con = DriverManager.getConnection (url, username, password);                 
	  		con.setAutoCommit(false);
			stmt = con.createStatement();
			String query = String.format("Update p1.account set Balance = Balance - %s where Number = %s and status = 'A'", amount, srcAccNum);
			stmt.addBatch(query);
			String query2 = String.format("Update p1.account set Balance = Balance + %s where Number = %s and status = 'A'", amount, destAccNum);
			stmt.addBatch(query2);
			stmt.executeBatch();
			stmt.close();
			con.commit();
			con.close();
			println(":: TRANSFER - SUCCESS\n");
		} catch(Exception e){
			println("Transfer Error");
		}
	}

	/**
	 * Display account summary.
	 * @param accNum account number
	 */
	public static void accountSummary(String accNum)
	{
    	try {
			println(":: ACCOUNT SUMMARY - RUNNING");
			Class.forName(driver);                                                                  
			Connection con = DriverManager.getConnection(url, username, password);                  
			Statement stmt = con.createStatement();                                                 
			String query = String.format("Select Number, Balance from p1.account where ID = %s and status = 'A'", accNum);
			int total = 0;
			println("    Number     Balance");
			println("______________________");
			ResultSet rs = stmt.executeQuery(query);                                                
			while(rs.next()) {                                                                      
				int Number = rs.getInt(1);
				int Balance = rs.getInt(2);
				total += Balance;
				System.out.printf("%10s  %10s\n", Number, Balance);      
			}
			println("\nTotal balance:  " + total);
			closeConn(rs, stmt, con);
			println(":: ACCOUNT SUMMARY - SUCCESS\n");                                                                     
      	} catch (Exception e) {
        	println("Error in getting account summary");
      	}
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() 
	{	
      try {
			println(":: REPORT A - RUNNING");
			Class.forName(driver);                                                                  
			Connection con = DriverManager.getConnection(url, username, password);                  
			Statement stmt = con.createStatement();                                                 
			String query = "Select ID, Name, Gender, Age, PIN, TOTAL from p1.customer Join (select ID as accountID,sum(balance) as TOTAL from p1.account group by id) on accountID = p1.customer.id order by Total Desc";
			println("        ID             Name      Gender         Age         Pin       Total");
			println("___________________________________________________________________________");
			ResultSet rs = stmt.executeQuery(query);                                                
			while(rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String gender = rs.getString(3);
				int age = rs.getInt(4);
				int pin = rs.getInt(5);
				int total = rs.getInt(6);
				System.out.printf("%10s  %15s  %10s  %10s  %10s  %10s\n", id, name, gender, age, pin , total); 
        }
        closeConn(rs, stmt, con);
		println(":: REPORT A - SUCCESS\n");
		} catch (Exception e) {
        	println("Error in getting report A");
      	}
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) 
	{
      try {
		println(":: REPORT B - RUNNING");
		String sqlquery = String.format("Select AVG(balance) from p1.account JOIN p1.customer ON p1.customer.id = p1.account.id where p1.customer.age >=%s AND p1.customer.age <= %s and p1.account.status = 'A'", min, max);
		Class.forName(driver);                                                                  
        Connection con = DriverManager.getConnection(url, username, password);                  
        Statement stmt = con.createStatement();                                                 
        ResultSet rs = stmt.executeQuery(sqlquery);                                                
        while(rs.next()) {
        	int total = rs.getInt(1);
			println("   AVERAGE");
			println("__________");
        	System.out.printf("%10s\n", total);
        }
        closeConn(rs, stmt, con);
		println(":: REPORT B - SUCCESS\n");
		} catch (Exception e) {
        	println("Error in getting report B");
		}
	}
}
