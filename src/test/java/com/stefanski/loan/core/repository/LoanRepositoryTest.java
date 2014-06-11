package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Loan;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.stefanski.loan.util.TestDataFixture.simpleCustomer;
import static com.stefanski.loan.util.TestDataFixture.simpleLoan;
import static java.math.BigDecimal.TEN;
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
        Loan loan = createSimpleLoan(null);

        // when:
        Loan foundLoan = loanRepository.findOne(loan.getId());

        // then:
        assertThat(foundLoan).isNotNull();
    }

    @Test
    public void shouldFindByIpAndApplicationBetween() throws Exception {
        // given:
        LocalDateTime now = LocalDateTime.now();

        Loan loan = createSimpleLoan(now.minusHours(1));
        createSimpleLoan(now);
        createSimpleLoan(now);
        createSimpleLoan(now.plusHours(1));

        LocalDateTime start = now;
        LocalDateTime end = now.plusMinutes(30);

        // when:
        List<Loan> loans = loanRepository.findByIpAndApplicationTimeBetween(loan.getIp(), start, end);

        // then:
        assertThat(loans).hasSize(2);
    }

    @Test
    public void shouldCalculateLoansWithIpAndInDay() throws Exception {
        // given:
        LocalDateTime now = LocalDateTime.now();
        LocalDate day = now.toLocalDate();

        Loan loan = createSimpleLoan(now.minusDays(1));
        createSimpleLoan(now);
        createSimpleLoan(now);
        createSimpleLoan(now.plusDays(1));

        // when:
        long count = loanRepository.getLoanCountFor(loan.getIp(), day);

        // then:
        assertThat(count).isEqualTo(2);
    }

    private Loan createSimpleLoan(LocalDateTime applicationTime) {
        if (applicationTime == null) {
            applicationTime = LocalDateTime.now();
        }

        Customer customer = simpleCustomer();
        customer = customerRepository.save(customer);

        Loan loan = simpleLoan(TEN, applicationTime);
        loan.setCustomer(customer);
        loan = loanRepository.save(loan);
        return loan;
    }
}