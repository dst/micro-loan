package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Loan;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.stefanski.loan.util.TestDataFixture.simpleCustomer;
import static com.stefanski.loan.util.TestDataFixture.simpleLoan;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dariusz Stefanski
 */
public class LoanRepositoryTest extends RepositoryIntegrationTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void shouldFindLoanById() throws Exception {
        // given:
        Customer customer = simpleCustomer();
        customer = customerRepository.save(customer);

        Loan loan = simpleLoan();
        loan.setCustomer(customer);
        loan = loanRepository.save(loan);
        long loanId = loan.getId();

        // when:
        Loan foundLoan = loanRepository.findOne(loanId);

        // then:
        assertThat(foundLoan).isNotNull();
    }
}