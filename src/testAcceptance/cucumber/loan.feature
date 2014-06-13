Feature: Loans

  Scenario: A customer apply for an loan and gets it
    Given customer
    When customer wants to loan 100 PLN for 30 days
    Then loan is issued

  Scenario: The interest is increased when a customer extends a loan
    Given customer
    And customer has loan
    When customer extends loan
    Then interest gets increased by factor of 1.5

  Scenario: The loan term is extended for a one week when a customer extends a loan
    Given customer
    And customer has loan
    When customer extends loan
    Then term is extended for 7 days

  Scenario: A customer can see his loan
    Given customer
    And customer has loan
    When customer wants to see his loan
    Then he can see loan

  Scenario: A customer can see his loan with extensions
    Given customer
    And customer has loan
    And loan has extension
    When customer wants to see his loan
    Then he can see loan
    And he can see extension
