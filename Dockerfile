# Step 1: Use a base image with Java and Tomcat (adjust if using Tomcat 9)
FROM tomcat:10-jdk11 
 # Or 'tomcat:9-jdk11' if using Tomcat 9

# Step 2: Set the working directory inside the container
WORKDIR /usr/local/tomcat

# Step 3: Copy your WAR file to Tomcat's webapps directory
COPY target/user-access-management-system.war /usr/local/tomcat/webapps/

# Step 4: Expose port 8080 (default port for Tomcat server)
EXPOSE 8080

# Step 5: Start Tomcat when the container runs
CMD ["catalina.sh", "run"]
