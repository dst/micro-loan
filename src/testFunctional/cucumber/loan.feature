Feature: Loans

  Scenario: Customer apply for loan and gets it
    Given a customer
    When a customer wants to loan 100 PLN for 30 days
    Then a loan is issued