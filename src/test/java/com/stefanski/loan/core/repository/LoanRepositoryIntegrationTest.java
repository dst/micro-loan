package com.stefanski.loan.core.repository;

import com.stefanski.loan.Application;
import com.stefanski.loan.core.domain.Loan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class LoanRepositoryIntegrationTest {

    @Autowired
    private LoanRepository loanRepository;


    @Test
    public void shouldFindCustomerById() throws Exception {
        // given:
        Loan loan = new Loan();
        loan = loanRepository.save(loan);
        long loanId = loan.getId();

        // when:
        Loan foundLoan = loanRepository.findOne(loanId);

        // then:
        assertThat(foundLoan).isNotNull();
    }
}