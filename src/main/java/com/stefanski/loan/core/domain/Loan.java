package com.stefanski.loan.core.domain;

import com.google.common.base.Objects;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Dariusz Stefanski
 */
@Entity
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal interest;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("amount", amount)
                .add("interest", interest)
                .add("startDateTime", startDateTime)
                .add("endDateTime", endDateTime)
                .toString();

    }
}
