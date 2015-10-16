package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.rest.util.Formatter;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

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

    private ExtensionResp(Long id, LocalDateTime creationTime) {
        this.id = id;
        this.creationTime = Formatter.formatTime(creationTime);
    }

    public static ExtensionResp fromExtension(Extension ext) {
        return new ExtensionResp(ext.getId(), ext.getCreationTime());
    }
}
