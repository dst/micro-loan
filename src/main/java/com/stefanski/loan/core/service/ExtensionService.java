package com.stefanski.loan.core.service;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.domain.Loan;
import com.stefanski.loan.core.ex.ResourceNotFoundException;
import com.stefanski.loan.core.repository.ExtensionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Dariusz Stefanski
 */
@Slf4j
@Transactional
@Service
public class ExtensionService {

    @Value("${system.loan.ext.days}")
    private int extensionDays;

    @Value("${system.loan.ext.interest}")
    private BigDecimal extensionInterest;

    @Autowired
    private ExtensionRepository extensionRepository;

    @Autowired
    private LoanService loanService;

    public Extension findLoanExtension(Long loanId, Long extensionId) {
        Extension ext = extensionRepository.findOne(extensionId);
        if (ext == null) {
            String msg = String.format("Extension with id %d does not exist", extensionId);
            throw new ResourceNotFoundException(msg);
        }

        if (!loanId.equals(ext.getLoan().getId())) {
            String msg = String.format("Extension with id %d which belongs to loan %d does not exist",
                    extensionId, loanId);
            throw new ResourceNotFoundException(msg);
        }

        log.debug("Found extension: {}", ext);
        return ext;
    }

    public Long extendLoan(Long loanId) {
        Loan loan = loanService.findLoanById(loanId);

        Extension extension = new Extension();
        extension.setCreationTime(LocalDateTime.now());
        extension.setLoan(loan);
        extension = extensionRepository.save(extension);
        log.info("Created extension: {}", extension);

        loan.extendDeadline(getExtensionDays());
        loan.multipleInterest(getExtensionInterest());
        loanService.save(loan);
        log.info("Updated loan: {}", loan);

        return extension.getId();
    }

    private int getExtensionDays() {
        return extensionDays;
    }

    private BigDecimal getExtensionInterest() {
        return extensionInterest;
    }

    public void setExtensionDays(int extensionDays) {
        this.extensionDays = extensionDays;
    }

    public void setExtensionInterest(BigDecimal extensionInterest) {
        this.extensionInterest = extensionInterest;
    }
}