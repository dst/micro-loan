package com.stefanski.loan.core.risk;

import com.stefanski.loan.core.domain.Loan;
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

    @Value("${system.loan.maxAmount}")
    private BigDecimal maxAmount;

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

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    private boolean isAboveOrEqualLimit(Loan loan) {
        return loan.getAmount().compareTo(maxAmount) >= 0;
    }

    private boolean isAppliedAtNight(Loan loan) {
        LocalTime time = loan.getStart().toLocalTime();
        assert time != null;
        return isTimeBetween(time, getNightStart(), getNightEnd());
    }

    private LocalTime getNightEnd() {
        return getNightStart().plusHours(6);
    }

    private LocalTime getNightStart() {
        return MIDNIGHT;
    }

    /**
     * @param time
     * @param start
     * @param end
     * @return If time is between start and end inclusive
     */
    private boolean isTimeBetween(LocalTime time, LocalTime start, LocalTime end) {
        return (time.equals(start) || time.isAfter(start)) && (time.equals(end) || time.isBefore(end));
    }
}
