# User Access Management System

A web-based application for managing user access to software applications within an organization. The system allows users to sign up, request access to software applications, and enables managers to approve or reject these requests.

## Features

- User Registration (Sign-Up)
- User Authentication (Login)
- Software Application Listing and Creation (Admin)
- Access Request Submission (Employee)
- Access Request Approval or Rejection (Manager)

## Technologies Used

- Java Servlets
- JavaServer Pages (JSP)
- PostgreSQL Database
- Bootstrap 5 for UI
- Maven for dependency management

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Maven
- PostgreSQL Database
- Apache Tomcat 9.x

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd user-access-management
   ```

2. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE access_management;
   ```

3. Update database configuration in `src/main/java/com/accessmanagement/util/DatabaseUtil.java`:
   ```java
   config.setJdbcUrl("jdbc:postgresql://localhost:5432/access_management");
   config.setUsername("your_username");
   config.setPassword("your_password");
   ```

4. Build the project:
   ```bash
   mvn clean package
   ```

5. Deploy the WAR file to Tomcat:
   - Copy the generated WAR file from `target/user-access-management.war` to Tomcat's `webapps` directory
   - Start Tomcat server

6. Access the application:
   - Open a web browser and navigate to `http://localhost:8080/user-access-management`

7. Run the verification script:
   ```
   .\verify_install.bat
   ```

## User Roles

1. Employee
   - Can sign up and create an account
   - Can log in to the system
   - Can request access to software applications
   - Cannot approve or reject access requests
   - Cannot create new software applications

2. Manager
   - Can log in to the system
   - Can view pending access requests
   - Can approve or reject access requests
   - Cannot request access to software applications
   - Cannot create new software applications

3. Admin
   - Can log in to the system
   - Has all the privileges of an Employee and Manager
   - Can create new software applications
   - Can manage system settings

## Security Considerations

- In a production environment, implement proper password hashing
- Use HTTPS for secure communication
- Implement proper input validation and sanitization
- Add CSRF protection
- Implement rate limiting for login attempts
- Add proper logging and monitoring

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 