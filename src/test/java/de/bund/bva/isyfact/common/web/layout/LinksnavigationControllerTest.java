package de.bund.bva.isyfact.common.web.layout;

import java.util.List;

import de.bund.bva.isyfact.aufrufkontext.AufrufKontext;
import de.bund.bva.isyfact.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LinksnavigationControllerTest {

    private Konfiguration konfiguration = mock(Konfiguration.class);

    private FlowDefinition flowDefinition = mock(FlowDefinition.class);

    private LinksnavigationController controller;

    private AufrufKontext aufrufKontext = mock(AufrufKontext.class);

    @Before
    public void setup() {
        RequestContext requestContext = mock(RequestContext.class);
        RequestContextHolder.setRequestContext(requestContext);

        FlowExecutionContext flowExecutionContext = mock(FlowExecutionContext.class);

        when(requestContext.getFlowExecutionContext()).thenReturn(flowExecutionContext);
        when(flowExecutionContext.getDefinition()).thenReturn(flowDefinition);


        AufrufKontextVerwalter aufrufKontextVerwalter = mock(AufrufKontextVerwalter.class);
        when(aufrufKontextVerwalter.getAufrufKontext()).thenReturn(aufrufKontext);

        controller = new LinksnavigationController();
        controller.setKonfiguration(konfiguration);
        controller.setAufrufKontextVerwalter(aufrufKontextVerwalter);

        reset(konfiguration);
    }

    @Test
    public void initialisiereModel() {
        when(konfiguration.getAsString("gui.linksnavigation.ids")).thenReturn("testFlow1,testFlow2");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.headline", null))
            .thenReturn("testFlow1Headline");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.1.text", null))
            .thenReturn("testFlow1Text1");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.1.link", null)).thenReturn("testFlow1");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.1.rollen", null))
            .thenReturn("testRolle1");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.2.text", null))
            .thenReturn("testFlow1Text2");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.2.link", null)).thenReturn("testFlow2");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.2.rollen", null))
            .thenReturn("testRolle2");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow2.headline", null))
            .thenReturn("testFlow1Headline");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow2.1.text", null))
            .thenReturn("testFlow2Text1");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow2.1.link", null)).thenReturn("testFlow2");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow2.1.rollen", null))
            .thenReturn("testRolle2");

        when(flowDefinition.getId()).thenReturn("testFlow1");

        when(aufrufKontext.getRolle()).thenReturn(new String[] { "testRolle1", "testRolle3" });

        ApplikationseiteModel model = new ApplikationseiteModel();

        controller.initialisiereModel(model);

        LinksnavigationModel linksnavigationModel = model.getLinksnavigationModel();

        assertNotNull(linksnavigationModel);
        assertEquals("testFlow1Headline", linksnavigationModel.getHeadline());

        List<LinksnavigationelementModel> elemente = linksnavigationModel.getLinksnavigationelemente();

        assertEquals(1, elemente.size());
        assertEquals("testFlow1Text1", elemente.get(0).getAnzuzeigenderText());
        assertEquals("testFlow1", elemente.get(0).getLink());
        assertTrue(elemente.get(0).isActive());
    }

    @Test
    public void ueberschreibeLinksnavigation() {
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.headline", null))
            .thenReturn("testFlow1Headline");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.1.text", null))
            .thenReturn("testFlow1Text1");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.1.link", null)).thenReturn("testFlow1");
        when(konfiguration.getAsString("gui.linksnavigation.testFlow1.1.rollen", null))
            .thenReturn("testRolle1");

        when(flowDefinition.getId()).thenReturn("testFlow1");

        when(aufrufKontext.getRolle()).thenReturn(new String[] { "testRolle1", "testRolle3" });

        ApplikationseiteModel model = new ApplikationseiteModel();
        model.setLinksnavigationModel(new LinksnavigationModel());
        model.getLinksnavigationModel().setHeadline("headline");

        LinksnavigationelementModel element = new LinksnavigationelementModel();
        element.setAnzuzeigenderText("text");
        element.setLink("link");
        model.getLinksnavigationModel().addLinksnavigationelementModel(element);

        controller.ueberschreibeLinksnavigation(model, "testFlow1");

        LinksnavigationModel linksnavigationModel = model.getLinksnavigationModel();

        assertNotNull(linksnavigationModel);
        assertEquals("testFlow1Headline", linksnavigationModel.getHeadline());

        List<LinksnavigationelementModel> elemente = linksnavigationModel.getLinksnavigationelemente();

        assertEquals(1, elemente.size());
        assertEquals("testFlow1Text1", elemente.get(0).getAnzuzeigenderText());
        assertEquals("testFlow1", elemente.get(0).getLink());
        assertTrue(elemente.get(0).isActive());
    }

    @Test
    public void entferneLinksnavigation() {
        ApplikationseiteModel model = new ApplikationseiteModel();
        controller.entferneLinksnavigation(model);

        assertNull(model.getLinksnavigationModel());
    }
}