# micro-loan development

## IDEA settings
- Fix Lombok problem when running tests: "Enable annotation processing" in Settings->Compiler->Annotation Processors

## New library
- Add gradle dependency
- Download gradle dependency: $ ./gradlew assemble
- Add IDE dependency:
    - IDEA: ./gradlew idea
    - Eclipse: ./gradlew eclipse

## TDD with Cucumber
Run server in debug mode with acceptance test properties.
VM options: -Dspring.config.location="src/main/resources/application.properties,src/testAcceptance/resources/application-accept.properties"
Then run RunCukes.groovy.

## Code metrics
    
### Code coverage
    $ ./gradlew jacocoTestReport
    $ browser build/reports/jacoco/test/html/index.html

### Acceptance tests
    $ ./gradlew acceptanceTest
    $ browser build/reports/cucumber/index.html

### Unit tests
    $ ./gradlew test
    $ browser build/reports/tests/index.html
    
### All at once
    $ cd tools
    $ ./generate-reports.sh
