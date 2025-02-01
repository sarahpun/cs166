/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
// for date and time
import java.time.LocalDateTime;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class GameRental {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of GameRental store
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public GameRental(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end GameRental

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            GameRental.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      GameRental esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the GameRental object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new GameRental (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Profile");
                System.out.println("2. Update Profile");
                System.out.println("3. View Catalog");
                System.out.println("4. Place Rental Order");
                System.out.println("5. View Full Rental Order History");
                System.out.println("6. View Past 5 Rental Orders");
                System.out.println("7. View Rental Order Information");
                System.out.println("8. View Tracking Information");

                //the following functionalities basically used by employees & managers
                System.out.println("9. Update Tracking Information");

                //the following functionalities basically used by managers
                System.out.println("10. Update Catalog");
                System.out.println("11. Update User");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewProfile(esql, authorisedUser); break;
                   case 2: updateProfile(esql, authorisedUser); break;
                   case 3: viewCatalog(esql); break;
                   case 4: placeOrder(esql, authorisedUser); break;
                   case 5: viewAllOrders(esql, authorisedUser); break;
                   case 6: viewRecentOrders(esql, authorisedUser); break;
                   case 7: viewOrderInfo(esql, authorisedUser); break;
                   case 8: viewTrackingInfo(esql, authorisedUser); break;
                   case 9: updateTrackingInfo(esql, authorisedUser); break;
                   case 10: updateCatalog(esql, authorisedUser); break;
                   case 11: updateUser(esql, authorisedUser); break;



                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(GameRental esql) {
      try {
         // ask user for info to create new user
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user phone number: ");
         String phoneNum = in.readLine();

         // insert into Users table
         String query = "INSERT INTO Users (login, password, role, favGames, phoneNum, numOverDueGames) " +
                        "VALUES ('" + login + "', '" + password + "', 'customer', '', '" + phoneNum + "', 0);";
         esql.executeUpdate(query);
         System.out.println("User has been created!");
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      } 
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(GameRental esql){
      try {
         // ask user to input login and password
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         // check if user login and password exists in Users table
         String query = String.format("SELECT * FROM Users WHERE login = '%s' AND password = '%s';", login, password);
         int userNum = esql.executeQuery(query);
         if (userNum > 0) {
            System.out.println("You have successfully logged in!");
            return login;
         } 
         // output for invalid login or password
         else {
            System.out.println("Invalid login or password!");
            return null;
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
         return null;
      } 
   }//end

// Rest of the functions definition go in here

   public static void viewProfile(GameRental esql, String login) {
      try {
          // get the user role
          String roleQuery = String.format("SELECT role FROM Users WHERE login = '%s';", login);
          List<List<String>> roleResult = esql.executeQueryAndReturnResult(roleQuery);
  
          // see if the user exists
          if (!roleResult.isEmpty()) {
            // get rid of whitespace
            String role = roleResult.get(0).get(0).trim();
  
            // if the user is a customer or employee
            if (!role.equals("manager")) {
               System.out.println("User Profile:");
               
               // get user info
               String query = String.format("SELECT * FROM Users WHERE login = '%s';", login);
               List<List<String>> result = esql.executeQueryAndReturnResult(query);
               if (!result.isEmpty()) {
                  // output user info
                  List<String> userInfo = result.get(0);
                  System.out.println("\tLogin: " + userInfo.get(0));
                  System.out.println("\tPassword: " + userInfo.get(1));
                  System.out.println("\tRole: " + userInfo.get(2));
                  System.out.println("\tFavorite Games: " + userInfo.get(3));
                  System.out.println("\tPhone Number: " + userInfo.get(4));
                  System.out.println("\tNumber of Overdue Games: " + userInfo.get(5));
               } 
               else {
                  System.out.println("User not found!");
               }
            } 
            else {
               // manager can view any user's profile
               System.out.print("Enter the login of the user whose profile you want to view: ");
               String userToView = in.readLine();
               // get user information
               String query = String.format("SELECT * FROM Users WHERE login = '%s';", userToView);
               List<List<String>> result = esql.executeQueryAndReturnResult(query);
               if (!result.isEmpty()) {
                  // output user information
                  List<String> userInfo = result.get(0);
                  System.out.println("User Profile:");
                  System.out.println("\tLogin: " + userInfo.get(0));
                  System.out.println("\tPassword: " + userInfo.get(1));
                  System.out.println("\tRole: " + userInfo.get(2));
                  System.out.println("\tFavorite Games: " + userInfo.get(3));
                  System.out.println("\tPhone Number: " + userInfo.get(4));
                  System.out.println("\tNumber of Overdue Games: " + userInfo.get(5));
               } 
               else {
                  System.out.println("User does not exist!");
               }
            }
         } 
         else {
            System.out.println("User does not exist!");
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
  }
  

   public static void updateProfile(GameRental esql, String login) {
      try {
         boolean updating = true;
         while (updating) {
            // ask user for what they would like to update
            System.out.println("What would you like to update?");
            System.out.println("1. Favorite Games");
            System.out.println("2. Password");
            System.out.println("3. Phone Number");
            System.out.println("4. I am done updating");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(in.readLine());

            String updateQuery = "";
 
            // update queries for each case
            switch (choice) {
               case 1:
                  System.out.print("Enter new favorite games: ");
                  String favGames = in.readLine();
                  updateQuery = String.format("UPDATE Users SET favGames = '%s' WHERE login = '%s';", favGames, login);
                  break;
               case 2:
                  System.out.print("Enter new password: ");
                  String password = in.readLine();
                  updateQuery = String.format("UPDATE Users SET password = '%s' WHERE login = '%s';", password, login);
                  break;
               case 3:
                  System.out.print("Enter new phone number: ");
                  String phoneNum = in.readLine();
                  updateQuery = String.format("UPDATE Users SET phoneNum = '%s' WHERE login = '%s';", phoneNum, login);
                  break;
               case 4:
                  updating = false;
                  break;
               default:
                  System.out.println("Invalid choice!");
            }

            // if update query is not empty
            if (!updateQuery.isEmpty()) {
               esql.executeUpdate(updateQuery);
               System.out.println("User update successful!");
            }
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void viewCatalog(GameRental esql) {
      try {
         int choice;
         String query = "";

         // do while loop for valid input
         do {
            // ask user what they would like to search/sort the catalog by
            System.out.println("1. Search by Genre");
            System.out.println("2. Search by Price");
            System.out.println("3. Sort by Price");
            System.out.print("Enter your choice: ");
            String choiceStr = in.readLine(); 

            // for invalid inputs
            try {
               choice = Integer.parseInt(choiceStr);
            } 
            catch (NumberFormatException e) {
               // loop for invalid input
               System.out.println("Invalid input! Please enter a number.");
               choice = -1; 
               continue;
            }

            // case based on input
            switch (choice) {
               case 1:
                  System.out.print("Enter genre: ");
                  String genre = in.readLine();
                  query = String.format("SELECT * FROM Catalog WHERE genre = '%s'", genre);
                  break;
               case 2:
                  System.out.print("Enter maximum price: ");
                  String maxPriceStr = in.readLine();
                  double maxPrice;

                  try {
                     maxPrice = Double.parseDouble(maxPriceStr);
                  } 
                  catch (NumberFormatException e) {
                     System.out.println("Invalid input. Please enter a valid price.");
                     return;
                  }

                  query = String.format("SELECT * FROM Catalog WHERE price <= %.2f", maxPrice);
                  break;
               case 3:
                  query = "SELECT * FROM Catalog";
                  break;
               default:
                  // loop for invalid choice
                  System.out.println("Invalid choice! Please try again.");
                  break;
            }
         } while (choice < 1 || choice > 3);
  
          // ask user if they want sort to be low to high or high to low
          while (true) {
              System.out.println("1. Sort by price (Low to High)");
              System.out.println("2. Sort by price (High to Low)");
              System.out.print("Enter your choice: ");
              String sortChoiceStr = in.readLine();
              int sortChoice;

              // query based on input
              try {
                  sortChoice = Integer.parseInt(sortChoiceStr);
                  if (sortChoice == 1) {
                     query += " ORDER BY price ASC;";
                     break;
                  } else if (sortChoice == 2) {
                     query += " ORDER BY price DESC;";
                     break;
                  } else {
                     // for invalid choice
                     System.out.println("Invalid choice. Please choose 1 or 2.");
                  }
              } 
              catch (NumberFormatException e) {
                  System.out.println("Invalid input. Please enter a number.");
              }
          }
  
          List<List<String>> result = esql.executeQueryAndReturnResult(query);
  
          // output the catalog based on the user's search/sort
         System.out.println("Game Catalog:");
         for (List<String> game : result) {
            System.out.println("\tID: " + game.get(0) + ", Name: " + game.get(1) + ", Genre: " + game.get(2) + ", Price: " + game.get(3));
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
  }  

   public static void placeOrder(GameRental esql, String login) {
      try {
         // store game ID in list
         List<String> gameIDs = new ArrayList<>();
         // store number of games in a list
         List<Integer> unitsOrdered = new ArrayList<>();
         double totalPrice = 0.0;
         boolean moreGames = true;
     
         while (moreGames) {
            // get game ID from user
            System.out.print("\tEnter game ID to rent: ");
            String gameID = in.readLine();
   
            // get the price for this game
            String priceQuery = String.format("SELECT price FROM Catalog WHERE gameID='%s';", gameID);
            List<List<String>> priceResult = esql.executeQueryAndReturnResult(priceQuery);
            if (!priceResult.isEmpty()) {
               double price = Double.parseDouble(priceResult.get(0).get(0));
   
               // add to list of games to rent
               gameIDs.add(gameID);
   
               // get units ordered
               System.out.print("\tEnter units ordered: ");
               int units = Integer.parseInt(in.readLine());
               unitsOrdered.add(units);
   
               totalPrice += price * units;
            } 
            else {
               System.out.println("Game with ID " + gameID + " not found in catalog. Please try again.");
               continue;
            }
     
            // ask if the user wants to add another game
            System.out.print("Would you like to add another game? (yes/no): ");
            String response = in.readLine();
            moreGames = response.equalsIgnoreCase("yes");
         }
     
         // insert new rental order into RentalOrder table
         String query = String.format(
            "INSERT INTO RentalOrder (login, noOfGames, totalPrice, orderTimestamp, dueDate) " +
            "VALUES ('%s', %d, %.2f, NOW(), NOW() + INTERVAL '7 days') RETURNING rentalOrderID;",
            login, gameIDs.size(), totalPrice
         );
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         String orderID = result.get(0).get(0);
     
         // insert each game into GamesInOrder table
         for (int i = 0; i < gameIDs.size(); i++) {
            query = String.format(
            "INSERT INTO GamesInOrder (rentalOrderID, gameID, unitsOrdered) VALUES ('%s', '%s', %d);",
            orderID, gameIDs.get(i), unitsOrdered.get(i)
            );
            esql.executeUpdate(query);
         }
     
         // insert tracking info for the order
         query = String.format(
            "INSERT INTO TrackingInfo (rentalOrderID, status, currentLocation, courierName, lastUpdateDate, additionalComments) " +
            "VALUES ('%s', 'Processing', 'Warehouse', 'Courier', NOW(), '') RETURNING trackingID;",
            orderID
         );

         List<List<String>> trackingResult = esql.executeQueryAndReturnResult(query);
         String trackingID = trackingResult.get(0).get(0);
     
         // output order info
         System.out.println("Your order has been placed!");
         System.out.printf("Total price: %.2f\n", totalPrice);
         System.out.println("Rental Order ID: " + orderID);
         System.out.println("Tracking Order ID: " + trackingID);
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }
   
   public static void viewAllOrders(GameRental esql, String login) {
      try {
         // get rental orders that match the login
         String query = String.format("SELECT rentalOrderID FROM RentalOrder WHERE login = '%s';", login);
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         
         // output all rental orders
         if (!result.isEmpty()) {
            System.out.println("Order History for " + login + ":");
            for (List<String> order : result) {
               System.out.println("\tRental Order ID: " + order.get(0));
            }
         } 
         // if there are no orders found
         else {
            System.out.println("No orders found for " + login);
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void viewRecentOrders(GameRental esql, String login) {
      try {
         // get orders that match the login, sorted by most recent, limited to 5
         String query = String.format("SELECT rentalOrderID FROM RentalOrder WHERE login = '%s' ORDER BY orderTimestamp DESC LIMIT 5;", login);
         List<List<String>> result = esql.executeQueryAndReturnResult(query);
         
         // output the recent orders
         if (!result.isEmpty()) {
             System.out.println("Recent Orders for " + login + ":");
             for (List<String> order : result) {
                 System.out.println("\tRental Order ID: " + order.get(0));
             }
         } 
         else {
             System.out.println("No recent orders found for " + login);
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void viewOrderInfo(GameRental esql, String login) {
      try {
         // ask user for the rental order ID
         System.out.print("Enter rental order ID: ");
         String rentalOrderID = in.readLine();
         
         // sql query to get order info
         String query = String.format("SELECT r.orderTimestamp, r.dueDate, r.totalPrice, t.trackingID " +
                                      "FROM RentalOrder r " +
                                      "JOIN TrackingInfo t ON r.rentalOrderID = t.rentalOrderID " +
                                      "WHERE r.rentalOrderID = '%s' AND r.login = '%s';", rentalOrderID, login);
         List<List<String>> orderResult = esql.executeQueryAndReturnResult(query);
         
         if (!orderResult.isEmpty()) {
            // output all rental order information
            List<String> orderInfo = orderResult.get(0);
            System.out.println("Rental Order Information:");
            System.out.println("\tOrder Timestamp: " + orderInfo.get(0));
            System.out.println("\tDue Date: " + orderInfo.get(1));
            System.out.println("\tTotal Price: " + orderInfo.get(2));
            System.out.println("\tTracking ID: " + orderInfo.get(3));
            
            // sql query to get games in the order and number of units
            query = String.format("SELECT c.gameID, c.gameName, g.unitsOrdered " +
                                 "FROM GamesInOrder g " +
                                 "JOIN Catalog c ON g.gameID = c.gameID " +
                                 "WHERE g.rentalOrderID = '%s';", rentalOrderID);
            List<List<String>> gamesResult = esql.executeQueryAndReturnResult(query);
            
            if (!gamesResult.isEmpty()) {
               // output the games in the order and units ordered
               System.out.println("Games in the Order:");
               for (List<String> game : gamesResult) {
                  System.out.println("\tGame ID: " + game.get(0));
                  System.out.println("\tGame Name: " + game.get(1));
                  System.out.println("\tUnits Ordered: " + game.get(2));
                  System.out.println();
               }
             } 
             else {
               System.out.println("No games found for rental order ID " + rentalOrderID);
             }
         } 
         else {
            System.out.println("Rental order ID " + rentalOrderID + " not found for customer " + login);
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void viewTrackingInfo(GameRental esql, String login) {
      try {
         // ask user for tracking ID
         System.out.print("Enter tracking ID: ");
         String trackingID = in.readLine();
         
         // sql query to get tracking information
         String query = String.format("SELECT t.rentalOrderID, t.courierName, t.currentLocation, t.status, t.lastUpdateDate, t.additionalComments " +
                                      "FROM TrackingInfo t " +
                                      "JOIN RentalOrder r ON t.rentalOrderID = r.rentalOrderID " +
                                      "WHERE t.trackingID = '%s' AND r.login = '%s';", trackingID, login);
         List<List<String>> trackingResult = esql.executeQueryAndReturnResult(query);
         
         if (!trackingResult.isEmpty()) {
            // output the tracking info
            List<String> trackingInfo = trackingResult.get(0);
            System.out.println("Tracking Information:");
            System.out.println("\tRental Order ID: " + trackingInfo.get(0));
            System.out.println("\tCourier Name: " + trackingInfo.get(1));
            System.out.println("\tCurrent Location: " + trackingInfo.get(2));
            System.out.println("\tStatus: " + trackingInfo.get(3));
            System.out.println("\tLast Update Date: " + trackingInfo.get(4));
            System.out.println("\tAdditional Comments: " + trackingInfo.get(5));
         } 
         else {
            System.out.println("Tracking ID " + trackingID + " not found for customer " + login);
         }
       } 
     catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void updateTrackingInfo(GameRental esql, String login) {
      try {
         // get user role
         String roleQuery = String.format("SELECT role FROM Users WHERE login = '%s';", login);
         List<List<String>> roleResult = esql.executeQueryAndReturnResult(roleQuery);

         // for debugging
         // System.out.println("Role Result: " + roleResult);

         if (!roleResult.isEmpty()) {
            //get rid of white space
            String role = roleResult.get(0).get(0).trim();

             // for debugging
            //  System.out.println("Role:" + role);

            // only allow manager and employee to update tracking info
            if(!role.equals("manager") && !role.equals("employee")) {
               System.out.println("You do not have permission to update tracking information!");
               return;
            }
         } 
         else {
            System.out.println("User not found!");
            return;
         }
         
         // ask user for tracking D
         System.out.print("Enter tracking ID: ");
         String trackingID = in.readLine();
         
         // get the order that matches the tracking ID
         String checkQuery = String.format("SELECT * FROM TrackingInfo WHERE trackingID = '%s';", trackingID);
         int rowCount = esql.executeQuery(checkQuery);
         
         if (rowCount > 0) {
            // ask user to enter in the updates
            System.out.println("Enter the tracking info updates:");
            System.out.print("Status: ");
            String status = in.readLine();
            System.out.print("Current Location: ");
            String currentLocation = in.readLine();
            System.out.print("Courier Name: ");
            String courierName = in.readLine();
            System.out.print("Additional Comments: ");
            String additionalComments = in.readLine();
             
            // update the tracking info in the database
            String updateQuery = String.format("UPDATE TrackingInfo " +
                                             "SET status = '%s', currentLocation = '%s', courierName = '%s', " +
                                             "additionalComments = '%s', lastUpdateDate = NOW() " +
                                             "WHERE trackingID = '%s';",
                                             status, currentLocation, courierName, additionalComments, trackingID);
            esql.executeUpdate(updateQuery);
            
            System.out.println("Tracking information has been updated successfully!");
         } 
         else {
            System.out.println("Tracking ID " + trackingID + " not found!");
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void updateCatalog(GameRental esql, String login) {
      try {
         // get user role
         String roleQuery = String.format("SELECT role FROM Users WHERE login = '%s';", login);
         List<List<String>> roleResult = esql.executeQueryAndReturnResult(roleQuery);
 
         if (!roleResult.isEmpty()) {
            // trim whitespace
            String role = roleResult.get(0).get(0).trim();
 
            // if role is not manager
            if (!role.equals("manager")) {
               System.out.println("You do not have permission to update the catalog!");
               return;
            }
         } 
         else {
            System.out.println("User not found!");
            return;
         }
 
         // get game ID
         System.out.print("Enter game ID to update: ");
         String gameID = in.readLine();
 
         // get game info from catalog that matches the gameID
         String checkQuery = String.format("SELECT * FROM Catalog WHERE gameID = '%s';", gameID);
         int rowCount = esql.executeQuery(checkQuery);
 
         if (rowCount > 0) {
            boolean updating = true;
            while (updating) {
               // ask user what info they want to update
               System.out.println("What would you like to update?");
               System.out.println("1. Game Name");
               System.out.println("2. Genre");
               System.out.println("3. Price");
               System.out.println("4. Description");
               System.out.println("5. Image URL");
               System.out.println("6. I am done updating");
               System.out.print("Enter your choice: ");
               int choice = Integer.parseInt(in.readLine());

               String updateQuery = "";

               // cases for each update
               switch (choice) {
                  case 1:
                     System.out.print("Enter new game name: ");
                     String gameName = in.readLine();
                     updateQuery = String.format("UPDATE Catalog SET gameName = '%s' WHERE gameID = '%s';", gameName, gameID);
                     break;
                  case 2:
                     System.out.print("Enter new genre: ");
                     String genre = in.readLine();
                     updateQuery = String.format("UPDATE Catalog SET genre = '%s' WHERE gameID = '%s';", genre, gameID);
                     break;
                  case 3:
                     System.out.print("Enter new price: ");
                     String price = in.readLine();
                     updateQuery = String.format("UPDATE Catalog SET price = %s WHERE gameID = '%s';", price, gameID);
                     break;
                  case 4:
                     System.out.print("Enter new description: ");
                     String description = in.readLine();
                     updateQuery = String.format("UPDATE Catalog SET description = '%s' WHERE gameID = '%s';", description, gameID);
                     break;
                  case 5:
                     System.out.print("Enter new image URL: ");
                     String imageURL = in.readLine();
                     updateQuery = String.format("UPDATE Catalog SET imageURL = '%s' WHERE gameID = '%s';", imageURL, gameID);
                     break;
                  case 6:
                     updating = false;
                     System.out.println("Done updating!");
                     break;
                  default:
                     System.out.println("Invalid choice! Please try again.");
               }
 
               if (!updateQuery.isEmpty()) {
                  esql.executeUpdate(updateQuery);
                  System.out.println("Game information has been updated successfully!");

                  // output updated info for user
                  String selectQuery = String.format("SELECT * FROM Catalog WHERE gameID = '%s';", gameID);
                  List<List<String>> updatedGameInfo = esql.executeQueryAndReturnResult(selectQuery);
                  System.out.println("Updated Game Information: " + updatedGameInfo);
               }
            }
         } 
         else {
            System.out.println("Game ID " + gameID + " not found in the catalog!");
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

   public static void updateUser(GameRental esql, String login) {
      try {
         // get user role
         String roleQuery = String.format("SELECT role FROM Users WHERE login = '%s';", login);
         List<List<String>> roleResult = esql.executeQueryAndReturnResult(roleQuery);
  
         if (!roleResult.isEmpty()) {
            // remove whitespace
            String role = roleResult.get(0).get(0).trim();

            // check if user is a manager
            if (!role.equals("manager")) {
               System.out.println("You do not have permission to update user profiles!");
               return;
            }
         } else {
            System.out.println("User not found!");
            return;
         }
  
         boolean updating = true;
         while (updating) {
            // ask manager for user ID to update
            System.out.print("Enter the login of the user to update: ");
            String userToUpdate = in.readLine();

            // get the corresponding info for the user
            String userQuery = String.format("SELECT * FROM Users WHERE login = '%s';", userToUpdate);
            int rowCount = esql.executeQuery(userQuery);
            if (rowCount == 0) {
               System.out.println("User not found!");
               continue;
            }

            while (true) {
               // ask manager what info they want to update
               System.out.println("What would you like to update for user '" + userToUpdate + "'?");
               System.out.println("1. Login");
               System.out.println("2. Password");
               System.out.println("3. Role");
               System.out.println("4. Favorite Games");
               System.out.println("5. Phone Number");
               System.out.println("6. Number of Overdue Games");
               System.out.println("7. I am done updating");
               System.out.print("Enter your choice: ");
               int choice = Integer.parseInt(in.readLine());

               String updateQuery = "";

               // cases for each choice
               switch (choice) {
                  case 1:
                     System.out.print("Enter new login: ");
                     String newLogin = in.readLine();
                     updateQuery = String.format("UPDATE Users SET login = '%s' WHERE login = '%s';", newLogin, userToUpdate);
                     break;
                  case 2:
                     System.out.print("Enter new password: ");
                     String newPassword = in.readLine();
                     updateQuery = String.format("UPDATE Users SET password = '%s' WHERE login = '%s';", newPassword, userToUpdate);
                     break;
                  case 3:
                     System.out.print("Enter new role: ");
                     String newRole = in.readLine();
                     updateQuery = String.format("UPDATE Users SET role = '%s' WHERE login = '%s';", newRole, userToUpdate);
                     break;
                  case 4:
                     System.out.print("Enter new favorite games: ");
                     String newFavGames = in.readLine();
                     updateQuery = String.format("UPDATE Users SET favGames = '%s' WHERE login = '%s';", newFavGames, userToUpdate);
                     break;
                  case 5:
                     System.out.print("Enter new phone number: ");
                     String newPhoneNum = in.readLine();
                     updateQuery = String.format("UPDATE Users SET phoneNum = '%s' WHERE login = '%s';", newPhoneNum, userToUpdate);
                     break;
                  case 6:
                     System.out.print("Enter new number of overdue games: ");
                     int numOverdueGames = Integer.parseInt(in.readLine());
                     updateQuery = String.format("UPDATE Users SET numOverDueGames = %d WHERE login = '%s';", numOverdueGames, userToUpdate);
                     break;
                  case 7:
                     updating = false;
                     break;
                  default:
                     System.out.println("Invalid choice! Please enter a number between 1 and 7.");
                     continue;
               }

               // if manager is done updating
               if (choice == 7) {
                  break;
               }

               if (!updateQuery.isEmpty()) {
                  esql.executeUpdate(updateQuery);
                  System.out.println("Update successful for user '" + userToUpdate + "'!");
               }
            }
         }
      } 
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
  }
}//end GameRental

