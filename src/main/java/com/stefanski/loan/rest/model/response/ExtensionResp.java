package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.rest.util.Formatter;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
@ApiModel
public class ExtensionResp {

    @ApiModelProperty(value = "unique identifier for the extension", required = true)
    private Long id;

    @ApiModelProperty(value = "creation time in ISO_DATE format (yyyy-mm-dd)", required = true)
    private String creationTime;

    public static ExtensionResp fromExtension(Extension ext) {
        ExtensionResp resp = new ExtensionResp();
        resp.setId(ext.getId());
        resp.setCreationTime(Formatter.formatTime(ext.getCreationTime()));
        return resp;
    }
}
