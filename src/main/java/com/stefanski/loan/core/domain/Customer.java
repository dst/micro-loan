package com.stefanski.loan.core.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dariusz Stefanski
 */
@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @OneToMany(mappedBy = "customer")
    private List<Loan> loans = new LinkedList<>();
}

