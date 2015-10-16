package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Customer;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.risk.RiskTooHighException;
import com.stefanski.loan.core.repository.LoanRepository;
import com.stefanski.loan.core.risk.RiskAnalyser;
import com.stefanski.loan.rest.model.request.LoanReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Transactional
@Service
public class LoanService {

    private BigDecimal loanInterest;
    private CustomerService customerService;
    private LoanRepository loanRepository;
    private RiskAnalyser riskAnalyser;

    @Autowired
    public LoanService(@Value("${system.loan.interest}") BigDecimal loanInterest,
                       CustomerService customerService,
                       LoanRepository loanRepository,
                       RiskAnalyser riskAnalyser) {
        this.loanInterest = loanInterest;
        this.customerService = customerService;
        this.loanRepository = loanRepository;
        this.riskAnalyser = riskAnalyser;
    }

    public Loan findLoanById(Long loanId) {
        Loan loan = loanRepository.findOne(loanId);
        if (loan == null) {
            String msg = String.format("Loan with id %d does not exist", loanId);
            throw new ResourceNotFoundException(msg);
        }

        log.debug("Found loan: {}", loan);
        return loan;
    }

    public void save(Loan loan) {
        loanRepository.save(loan);
    }

    public Long applyForLoan(LoanReq loanReq) {
        Loan loan = createLoanFromRequest(loanReq);

        riskAnalyser.validate(loan);

        Loan createdLoan = loanRepository.save(loan);
        log.info("Created loan: {}", createdLoan);

        return createdLoan.getId();
    }

    public List<Loan> findCustomerLoans(Long customerId) {
        if (customerId == null) {
            throw new ResourceNotFoundException("Customer with empty id does not exist");
        }

        Customer customer = customerService.findById(customerId);
        return customer.getLoans();
    }

    private Loan createLoanFromRequest(LoanReq loanReq) {
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = begin.plusDays(loanReq.getDaysCount());

        Loan loan = new Loan();
        loan.setAmount(loanReq.getAmount());
        loan.setInterest(loanInterest);
        loan.setStart(begin);
        loan.setEnd(end);
        loan.setIp(loanReq.getIp());

        Customer customer = customerService.findById(loanReq.getCustomerId());
        loan.setCustomer(customer);

        return loan;
    }
}
