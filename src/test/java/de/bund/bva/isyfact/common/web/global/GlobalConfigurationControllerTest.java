package de.bund.bva.isyfact.common.web.global;

import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import de.bund.bva.isyfact.common.web.MockFacesContext;
import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalConfigurationControllerTest {

    private static final Map<String, String> requestHeaderMap = new HashMap<>();

    private static final Map<String, Object> sessionMap = new HashMap<>();

    private static final String SONSTIGER_BROWSER =
        "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    private static final String IE_KLEINER_7 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";

    private static final String IE_7 = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)";

    private static final String IE_8 =
        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Win64; x64; Trident/4.0)";

    @Before
    public void setupContext() {
        FacesContext facesContextMock = MockFacesContext.mockFacesContext();
        ExternalContext externalContext = mock(ExternalContext.class);
        when(facesContextMock.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getRequestHeaderMap()).thenReturn(requestHeaderMap);
        when(externalContext.getSessionMap()).thenReturn(sessionMap);

        sessionMap.clear();
        requestHeaderMap.clear();
    }

    @Test
    public void initializeConfigurationModelJavaScriptEnabled() {
        sessionMap.put(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL, true);

        GlobalConfigurationController controller = new GlobalConfigurationController();

        GlobalConfigurationModel model = controller.initializeConfigurationModel();

        assertTrue(model.isJsEnabled());

        sessionMap.put(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL, false);

        model = controller.initializeConfigurationModel();

        assertFalse(model.isJsEnabled());
    }

    @Test
    public void initializeConfigurationModelWebBrowserVersion() {
        GlobalConfigurationController controller = new GlobalConfigurationController();

        requestHeaderMap.put("User-Agent", SONSTIGER_BROWSER);
        GlobalConfigurationModel model = controller.initializeConfigurationModel();
        assertEquals(WebBrowserVersion.SONSTIGER_WEBBROWSER, model.getWebBrowserVersion());

        requestHeaderMap.put("User-Agent", IE_KLEINER_7);
        model = controller.initializeConfigurationModel();
        assertEquals(WebBrowserVersion.INTERNET_EXPLORER_VERSION_KLEINER_7, model.getWebBrowserVersion());

        requestHeaderMap.put("User-Agent", IE_7);
        model = controller.initializeConfigurationModel();
        assertEquals(WebBrowserVersion.INTERNET_EXPLORER_VERSION_7, model.getWebBrowserVersion());

        requestHeaderMap.put("User-Agent", IE_8);
        model = controller.initializeConfigurationModel();
        assertEquals(WebBrowserVersion.INTERNET_EXPLORER_VERSION_8, model.getWebBrowserVersion());
    }

    @Test
    public void getModelZuController() {
        RequestContext requestContext = mock(RequestContext.class);
        MutableAttributeMap<Object> flowScope = new LocalAttributeMap<>();

        flowScope.put("globalConfigurationModel", new GlobalConfigurationModel());

        RequestContextHolder.setRequestContext(requestContext);

        when(requestContext.getFlowScope()).thenReturn(flowScope);

        GlobalConfigurationController controller = new GlobalConfigurationController();

        assertNotNull(controller.getModelZuController());
    }
}