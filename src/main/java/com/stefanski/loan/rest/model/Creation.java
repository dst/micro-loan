package com.stefanski.loan.rest.model;

import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
public class Creation {

    public Creation(Long id) {
        this.id = id;
    }

    private Long id;
}
