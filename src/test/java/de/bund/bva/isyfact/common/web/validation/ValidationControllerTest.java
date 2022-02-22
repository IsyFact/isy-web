package de.bund.bva.isyfact.common.web.validation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.faces.application.FacesMessage;

@RunWith(MockitoJUnitRunner.class)
public class ValidationControllerTest {

    private static final String FLASH_SCOPE_VALIDATION_MODEL_VARIABLE = "validationModel";

    private static final ValidationMessage VALIDATION_MESSAGE_1 =
        new ValidationMessage("a1", "b1", "c1", "d1");

    private static final ValidationMessage VALIDATION_MESSAGE_2 =
        new ValidationMessage("a2", "b2", "c2", "d2");

    @Mock
    private RequestContext requestContext;

    private MutableAttributeMap<Object> flashScope = new LocalAttributeMap<>();

    @Before
    public void setupRequestContext() {
        RequestContextHolder.setRequestContext(requestContext);
        when(requestContext.getFlashScope()).thenReturn(flashScope);

        flashScope.clear();
    }


    @Test
    public void processValidationMessagesLeereListeVeraendertNichtMessagesImFlashScope() {
        ValidationController validationController = new ValidationController();

        validationController.processValidationMessages(Collections.emptyList());

        assertNull(flashScope.get(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE));

        flashScope.clear();

        ValidationModel existingModel = new ValidationModel();
        List<ValidationMessage> existingMessages = Arrays.asList(VALIDATION_MESSAGE_1, VALIDATION_MESSAGE_2);
        existingModel.getValidationMessages().addAll(existingMessages);

        flashScope.put(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE, existingModel);

        validationController.processValidationMessages(Collections.emptyList());

        ValidationModel validationModel =
            (ValidationModel) flashScope.get(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE);

        assertEquals(existingMessages.size(), validationModel.getValidationMessages().size());
        assertTrue(validationModel.getValidationFacesMessages().isEmpty());
    }

    @Test
    public void processValidationMessagesListeVeraendertMessagesImFlashScope() {
        List<ValidationMessage> messages = Arrays.asList(VALIDATION_MESSAGE_1, VALIDATION_MESSAGE_2);

        ValidationController validationController = new ValidationController();

        validationController.processValidationMessages(messages);

        ValidationModel modelAusFlashScope =
            (ValidationModel) flashScope.get(FLASH_SCOPE_VALIDATION_MODEL_VARIABLE);

        FacesMessage fm0 = modelAusFlashScope.getValidationFacesMessages().get(0);
        FacesMessage fm1 = modelAusFlashScope.getValidationFacesMessages().get(1);

        assertEquals(2, modelAusFlashScope.getValidationMessages().size());
        assertEquals(2, modelAusFlashScope.getValidationFacesMessages().size());
        assertNotNull(modelAusFlashScope.getGlobalValidationFacesMessage());
        assertEquals(VALIDATION_MESSAGE_1.getReadableReference(), fm0.getSummary());
        assertEquals(VALIDATION_MESSAGE_2.getReadableReference(), fm1.getSummary());
        assertEquals(VALIDATION_MESSAGE_1.getMessage(), fm0.getDetail());
        assertEquals(VALIDATION_MESSAGE_2.getMessage(), fm1.getDetail());
        assertFalse(fm0.getDetail().contains(modelAusFlashScope.getValidationMessages().get(0).getCode()));
        assertFalse(fm1.getDetail().contains(modelAusFlashScope.getValidationMessages().get(1).getCode()));
    }
}