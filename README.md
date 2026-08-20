//--------------------------------------------------------
1. Initial Estructure of the employee managmente project
//--------------------------------------------------------
src/main/java
└── com.example.registerempledosweb
    ├── controller
    │   └── EmployeeController.java
    ├── model
    │   ├── domain
    │   │   └── Employee.java
    │   ├── request
    │   │   └── EmployeeRequest.java
    │   ├── response
    │   └── statusenum
    ├── repository
    │   └── EmployeeRepository.java
    ├── service
    │   ├── EmployeeService.java
    │   └── EmployeeServiceInterface.java
    └── RegisterApplication.java

src/main/resources
└── application.properties

//-----------------------------------
2. INITIAL CONFIGURATION of the pom.xml
//-----------------------------------

Spring Boot 2.1.6
Java 8
MySQL connector antiguo
JUnit/Spring Test (version 4)

//--------------------------------------------------------
3. CONFIGURATION OF THE pom.xmlWHEN UPDATE SPRING BOOT, JAVA,
AMONG OTHERS TO RECENT VERSIONS.
//--------------------------------------------------------

java 21 ... Se cambió javax. por jakarta.
Spring Boot 4.0.7 //no funcionó la 4.1.0 tiene el bug GrpcServerStartedEvent 
spring MVC
JPA
MySQL
Validation 
Testing (JUnit 5)
