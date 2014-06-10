package com.stefanski.loan.rest.model.response;

import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
public class CreationResp {

    private Long id;

    public CreationResp(Long id) {
        this.id = id;
    }
}
