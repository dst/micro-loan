package com.stefanski.loan.core.domain;

import com.google.common.base.Objects;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Data
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal interest;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime start;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime end;

    @NotNull
    private String ip;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @OneToMany(mappedBy = "loan")
    private List<Extension> extensions = new LinkedList<>();

    public void addExtension(Extension extension) {
        extensions.add(extension);
    }

    public void extendDeadline(int days) {
        setEnd(end.plusDays(days));
    }

    public void multipleInterest(BigDecimal factor) {
        setInterest(interest.multiply(factor));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("amount", amount)
                .add("interest", interest)
                .add("start", start)
                .add("end", end)
                .toString();
    }
}
