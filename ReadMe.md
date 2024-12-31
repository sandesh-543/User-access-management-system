# User Access Management System

## Overview
The User Access Management System is a web application designed to manage user registrations, logins, and role-based access control. It uses Java, JSP, and servlets for the backend, and PostgreSQL for the database. The project is built using Maven and utilizes HikariCP for database connection pooling.

## Features
- User registration with input validation
- Password hashing using BCrypt
- Role-based access control (Admin, Manager, Employee)
- Session management
- Error handling and user feedback
- Database connection pooling

## Prerequisites
- Java 22
- Maven
- PostgreSQL
- Servlet container (e.g., Tomcat)

## Setup

### Database Configuration
1. Create a PostgreSQL database named `UserAccessManagement`.
2. Create a user with the username `YOUR_USERNAME` and password `YOUR_PASSWORD`.
3. Update the database connection details in `src/main/java/com/example/UserAccessManagementSystem/utils/DatabaseConnection.java` to match your setup.

### Maven Dependencies
Ensure the following dependencies are included in your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.4</version>
    </dependency>
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.1.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.5.15</version>
    </dependency>
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>6.2.1</version>
    </dependency>
</dependencies>
```
## Steps to Run

### Cloning the Repository
1. Clone the repository:
    ```sh
    git clone https://github.com/sandesh-543/user-access-management-system.git
    cd user-access-management-system
    ```

### Building the Project
2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

### Creating the WAR File
3. The WAR file will be generated in the `target` directory. Ensure it is named `user-access-management-system.war`.

### Initializing Tomcat
4. Download and install Apache Tomcat from [Tomcat's official website](https://tomcat.apache.org/).
5. Copy the `user-access-management-system.war` file to the `webapps` directory of your Tomcat installation.

### Starting Tomcat
6. Start the Tomcat server:
    ```sh
    cd /path/to/tomcat/bin
    ./startup.sh
    ```

### Accessing the Application
7. Open your web browser and navigate to:
    ```
    http://localhost:8080/user-access-management-system
    ```