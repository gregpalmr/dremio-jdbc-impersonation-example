///
// FILE: DremioJdbcImpersonationExample.java
//
// DESC: A Java JDBC client example that uses the Dremio inbound impersonation capability
//
// USAGE:
//
//        Compile the java source file
//
//             $ javac DremioJdbcImpersonationExample.java
//
//        Download the Dremio JDBC driver and set the Java class path
//
//             $ export CLASSPATH="./dremio-jdbc-driver-*.jar:."
//
//        Run the example application
//
//             $ java DremioJdbcImpersonationExample <dremio coordinator ip addr> <dremio user id> <dremio password> <delegation user id>"
//
//        OR
//
//             $ java DremioJdbcImpersonationExample 192.168.2.234 dremiouser1 dremiopassword dremiouser2"
//


// Import required packages
import java.sql.*;

public class DremioJdbcImpersonationExample {

   static String padRight(String s, int n) {
     return String.format("%-" + n + "s", s);  
   }

   static String padLeft(String s, int n) {
    return String.format("%" + n + "s", s);  
   }

   // JDBC driver 
   static final String JDBC_DRIVER = "com.dremio.jdbc.Driver";

   public static void main(String[] args) {

   //  Database credentials, passed in as args
   String dbServer = null; 
   String userId   = null;
   String password = null;
   String dbUrl    = null;
   String delegationUid = null;

   if (args.length < 4) {
     System.err.println("\n ERROR - Arguments not supplied correctly. Exiting.");
     System.err.println("\n USAGE:  java DremioJdbcImpersonationExample <dremio coordinator ip addr>  <dremio user id> <dremio password> <delegation user id>");
     System.err.println("   OR    java DremioJdbcImpersonationExample 192.168.2.234 dremiouser1 dremiopassword dremiouser2 \n");
     System.exit(1);
   } else {
      dbServer = args[0];
      userId   = args[1];
      password = args[2];
      delegationUid = args[3];
      dbUrl    = "jdbc:dremio:direct=" + dbServer + ":31010" + ";impersonation_target=" + delegationUid;
   }

   Connection conn = null;
   Statement stmt = null;
   try{
      // Register JDBC driver
      Class.forName(JDBC_DRIVER);

      // Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(dbUrl, userId, password);

      // Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;

      sql = "SELECT pickup_datetime, passenger_count, trip_distance_mi, fare_amount FROM Samples.\"samples.dremio.com\".\"NYC-taxi-trips\" WHERE EXTRACT(MONTH FROM pickup_datetime) BETWEEN 5 AND 10 LIMIT 25";

      ResultSet rs = stmt.executeQuery(sql);

      // Extract data from result set

      System.out.println("");
      System.out.println(padRight( "Pickup Datetime",  26) + 
                         " " + padLeft("Pass Count",   10) + 
                         " " + padLeft("Trip Dist Mi", 13) + 
                         " " + padLeft("Fair Amount",  12)
                         );
      System.out.println("----------------------------------------------------------------");

      while(rs.next()){
         //Retrieve by column name
         Timestamp pickupDatetime = rs.getTimestamp("pickup_datetime");
         int passengerCount 	  = rs.getInt("passenger_count");
         float tripDistanceMi 	  = rs.getFloat("trip_distance_mi");
         float fareAmount 	  = rs.getFloat("fare_amount");

         //Display values
         System.out.print(padRight(pickupDatetime.toString(),15));
         System.out.print(" "+ padLeft(Integer.toString(passengerCount), 10));
         System.out.print(" "+ padLeft(Float.toString(tripDistanceMi), 13));
         System.out.print(" "+ padLeft(Float.toString(fareAmount), 12));
         System.out.print("\n");
      }

      // Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try

   System.out.println("Goodbye!");

} //end main

} //end DremioJdbcImpersonationExample
