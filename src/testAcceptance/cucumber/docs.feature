Feature: Docs

  Scenario: Online documentation is available
    When somebody visit main page
    Then Swagger documentation is visible
