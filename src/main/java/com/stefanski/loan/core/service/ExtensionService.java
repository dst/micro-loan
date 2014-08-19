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

    private int extensionDays;
    private BigDecimal extensionInterest;

    private ExtensionRepository extensionRepository;
    private LoanService loanService;

    @Autowired
    public ExtensionService(@Value("${system.loan.ext.days}") int extensionDays,
                            @Value("${system.loan.ext.interest}") BigDecimal extensionInterest,
                            ExtensionRepository extensionRepository,
                            LoanService loanService) {
        this.extensionDays = extensionDays;
        this.extensionInterest = extensionInterest;
        this.extensionRepository = extensionRepository;
        this.loanService = loanService;
    }

    public Extension findLoanExtension(Long loanId, Long extensionId) {
        Extension ext = getExtension(extensionId);
        checkThatExtBelongsToLoan(ext, loanId);
        log.debug("Found extension: {}", ext);
        return ext;
    }

    public Long extendLoan(Long loanId) {
        Loan loan = loanService.findLoanById(loanId);
        Extension extension = createExtensionFor(loan);
        extendLoan(loan);
        return extension.getId();
    }

    private Extension getExtension(Long extensionId) {
        Extension ext = extensionRepository.findOne(extensionId);
        if (ext == null) {
            String msg = String.format("Extension with id %d does not exist", extensionId);
            throw new ResourceNotFoundException(msg);
        }
        return ext;
    }

    private void checkThatExtBelongsToLoan(Extension ext, Long loanId) {
        if (!loanId.equals(ext.getLoan().getId())) {
            String msg = String.format("Extension with id %d which belongs to loan %d does not exist",
                    ext.getId(), loanId);
            throw new ResourceNotFoundException(msg);
        }
    }

    private Extension createExtensionFor(Loan loan) {
        Extension extension = new Extension();
        extension.setCreationTime(LocalDateTime.now());
        extension.setLoan(loan);
        extension = extensionRepository.save(extension);
        log.info("Created extension: {}", extension);
        return extension;
    }

    private void extendLoan(Loan loan) {
        loan.extendDeadline(getExtensionDays());
        loan.multipleInterest(getExtensionInterest());
        loanService.save(loan);
        log.info("Updated loan: {}", loan);
    }

    private int getExtensionDays() {
        return extensionDays;
    }

    private BigDecimal getExtensionInterest() {
        return extensionInterest;
    }
}
