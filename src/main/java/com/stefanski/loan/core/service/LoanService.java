package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.error.ResourceNotFoundException;
import com.stefanski.loan.core.repository.LoanRepository;
import com.stefanski.loan.rest.model.request.LoanRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Service
public class LoanService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanRepository loanRepository;

    public Loan findById(Long loanId) throws ResourceNotFoundException {
        Loan loan = loanRepository.findOne(loanId);
        if (loan == null) {
            String msg = String.format("Loan with id %d does not exist", loanId);
            throw new ResourceNotFoundException(msg);
        }
        return loan;
    }

    public Long applyForLoan(Long customerId, LoanRequest loanReq) throws ResourceNotFoundException {
        Customer customer = customerService.findById(customerId);

        Loan loan = createLoanFromRequest(loanReq);
        loan.setCustomer(customer);

        Loan createdLoan = loanRepository.save(loan);
        log.info("Created loan: {}", createdLoan);
        Long loanId = createdLoan.getId();
        return loanId;
    }

    public List<Loan> findCustomerLoans(Long customerId) throws ResourceNotFoundException {
        Customer customer = customerService.findById(customerId);
        return customer.getLoans();
    }

    private Loan createLoanFromRequest(LoanRequest loanReq) {
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = begin.plusDays(loanReq.getDaysCount());

        Loan loan = new Loan();
        loan.setAmount(loanReq.getAmount());
        loan.setInterest(getLoanInterest());
        loan.setStartDateTime(begin);
        loan.setEndDateTime(end);
        return loan;
    }

    private BigDecimal getLoanInterest() {
        //TODO(dst), 6/8/14: impl
        return BigDecimal.ONE;
    }
}
