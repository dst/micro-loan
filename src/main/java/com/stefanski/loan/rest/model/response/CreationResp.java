package com.stefanski.loan.rest.model.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
@ApiModel
public class CreationResp {

    @ApiModelProperty(value = "ID of created object", required = true)
    private Long id;

    public CreationResp(Long id) {
        this.id = id;
    }
}
