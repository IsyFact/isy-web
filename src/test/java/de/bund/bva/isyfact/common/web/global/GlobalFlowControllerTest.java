package de.bund.bva.isyfact.common.web.global;

import de.bund.bva.isyfact.common.web.exception.web.ErrorController;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.controller.NavigationMenuController;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontext;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontextVerwalter;
import org.junit.Test;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class GlobalFlowControllerTest {

    @Test
    public void initialisiereModel() {
        NavigationMenuController navigationMenuController = mock(NavigationMenuController.class);

        ErrorController errorController = mock(ErrorController.class);
        final String errorMessage = "errorMessage";
        final String errorMessageTitle = "errorMessageTitle";
        when(errorController.getAjaxErrorMessage()).thenReturn(errorMessage);
        when(errorController.getAjaxErrorMessageTitle()).thenReturn(errorMessageTitle);

        AufrufKontextVerwalter aufrufKontextVerwalter = mock(AufrufKontextVerwalter.class);
        AufrufKontext aufrufKontext = mock(AufrufKontext.class);
        final String bearbeiterName = "bearbeiterName";
        when(aufrufKontextVerwalter.getAufrufKontext()).thenReturn(aufrufKontext);
        when(aufrufKontext.getDurchfuehrenderSachbearbeiterName()).thenReturn(bearbeiterName);

        RequestContext requestContext = mock(RequestContext.class);
        RequestContextHolder.setRequestContext(requestContext);
        FlowDefinition flowDefinition = mock(FlowDefinition.class);
        when(requestContext.getActiveFlow()).thenReturn(flowDefinition);
        final String flowId = "testFlowId";
        when(flowDefinition.getId()).thenReturn(flowId);

        GlobalFlowController controller = new GlobalFlowController();
        controller.setAufrufKontextVerwalter(aufrufKontextVerwalter);
        controller.setErrorController(errorController);
        controller.setNavigationMenuController(navigationMenuController);

        GlobalFlowModel model = new GlobalFlowModel();

        controller.initialisiereModel(model);

        assertEquals(errorMessage, model.getAjaxErrorMessage());
        assertEquals(errorMessageTitle, model.getAjaxErrorMessageTitle());
        assertEquals(bearbeiterName, model.getSachbearbeiterName());
        assertEquals("resources.nachrichten.maskentexte." + flowId,
            model.getPathToResourcesBundleCurrentFlow());
        assertTrue(model.isFocusOnloadActive());
        verify(navigationMenuController).initialisiereNavigationMenue();
    }
}