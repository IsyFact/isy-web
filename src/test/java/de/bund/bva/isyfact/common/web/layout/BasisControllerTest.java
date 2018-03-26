package de.bund.bva.isyfact.common.web.layout;

import de.bund.bva.isyfact.common.web.global.GlobalConfigurationModel;
import de.bund.bva.isyfact.common.web.global.WebBrowserVersion;
import org.junit.Before;
import org.junit.Test;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasisControllerTest {

    private static final String DEFAULT_MODAL_DIALOG_NAME = "modalDialog";

    private BasisController basisController;

    private MutableAttributeMap<Object> flowScope = new LocalAttributeMap<>();

    private MutableAttributeMap<Object> conversationScope = new LocalAttributeMap<>();

    @Before
    public void setup() {
        basisController = new BasisController();
        RequestContext requestContext = mock(RequestContext.class);

        RequestContextHolder.setRequestContext(requestContext);

        when(requestContext.getFlowScope()).thenReturn(flowScope);
        when(requestContext.getConversationScope()).thenReturn(conversationScope);

        flowScope.clear();
        conversationScope.clear();
    }

    @Test
    public void initialisiereModel() {
        BasisModel model = new BasisModel();

        basisController.initialisiereModel(model);

        assertNotNull(model.getInformationsbereichModel());
        assertFalse(model.getInformationsbereichModel().isAnzeigen());
        assertEquals(IconTyp.INFO, model.getInformationsbereichModel().getIconTyp());

        assertNotNull(model.getSeitentoolbarModel());
        assertFalse(model.getSeitentoolbarModel().isAnzeigen());


        assertNotNull(model.getVertikalToolbarModel());
        assertTrue(model.getVertikalToolbarModel().isAnzeigen());
    }

    @Test
    public void toggleInformationsbereich() {
        BasisModel basisModel = new BasisModel();
        basisModel.setInformationsbereichModel(new InformationsbereichModel());

        basisModel.getInformationsbereichModel().setAnzeigen(true);

        basisController.toggleInformationsbereich(basisModel);

        assertFalse(basisModel.getInformationsbereichModel().isAnzeigen());

        basisController.toggleInformationsbereich(basisModel);

        assertTrue(basisModel.getInformationsbereichModel().isAnzeigen());
    }

    @Test
    public void showModalDialog() {
        BasisModel model = new BasisModel();
        flowScope.put("key", model);

        basisController.showModalDialog();

        assertTrue(model.isShowModalDialog());
        assertEquals(DEFAULT_MODAL_DIALOG_NAME, model.getModalDialogName());
    }

    @Test
    public void showModalDialogMitName() {
        BasisModel model = new BasisModel();
        flowScope.put("key", model);

        basisController.showModalDialog("testDialog");

        assertTrue(model.isShowModalDialog());
        assertEquals("testDialog", model.getModalDialogName());
    }

    @Test
    public void hideModalDialog() {
        BasisModel model = new BasisModel();
        flowScope.put("key", model);

        basisController.hideModalDialog();

        assertFalse(model.isShowModalDialog());
    }

    @Test
    public void showPrintView() {
        BasisModel model = new BasisModel();
        flowScope.put("key", model);

        GlobalConfigurationModel globalConfigurationModel = new GlobalConfigurationModel();
        globalConfigurationModel.setJsEnabled(true);
        globalConfigurationModel.setWebBrowserVersion(WebBrowserVersion.INTERNET_EXPLORER_VERSION_KLEINER_7);

        conversationScope.put("globalConfigurationModel", globalConfigurationModel);

        basisController.showPrintView();

        assertTrue(model.isShowPrintView());

        GlobalConfigurationModel flowScopeGlobalConfigModel =
            (GlobalConfigurationModel) flowScope.get("globalConfigurationModel");

        assertNotNull(flowScopeGlobalConfigModel);
        assertTrue(flowScopeGlobalConfigModel.isJsEnabled());
        assertEquals(WebBrowserVersion.INTERNET_EXPLORER_VERSION_KLEINER_7,
            flowScopeGlobalConfigModel.getWebBrowserVersion());
    }
}