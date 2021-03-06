package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

import static java.time.LocalTime.MIDNIGHT;

/**
 * @author Dariusz Stefanski
 */
@Component
class DrunkManAtNightRisk implements Risk {

    private BigDecimal maxAmount;

    @Autowired
    public DrunkManAtNightRisk(@Value("${system.loan.maxAmount}") BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean isApplicableTo(Loan loan) {
        return isAppliedAtNight(loan) && isAboveOrEqualLimit(loan);
    }

    @Override
    public String getMessage() {
        return "A loan with a max possible amount is not possible between 00:00 and 6:00 AM";
    }

    @Override
    public String getName() {
        return "Drunk Man At Night Risk";
    }

    private boolean isAboveOrEqualLimit(Loan loan) {
        return loan.getAmount().compareTo(maxAmount) >= 0;
    }

    private boolean isAppliedAtNight(Loan loan) {
        LocalTime time = loan.getStart().toLocalTime();
        return isTimeBetweenInclusive(time, startOfNight(), endOfNight());
    }

    private LocalTime startOfNight() {
        return MIDNIGHT;
    }

    private LocalTime endOfNight() {
        return startOfNight().plusHours(6);
    }

    private boolean isTimeBetweenInclusive(LocalTime time, LocalTime start, LocalTime end) {
        return (time.equals(start) || time.isAfter(start)) && (time.equals(end) || time.isBefore(end));
    }
}
