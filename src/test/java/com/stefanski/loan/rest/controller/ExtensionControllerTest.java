package com.stefanski.loan.rest.controller;

import com.stefanski.loan.core.domain.Extension;
import com.stefanski.loan.core.service.ExtensionService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static com.stefanski.loan.util.TestDataFixture.EXTENSION_ID;
import static com.stefanski.loan.util.TestDataFixture.LOAN_ID;
import static com.stefanski.loan.util.TestHelper.APPLICATION_JSON_UTF8;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Dariusz Stefanski
 */
public class ExtensionControllerTest extends ControllerIntegrationTest {


    private static final String LOANS_PREFIX = "/loans";

    @Mock
    private ExtensionService extensionService;

    @InjectMocks
    private ExtensionController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup(controller);
    }

    @Test
    public void shouldReturnIdOfCreatedExtension() throws Exception {
        when(extensionService.extendLoan(LOAN_ID)).thenReturn(EXTENSION_ID);

        mockMvc.perform(postWithJson(LOANS_PREFIX + "/" + LOAN_ID + "/extensions", null))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXTENSION_ID.intValue())));
    }

    @Test
    public void shouldCreateLocationToNewExtension() throws Exception {
        when(extensionService.extendLoan(LOAN_ID)).thenReturn(EXTENSION_ID);

        String extensionsPath = LOANS_PREFIX + "/" + LOAN_ID + "/extensions";
        mockMvc.perform(postWithJson(extensionsPath, null))
                .andExpect(header().string("Location",
                        Matchers.endsWith(extensionsPath + "/" + EXTENSION_ID)));
    }


    @Test
    public void shouldReturnCorrectExtension() throws Exception {
        Extension extension = new Extension();
        extension.setId(EXTENSION_ID);
        extension.setCreationTime(LocalDateTime.now());

        when(extensionService.findLoanExtension(LOAN_ID, EXTENSION_ID)).thenReturn(extension);

        mockMvc.perform(get(LOANS_PREFIX + "/" + LOAN_ID + "/extensions/" + EXTENSION_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXTENSION_ID.intValue())))
                .andExpect(jsonPath("$.creationTime", is(extension.getCreationTime().format(ISO_DATE))));
    }
}
