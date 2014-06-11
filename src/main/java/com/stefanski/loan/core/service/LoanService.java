package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.ex.RiskTooHighException;
import com.stefanski.loan.core.repository.LoanRepository;
import com.stefanski.loan.core.risk.RiskAnalyser;
import com.stefanski.loan.rest.model.request.LoanRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${system.loan.interest}")
    private BigDecimal loanInterest;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RiskAnalyser riskAnalyser;

    public Loan findById(Long loanId) throws ResourceNotFoundException {
        Loan loan = loanRepository.findOne(loanId);
        if (loan == null) {
            String msg = String.format("Loan with id %d does not exist", loanId);
            throw new ResourceNotFoundException(msg);
        }
        return loan;
    }

    public Long applyForLoan(Long customerId, LoanRequest loanReq) throws ResourceNotFoundException, RiskTooHighException {
        Customer customer = customerService.findById(customerId);

        Loan loan = createLoanFromRequest(loanReq);
        loan.setCustomer(customer);

        riskAnalyser.validate(loan);

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
        loan.setApplicationTime(begin);
        loan.setDeadline(end);
        loan.setIp(loanReq.getIp());
        return loan;
    }

    private BigDecimal getLoanInterest() {
        return loanInterest;
    }
}
