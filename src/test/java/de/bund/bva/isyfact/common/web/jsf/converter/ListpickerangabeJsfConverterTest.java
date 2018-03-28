package de.bund.bva.isyfact.common.web.jsf.converter;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.Listpickerangabe;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ListpickerangabeJsfConverterTest {

    @Test
    public void getAsObjectNurSchluessel() {
        String schluessel = "schluessel";

        ListpickerangabeJsfConverter listpickerangabeJsfConverter = new ListpickerangabeJsfConverter();

        Listpickerangabe angabe =
            (Listpickerangabe) listpickerangabeJsfConverter.getAsObject(null, null, schluessel);
        assertEquals(schluessel, angabe.getText());
    }

    @Test
    public void getAsObjectSchluesselUndWert() {
        String schluessel = "schluessel";
        String wert = "wert";
        String trenner = " - ";

        ListpickerangabeJsfConverter listpickerangabeJsfConverter = new ListpickerangabeJsfConverter();

        Listpickerangabe angabe = (Listpickerangabe) listpickerangabeJsfConverter
            .getAsObject(null, null, schluessel + trenner + wert);

        assertEquals(schluessel, angabe.getText());

    }

    @Test
    public void getAsObjectUngueltig() {
        ListpickerangabeJsfConverter listpickerangabeJsfConverter = new ListpickerangabeJsfConverter();

        String ungueltig = "- Wert";

        assertNull(listpickerangabeJsfConverter.getAsObject(null, null, ungueltig));
    }

    @Test
    public void getAsString() {
        ListpickerangabeJsfConverter listpickerangabeJsfConverter = new ListpickerangabeJsfConverter();

        Listpickerangabe listpickerangabe = new Listpickerangabe();
        listpickerangabe.setText("text");

        assertEquals("text", listpickerangabeJsfConverter.getAsString(null, null, listpickerangabe));
    }
}