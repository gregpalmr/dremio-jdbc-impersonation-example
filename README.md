# dremio-jdbc-impersonation-example

A Java JDBC client example that uses the Dremio inbound impersonation capability

## USAGE:

### Step 1. Setup the "inbound impersonation" feature on your Dremio Enterprise cluster.

  a. Sign into your Dremio Enterprise coordinator node as an administrator.

  b. Click on the "New Query" button to bring up a new query editor.

  c. Run the ALTER SYSTEM command that updates the "exec.impersonation.inbound_policies" support key. Use the format:

          ALTER SYSTEM SET "exec.impersonation.inbound_policies"='[
          {
              proxy_principals:{
                  users:["<proxy-user-1>"]
              },
              target_principals: {
                  users:["<target-user-1>",
                         "<target-user-2>"]
              }
          },
          {    
              proxy_principals:{
                  users:["<proxy-user-2>"]
              },
              target_principals: {
                  users:["<target-user-2>,
                         "<target-user-3>",
                         "<target-user-4>"]
              }
          }
          ]'

  You can also specify groups in the principals settings, like this:

          ALTER SYSTEM SET "exec.impersonation.inbound_policies"='[
          {
              proxy_principals:{
                  groups:["<proxy_group-1>"]
              },
              target_principals: {
                  groups:["<target-group-1>",
                         "<target-group-2>"]
              }
          },
          {    
              proxy_principals:{
                  groups:["<proxy-group-2>"]
              },
              target_principals: {
                  groups:["<proxy-group-2>,
                         "<proxy-group-3>",
                         "<proxy-group-4>"]
              }
          }
          ]'

  d. Verify that the command ran correctly. Use the command:

          -- Verify the ALTER SYSTEM command
          SELECT name, string_val 
          FROM   sys.options 
          WHERE  name = 'exec.impersonation.inbound_policies'

### Step 2. Install the Dremio Sample data source

 a. In the Dremio Web UI, click on the "Add Sample Source" button.

 b. Click on the new Samples link under the "Data Lakes" sources.

 c. Click on the samples.dremio.com folder and then hover over the NYC-taxi-trips "Actions" column.

 d. Click on the "Format folder" icon and then click on the Save button.

### Step 3. Clone this git repo with the commands:

     $ git clone https://github.com/gregpalmr/dremio-jdbc-impersonation-example

     $ cd dremio-jdbc-impersonation-example/src

If you don't have the git command line tool, you can download the git repo in ZIP file format.

### Step 4. Download the Dremio JDBC Driver

Download the Dremio JDBC driver from httpd://dremio.com/drivers and place it in this directory

### Step 5. Compile the java source file

Compile the java source file  with the command:

     $ javac DremioJdbcImpersonationExample.java

Notice that the JDBC connection string specifies the `impersonation_target` property that specifies a user that is different from the connection user specified in the userId variable.

### Step 6. Set the Java class path

Set the class path  with the command:

     $ export CLASSPATH="./dremio-jdbc-driver-*.jar:."

### Step 7. Run the example JDBC client application with the command format:

     $ java DremioJdbcImpersonationExample <dremio coordinator ip addr> <dremio proxy user id> <dremio proxy user password> <target user id>"

Where <dremio proxy user id> is the user that you are connecting to the Dremio server as, and <target user id> is the user that you want to run the Dremio query as.

     for example:

     $ java DremioJdbcImpersonationExample 127.0.0.1 dremiouser1 dremiopassword dremiouser2"

          Pickup Datetime            Pass Count  Trip Dist Mi  Fair Amount
          ----------------------------------------------------------------
          2014-08-07 07:26:00.0          1          0.44          5.5
          2014-08-07 07:23:00.0          1          1.12          9.5
          2014-08-07 07:06:00.0          1          7.54         28.5
          2014-08-07 07:23:00.0          1           3.3         11.5
          2014-08-07 07:27:00.0          5          1.22          6.5
          2014-08-07 07:15:00.0          1          9.89         32.0
          2014-08-07 07:15:00.0          1          2.01         13.5
          2014-08-07 07:20:00.0          1          1.76         11.0
          2014-08-07 07:31:00.0          1          1.18          7.5
          2014-08-07 07:16:00.0          2         16.05         43.5
          2014-08-07 07:35:00.0          1          1.03          6.5
          2014-08-07 08:21:00.0          1          0.72          5.0
          2014-08-07 08:15:00.0          1          1.03          7.5
          2014-08-07 07:59:00.0          3         11.35         33.5
          2014-08-07 08:03:00.0          1          3.23         18.0
          2014-08-07 08:11:00.0          5          1.04         11.0
          2014-08-07 07:37:00.0          3          0.91          8.5
          2014-08-07 07:40:00.0          1          0.41          5.0
          2014-08-07 07:44:00.0          6          0.49          6.5
          2014-08-07 07:20:00.0          1         17.07         52.0
          2014-08-07 07:40:00.0          1          1.14          6.5
          2014-08-07 07:14:00.0          2         13.45         39.0
          2014-08-07 07:39:00.0          1          1.46          7.5
          2014-08-07 07:38:00.0          2           1.8         10.0
          2014-08-07 07:38:00.0          1           4.3         15.5

### Step 8. Verify that the impersonation was successful

a. In the Dremio Web UI, click on the Jobs link to view the job history

b. Verify that the query was run as the "impersonation_target" user and not the user specified as the connection userId.

---

Please direct questions or comments to greg@dremio.com

