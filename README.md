# micro-loan
[![Build Status](https://travis-ci.org/dst/micro-loan.svg)](https://travis-ci.org/dst/micro-loan)
[![Coverage Status](https://coveralls.io/repos/dst/micro-loan/badge.png)](https://coveralls.io/r/dst/micro-loan)

## System dependencies
- Java 8

## Configuration
Default port is 8888. It can be changed in application.properties if needed.

## Starting server
For Windows use gradlew.bat

### Run from gradle
    ./gradlew clean bootRun

### Run the fat jar
    java -jar build/libs/micro-loan-0.0.1.jar

## Verification
    $ curl localhost:8888/info

## Technology stack
- back-end: Java 8, Spring Boot, Spring Data, JPA with Hibernate, embedded H2 Database, Lombok, Logback
- testing: JUnit 4, mockito, AssertJ, MockMvc, Cucumber, Groovy, Spock
- building/deploying: embedded Apache Tomcat, gradle, JaCoCo

## Example session with server
Add -v for verbose output.

### Customer creates account
    $ curl -H "Content-Type: application/json" -d '{"firstName":"abc", "lastName":"xyz"}' localhost:8888/customers
    
    output: {"id":1}

### Customer views his account
    $ curl localhost:8888/customers/1
    
    output: {"id":1,"firstName":"abc","lastName":"xyz"}

### Customer loans 1000 PLN for 30 days (first loan)
    $ curl -H "Content-Type: application/json" -d '{"amount": 1000.00, "daysCount": 30}' localhost:8888/customers/1/loans
    
    output: {"id":1}

### Customer views first loan
    $ curl localhost:8888/customers/1/loans/1
    
    output: {"id":1,"amount":1000.00,"interest":9.00,"extensions":[],"start":"2014-06-14","end":"2014-07-14"}
    
### Customer extends first loan
    $ curl -H "Content-Type: application/json" -d '{}' localhost:8888/customers/1/loans/1/extensions
    
    output: {"id":1}
    
### Customer views first loan with extension
Interest was multiplied by 1.5 and deadline was extended by 7 days.
 
    $ curl localhost:8888/customers/1/loans/1
    
    output: {"id":1,"amount":1000.00,"interest":13.50,"extensions":[{"id":1,"creationTime":"2014-06-14"}],"start":"2014-06-14","end":"2014-07-21"}

### Customer loans 500.50 PLN for 15 days (second loan)
    $ curl -H "Content-Type: application/json" -d '{"amount": 500.50, "daysCount": 15}' localhost:8888/customers/1/loans
    
    output: {"id":2}

### Customer views all loans
    $ curl localhost:8888/customers/1/loans
    
    output: [{"id":1,"amount":1000.00,"interest":13.50,"extensions":[{"id":1,"creationTime":"2014-06-14"}],"start":"2014-06-14","end":"2014-07-21"},{"id":2,"amount":500.50,"interest":9.00,"extensions":[],"start":"2014-06-14","end":"2014-06-29"}]
