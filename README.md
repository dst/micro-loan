[![Build Status](https://travis-ci.org/dst/micro-loan.svg)](https://travis-ci.org/dst/micro-loan)
# micro-loan

## System dependencies
- Java 8

## Configuration
Adjust application.properties if needed.

## Starting server
For Windows use gradlew.bat

### Run from gradle

    ./gradlew clean bootRun

### Run the fat jar

    java -jar build/libs/micro-loan-0.0.1.jar

## Verification
For default application.properties:
    $ curl localhost:8888/info

## Technology stack
- back-end: Java 8, Spring Boot, Spring Data, JPA with Hibernate, embedded H2 Database, Lombok, Logback
- testing: JUnit 4, mockito, AssertJ, MockMvc, Cucumber, Groovy
- building/deploying: embedded Apache Tomcat, gradle, JaCoCo

## Development

### IDEA settings
- Fix Lombok problem when running tests: "Enable annotation processing" in Settings->Compiler->Annotation Processors

### New library
- Add gradle dependency
- Download gradle dependency: $ ./gradlew assemble
- Add IDE dependency:
    - IDEA: ./gradlew idea
    - Eclipse: ./gradlew eclipse
    
### Code coverage
    $ ./gradlew jacocoTestReport
    $ browser build/reports/jacoco/test/html/index.html

### Functional tests
    $ ./gradlew functionalTest
    $ browser build/reports/cucumber/index.html

## Example session with server:
### Create customer
    $ curl -v H "Content-Type: application/json" -d '{"firstName":"abc", "lastName":"xyz"}' localhost:8888/customers

### View customer
    $ curl -v localhost:8888/customers/1

### Create first loan for customer 1
    $ curl -H "Content-Type: application/json" -d '{"amount": 10.0}' localhost:8888/customers/1/loans

### View first loan
    $ curl localhost:8888/customer/1/loans/1

### Create second loan for customer 1
    $ curl -H "Content-Type: application/json" -d '{"amount": 20.0}' localhost:8888/customers/1/loans

### View all loans
    $ curl localhost:8888/customer/1/loans