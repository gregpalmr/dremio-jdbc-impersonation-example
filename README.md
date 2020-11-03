# dremio-jdbc-impersonation-example

A Java JDBC client example that uses the Dremio inbound impersonation capability

USAGE:

### Step 1. Clone this git repo with the command:

          $ git clone https://github.com/gregpalmr/dremio-jdbc-impersonation-example

          If you don't have the git command line tool, you can download the git repo in ZIP file format.

### Step 2. Download the Dremio JDBC Driver from httpd://dremio.com/drivers and place it in this directory

### Step 3. Compile the java source file with the command:

          $ javac DremioJdbcImpersonationExample.java

### Step 4. Set the Java class path with the command:

          $ export CLASSPATH="./dremio-jdbc-driver-*.jar:."

### Step 5. Setup the "inbound impersonation" feature on your Dremio Enterprise cluster.

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

### Step 6. Run the example JDBC client application with the command format:

          $ java DremioJdbcImpersonationExample <dremio coordinator ip addr> <dremio proxy user id> <dremio proxy user password> <target user id>"

          Where <dremio proxy user id> is the user that you are connecting to the Dremio server as, and <target user id> is the user that you want to run the Dremio query as.

          for example:

          $ java DremioJdbcImpersonationExample 127.0.0.1 dremiouser1 dremiopassword dremiouser2"

---

Please direct questions or comments to greg@dremio.com

