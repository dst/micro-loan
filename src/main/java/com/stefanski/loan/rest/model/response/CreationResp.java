package com.stefanski.loan.rest.model.response;

import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
public class CreationResp {

    public CreationResp(Long id) {
        this.id = id;
    }

    private Long id;
}
