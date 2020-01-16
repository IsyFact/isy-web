package de.bund.bva.isyfact.common.web.layout;

import java.util.ArrayList;

import de.bund.bva.isyfact.konfiguration.common.Konfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuicklinksControllerTest {

    @Mock
    private Konfiguration konfiguration;

    @Mock
    private FlowDefinition flowDefinition;

    @Mock
    private SharedAttributeMap<Object> sessionMap;

    private QuicklinksController controller;

    private static final String SESSION_KEY_QUICKLINKS = "quicklinks";

    private Object mutex = new Object();

    @Before
    public void setup() {
        RequestContext requestContext = mock(RequestContext.class);
        RequestContextHolder.setRequestContext(requestContext);

        ExternalContext externalContext = mock(ExternalContext.class);
        ExternalContextHolder.setExternalContext(externalContext);

        when(externalContext.getSessionMap()).thenReturn(sessionMap);

        FlowExecutionContext flowExecutionContext = mock(FlowExecutionContext.class);

        when(requestContext.getFlowExecutionContext()).thenReturn(flowExecutionContext);
        when(flowExecutionContext.getDefinition()).thenReturn(flowDefinition);

        controller = new QuicklinksController(konfiguration);

        reset(konfiguration, sessionMap);

        when(sessionMap.getMutex()).thenReturn(mutex);
    }

    @Test
    public void initialisiereModel() {
        when(flowDefinition.getId()).thenReturn("testFlow1");
        when(konfiguration.getAsString("gui.quicklinks.gruppenIds", null)).thenReturn("gruppe1,gruppe2");
        when(konfiguration.getAsString("gui.quicklinks.gruppe1.contextflow", null))
            .thenReturn("testFlow1,testFlow2");
        when(konfiguration.getAsString("gui.quicklinks.gruppe2.contextflow", null)).thenReturn("testFlow2");

        when(konfiguration.getAsString("gui.quicklinks.gruppe1.text", null)).thenReturn("gruppe1Text");
        when(konfiguration.getAsString("gui.quicklinks.gruppe2.text", null)).thenReturn("gruppe2Text");

        QuicklinksModel quicklinksModel = new QuicklinksModel();

        when(sessionMap.get(SESSION_KEY_QUICKLINKS)).thenReturn(quicklinksModel);

        controller.initialisiereModel(null);

        verify(sessionMap, atLeastOnce()).put(SESSION_KEY_QUICKLINKS, quicklinksModel);

        assertEquals(2, quicklinksModel.getQuicklinksGruppen().size());
        QuicklinksGruppeModel quicklinksGruppeModel = quicklinksModel.getQuicklinksGruppen().get("gruppe1");
        assertEquals("gruppe1", quicklinksGruppeModel.getGruppeId());
        assertEquals("gruppe1Text", quicklinksGruppeModel.getAnzuzeigenderGruppenname());
        assertTrue(quicklinksGruppeModel.isSichtbar());

        quicklinksGruppeModel = quicklinksModel.getQuicklinksGruppen().get("gruppe2");
        assertEquals("gruppe2", quicklinksGruppeModel.getGruppeId());
        assertEquals("gruppe2Text", quicklinksGruppeModel.getAnzuzeigenderGruppenname());
        assertFalse(quicklinksGruppeModel.isSichtbar());
    }

    @Test
    public void fuegeQuicklinkHinzu() {
        QuicklinksModel quicklinksModel = new QuicklinksModel();

        when(sessionMap.get(SESSION_KEY_QUICKLINKS)).thenReturn(quicklinksModel);

        QuicklinksElementModel neuesElement = new QuicklinksElementModel();

        neuesElement.setId("neuesElementId");
        neuesElement.setLink("neuesElementLink");
        neuesElement.setAnzuzeigenderText("neuesElementText");

        controller.fuegeQuicklinkHinzu(neuesElement, "neueGruppe", 5);

        verify(sessionMap, atLeastOnce()).put(SESSION_KEY_QUICKLINKS, quicklinksModel);

        QuicklinksGruppeModel gruppeModel = quicklinksModel.getQuicklinksGruppen().get("neueGruppe");

        assertNotNull(gruppeModel);

        assertEquals(1, gruppeModel.getQuicklinksElemente().size());
        assertEquals("neueGruppe", gruppeModel.getGruppeId());
        assertEquals("neueGruppe", gruppeModel.getAnzuzeigenderGruppenname());
        assertTrue(gruppeModel.isSichtbar());
        assertEquals("neuesElementId", gruppeModel.getQuicklinksElemente().get(0).getId());
        assertEquals("neuesElementText", gruppeModel.getQuicklinksElemente().get(0).getAnzuzeigenderText());
        assertEquals("neuesElementLink", gruppeModel.getQuicklinksElemente().get(0).getLink());
    }

    private QuicklinksModel setupQuicklinksModel() {
        QuicklinksElementModel element1 = new QuicklinksElementModel();
        element1.setId("element1");

        QuicklinksElementModel element2 = new QuicklinksElementModel();
        element2.setId("element2");

        QuicklinksModel quicklinksModel = new QuicklinksModel();

        QuicklinksGruppeModel gruppe1 = new QuicklinksGruppeModel();
        gruppe1.setGruppeId("gruppe1");
        gruppe1.setQuicklinksElemente(new ArrayList<QuicklinksElementModel>());
        gruppe1.getQuicklinksElemente().add(element1);

        QuicklinksGruppeModel gruppe2 = new QuicklinksGruppeModel();
        gruppe2.setGruppeId("gruppe2");
        gruppe2.setQuicklinksElemente(new ArrayList<QuicklinksElementModel>());
        gruppe2.getQuicklinksElemente().add(element1);
        gruppe2.getQuicklinksElemente().add(element2);

        quicklinksModel.getQuicklinksGruppen().put("gruppe1", gruppe1);
        quicklinksModel.getQuicklinksGruppen().put("gruppe2", gruppe2);

        return quicklinksModel;
    }

    @Test
    public void entferneQuicklink() {
        QuicklinksModel quicklinksModel = setupQuicklinksModel();

        when(sessionMap.get(SESSION_KEY_QUICKLINKS)).thenReturn(quicklinksModel);

        controller.entferneQuicklink("element1");

        verify(sessionMap, atLeastOnce()).put(SESSION_KEY_QUICKLINKS, quicklinksModel);
        assertTrue(quicklinksModel.getQuicklinksGruppen().get("gruppe1").getQuicklinksElemente().isEmpty());
        assertEquals(1, quicklinksModel.getQuicklinksGruppen().get("gruppe2").getQuicklinksElemente().size());
    }

    @Test
    public void entferneQuicklinkAusGruppe() {
        QuicklinksModel quicklinksModel = setupQuicklinksModel();

        when(sessionMap.get(SESSION_KEY_QUICKLINKS)).thenReturn(quicklinksModel);

        controller.entferneQuicklink("gruppe1", "element1");

        verify(sessionMap, atLeastOnce()).put(SESSION_KEY_QUICKLINKS, quicklinksModel);
        assertTrue(quicklinksModel.getQuicklinksGruppen().get("gruppe1").getQuicklinksElemente().isEmpty());
        assertEquals(2, quicklinksModel.getQuicklinksGruppen().get("gruppe2").getQuicklinksElemente().size());
    }

    @Test
    public void verschiebeQuicklinks() {
        QuicklinksModel quicklinksModel = setupQuicklinksModel();

        when(sessionMap.get(SESSION_KEY_QUICKLINKS)).thenReturn(quicklinksModel);

        controller.verschiebeQuicklinks("gruppe2", "gruppe1", 3);

        verify(sessionMap, atLeastOnce()).put(SESSION_KEY_QUICKLINKS, quicklinksModel);

        assertTrue(quicklinksModel.getQuicklinksGruppen().get("gruppe2").getQuicklinksElemente().isEmpty());
        assertEquals(2, quicklinksModel.getQuicklinksGruppen().get("gruppe1").getQuicklinksElemente().size());
    }
}