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
    
    output: {"app":{"name":"micro-loan","description":"RESTful web service for micro loans"}}

## Technology stack
- back-end: Java 8, Spring Boot, Spring Data, JPA with Hibernate, embedded H2 Database, Lombok, Logback
- testing: JUnit 4, mockito, AssertJ, MockMvc, Cucumber, Groovy, Spock
- building/deploying: embedded Apache Tomcat, gradle, Heroku
- code metics: JaCoCo

## Example session with server
Server accepts and returns only JSON. Add -v for verbose output.

### Customer creates account
    $ curl -H "Content-Type: application/json" -d '{"firstName":"Jan", "lastName":"Kowalski"}' localhost:8888/customers
    
    output: {"id":1}

### Customer views his account
    $ curl localhost:8888/customers/1
    
    output: {"id":1,"firstName":"Jan","lastName":"Kowalski"}

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

## Heroku deployment
Automatic deployment is possible for Heroku users, but please be aware that "1x standard dyno"
has not enough RAM and CPU to start micro-loan app in 60 seconds (boot timeout).
PX dyno should be used instead.

- Copy gradle.properties.example to gradle.properties
    - Set api key
    - Set app name
- Create app: $ ./gradlew herokuAppCreate
- Deploy app: ./gradlew herokuAppDeploy (or "git push heroku master" to be more aware what is going on)
- Check logs: heroku logs -t
- RESTful web service waits to serve your requests at address http://your-app-name.herokuapp.com
