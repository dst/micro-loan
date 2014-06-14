package com.stefanski.loan.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * @author Dariusz Stefanski
 */
@RestController
public abstract class AbstractRestController {

    protected HttpHeaders getHttpHeadersWithLocation(String pathSuffix, Object... args) {
        HttpHeaders headers = new HttpHeaders();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(pathSuffix).buildAndExpand(args).toUri();
        headers.setLocation(uri);
        return headers;
    }
}
