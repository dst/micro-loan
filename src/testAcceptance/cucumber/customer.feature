Feature: Customers

  Scenario: Create a customer with Polish letters
    Given customer with firstName "Mirosław" and lastName "Żółw"
    When creation is performed
    Then customer is created

  Scenario: Viewing customer data
    Given existing customer
    When customer is asked
    Then customer is returned
