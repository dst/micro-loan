[![Build Status](https://travis-ci.org/dst/micro-loan.svg)](https://travis-ci.org/dst/micro-loan)
# micro-loan

## System dependencies
- Java 8

## Configuration
Adjust application.properties if needed.

## Starting server
Linux:

```
./gradlew clean bootRun
```

Windows:
Use gradlew.bat

## Verification
For default application.properties:
    $ curl localhost:8888/info

## Technology stack
- back-end: Java 8, Spring Boot, Spring Data, JPA with Hibernate, embedded H2 Database, Lombok, Logback
- testing: JUnit 4, mockito, AssertJ, MockMvc
- building/deploying: gradle, embedded Apache Tomcat

## Development

### IDEA settings
- Fix Lombok problem when running tests: "Enable annotation processing" in Settings->Compiler->Annotation Processors

### New library
- Add gradle dependency
- Download gradle dependency: $ ./gradlew assemble
- Add IDE dependency:
    - IDEA: ./gradlew idea
    - Eclipse: ./gradlew eclipse
    
## Example session with server:
Create customer:

```
$ curl -v H "Content-Type: application/json" -d '{"firstName":"abc", "lastName":"xyz"}' localhost:8888/customers
```

View customer:

```
$ curl -v localhost:8888/customers/1
```

Create loan:

```
$ curl -H "Content-Type: application/json" -d '{"amount": 10.0}' localhost:8888/loans
```

View loan:

```
$ curl localhost:8888/loans/1
```