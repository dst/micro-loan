package com.stefanski.loan.rest.model.response;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.rest.util.Formatter;
import lombok.Data;

/**
 * @author Dariusz Stefanski
 */
@Data
public class ExtensionResp {

    private Long id;
    private String creationTime;

    public static ExtensionResp fromExtension(Extension ext) {
        ExtensionResp resp = new ExtensionResp();
        resp.setId(ext.getId());
        resp.setCreationTime(Formatter.formatTime(ext.getCreationTime()));
        return resp;
    }
}
