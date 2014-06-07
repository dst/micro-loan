package com.stefanski.loan.core.repository;

import com.stefanski.loan.core.domain.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dariusz Stefanski
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
