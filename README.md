# Employee Management System - Backend

A robust Spring Boot application for managing employees, departments, and payments with reporting capabilities.

## 🚀 Features

- **Employee Management**: CRUD operations for employees
- **Department Management**: Organize employees by departments
- **Payment Processing**: Integration with Stripe for payment handling
- **Reporting**: Generate PDF reports using JasperReports
- **RESTful API**: Well-structured API endpoints for frontend integration
- **Cross-Origin Support**: Configured for seamless frontend integration
- **Database Migration**: MySQL schema management

## 🛠️ Technologies

- **Spring Boot 3.2.0** - Main framework
- **Spring Data JPA** - Data persistence
- **MySQL** - Primary database
- **H2** - Testing database
- **JasperReports** - PDF report generation
- **Stripe API** - Payment processing
- **Java 17** - Programming language
- **Maven** - Dependency management
- **Thymeleaf** - Server-side templating (for basic views)

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Git

## 🔧 Installation & Setup

### Clone the Repository

```bash
git clone https://github.com/chakravartyharish/employee-management-backend.git
cd employee-management-backend
```

### Configure Database

The application is configured to use MySQL. Update the database configuration in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

### Configure Stripe (Optional)

If you want to use the payment features, update the Stripe API key in `application.properties`:

```properties
stripe.api.key=your_stripe_api_key_here
```

### Build and Run

```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🌐 API Endpoints

### Employee Endpoints

- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create a new employee
- `PUT /api/employees/{id}` - Update an employee
- `DELETE /api/employees/{id}` - Delete an employee

### Department Endpoints

- `GET /api/departments` - Get all departments
- `GET /api/departments/{id}` - Get department by ID
- `POST /api/departments` - Create a new department
- `PUT /api/departments/{id}` - Update a department
- `DELETE /api/departments/{id}` - Delete a department

### Report Endpoints

- `GET /api/reports/employees` - Generate employee report (PDF)
- `GET /api/reports/departments` - Generate department report (PDF)

### Payment Endpoints

- `POST /api/payments/create-payment` - Create a payment intent
- `POST /api/payments/confirm-payment` - Confirm a payment

## 🧪 Testing

```bash
# Run tests
mvn test
```

## 📦 Deployment

This application can be deployed to any Java-compatible hosting service:

1. Build the JAR file:
   ```bash
   mvn clean package
   ```

2. The JAR file will be in the `target` directory.

3. Deploy using:
   ```bash
   java -jar target/employee-management-0.0.1-SNAPSHOT.jar
   ```

## 🔄 Database Migration

The application uses JPA's auto-schema generation for development. For production, consider using a proper migration tool like Flyway or Liquibase.

A sample SQL schema is provided in `src/main/resources/db/mysql-schema.sql`.

## 🔗 Frontend Integration

This backend is designed to work with the [Employee Management Frontend](https://github.com/chakravartyharish/employee-management-frontend) application.

The CORS configuration is set up to allow requests from the frontend application running on different origins.

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

Harish Chakravarty - [GitHub Profile](https://github.com/chakravartyharish)
