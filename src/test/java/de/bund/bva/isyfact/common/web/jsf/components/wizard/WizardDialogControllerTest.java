package de.bund.bva.isyfact.common.web.jsf.components.wizard;

import de.bund.bva.isyfact.common.web.exception.IsyFactTechnicalRuntimeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WizardDialogControllerTest {

    private static class TestWizardDialogController extends WizardDialogController {

        @Override
        public boolean cancel(WizardDialogModel model) {
            return false;
        }

        @Override
        public boolean finish(WizardDialogModel model) {
            return false;
        }
    }

    private TestWizardDialogController controller;

    private WizardDialogModel model;

    @Before
    public void setup() {
        controller = new TestWizardDialogController();

        model = new WizardDialogModel();

        for (int i = 0; i < 5; i++) {
            model.getWizardDialogPages().add(new WizardDialogPage("page" + i, "page" + i));
        }
    }

    @Test(expected = IsyFactTechnicalRuntimeException.class)
    public void initializeDefaultPagesKeinePages() {
        controller.initializeDefaultPages(new WizardDialogModel());
    }

    @Test
    public void initializeDefaultPages() {
        controller.initializeDefaultPages(model);

        assertEquals("page0", model.getActiveWizardDialogPageId());
        assertFalse(model.getWizardDialogPages().get(0).isButtonPreviousActivated());
        assertFalse(model.getWizardDialogPages().get(4).isButtonNextActivated());
        assertTrue(model.getWizardDialogPages().get(4).isButtonCompleteActivated());
        assertEquals("page0", model.getActiveWizardDialogPageId());

        for (WizardDialogPage page : model.getWizardDialogPages()) {
            assertTrue(page.isButtonAbortActivated());
        }
    }

    @Test(expected = IsyFactTechnicalRuntimeException.class)
    public void nextBeiLetzterPage() {
        model.setActiveWizardDialogPageId("page4");

        controller.next(model);
    }

    @Test
    public void next() {
        model.setActiveWizardDialogPageId("page3");

        controller.next(model);

        assertEquals("page4", model.getNextActiveWizardDialogPageId());
    }

    @Test(expected = IsyFactTechnicalRuntimeException.class)
    public void previousBeiErsterPage() {
        model.setActiveWizardDialogPageId("page0");

        controller.previous(model);
    }

    @Test
    public void previous() {
        model.setActiveWizardDialogPageId("page1");

        controller.previous(model);

        assertEquals("page0", model.getNextActiveWizardDialogPageId());
    }

}