package de.bund.bva.isyfact.common.web.jsf.components.listpicker.behoerde;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.bund.bva.isyfact.util.spring.MessageSourceHolder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AbstractBehoerdeListpickerControllerTest {

    private static class TestAbstractBehoerdeListpickerController
        extends AbstractBehoerdeListpickerController {

        public TestAbstractBehoerdeListpickerController(int maxElemente) {
            super(maxElemente);
        }

        @Override
        public List<BehoerdeListpickerItem> erzeugeBehoerdeListpickerItemListe() {
            return null;
        }
    }

    private AbstractBehoerdeListpickerController controller =
        mock(AbstractBehoerdeListpickerController.class, CALLS_REAL_METHODS);

    private static final List<BehoerdeListpickerItem> items = new ArrayList<>();

    @Before
    public void setup() {
        items.clear();
        items.add(new BehoerdeListpickerItem("amt", "Name Amt"));
        items.add(new BehoerdeListpickerItem("behoerde", "Name Behoerde"));
        items.add(new BehoerdeListpickerItem("institut", "Name Institut"));

        when(controller.erzeugeBehoerdeListpickerItemListe()).thenReturn(items);
    }

    @Test
    public void filterLeer() {
        BehoerdeListpickerModel model = new BehoerdeListpickerModel();
        model.setFilter("");

        controller.setMaxElemente(3);
        controller.filter(model);

        assertEquals(3, model.getItemCount());
        assertEquals(3, model.getItems().size());
    }

    @Test
    public void filter() {
        BehoerdeListpickerModel model = new BehoerdeListpickerModel();
        model.setFilter("amt");

        controller.setMaxElemente(3);
        controller.filter(model);

        assertEquals(1, model.getItemCount());
        assertEquals(1, model.getItems().size());
        assertEquals("amt", model.getItems().get(0).getKennzeichen());
    }

    @Test
    public void erzeugeListpickerModelFuerBehoerden() {
        MessageSource messageSource = mock(MessageSource.class);
        MessageSourceHolder messageSourceHolder = new MessageSourceHolder();
        messageSourceHolder.setMessageSource(messageSource);

        when(messageSource
            .getMessage("MEL_Behoerdelistpicker_weitere_Elemente", new Object[] { null }, Locale.GERMANY))
            .thenReturn("nachrichtMehrElemente");

        controller.setMaxElemente(2);
        BehoerdeListpickerModel model = controller.erzeugeListpickerModelFuerBehoerden();

        // TODO: Bug? Sollte 2 statt 3 sein?
        assertEquals(3, model.getItemCount());
        assertEquals(2, model.getItems().size());
        assertEquals("nachrichtMehrElemente", model.getMessageMoreElements());
    }
}