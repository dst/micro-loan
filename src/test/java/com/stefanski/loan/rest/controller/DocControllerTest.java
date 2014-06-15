package com.stefanski.loan.rest.controller;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dariusz Stefanski
 */
public class DocControllerTest extends ControllerIntegrationTest {

    private DocController controller;

    @Before
    public void setup() {
        controller = new DocController();
        super.setup(controller);
    }

    @Test
    public void shouldEmptyPathRedirectToSwaggerDoc() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/docs/index.html"));
    }
}