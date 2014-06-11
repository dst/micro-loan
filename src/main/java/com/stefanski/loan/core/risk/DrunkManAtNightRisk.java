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
public class DrunkManAtNightRisk implements Risk {

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

    private boolean isAppliedAtNight(Loan loan) {
        LocalTime time = loan.getApplicationTime().toLocalTime();
        assert time != null;

        //TODO(dst), 6/10/14: think if it is what author thought about
        return time.isAfter(MIDNIGHT) && time.isBefore(MIDNIGHT.plusHours(6));
    }

    private boolean isAboveOrEqualLimit(Loan loan) {
        return loan.getAmount().compareTo(maxAmount) >= 0;
    }

}
