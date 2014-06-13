package com.stefanski.loan.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.stefanski.loan.rest.util.LocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Dariusz Stefanski
 */
@Data
@Entity
public class Extension {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime creationTime;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Loan loan;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("creationTime", creationTime)
                .toString();

    }
}
