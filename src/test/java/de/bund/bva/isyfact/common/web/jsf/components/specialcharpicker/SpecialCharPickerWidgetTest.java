package de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpecialCharPickerWidgetTest {

    @Test
    public void getResource() {
        SpecialCharPickerWidget widget = new SpecialCharPickerWidget();
        assertEquals("/resources/plis-web/sonderzeichen/sonderzeichen.txt", widget.getResource());

        widget.setResource("resource");
        assertEquals("resource", widget.getResource());
    }

    @Test
    public void getBasiszeichenListe() {
        SpecialCharPickerWidget widget = new SpecialCharPickerWidget();

        widget.setResource("/resources/plis-web/sonderzeichen/sonderzeichen2.txt");

        List<Character> expected = Arrays.asList('*', 'E');

        assertEquals(expected, widget.getBasiszeichenListe());
    }

    @Test
    public void getSonderzeichenAsString() {
        SpecialCharPickerWidget widget = new SpecialCharPickerWidget();

        widget.setResource("/resources/plis-web/sonderzeichen/sonderzeichen2.txt");

        assertEquals("A¢£©¼½¾€", widget.getSonderzeichenAsString());
    }

    @Test
    public void getSonderzeichenMappings() {
        SpecialCharPickerWidget widget = new SpecialCharPickerWidget();

        Map<String, SpecialCharPickerMapping> sonderzeichen = widget.getSonderzeichenMappings();

        assertNotNull(sonderzeichen);
        assertEquals(468, sonderzeichen.size());
        assertEquals("Euro", sonderzeichen.get("€").getTitel());
        assertEquals('O', sonderzeichen.get("ø").getBasiszeichen());
        assertEquals('I', sonderzeichen.get("İ").getBasiszeichen());
        assertEquals('I', sonderzeichen.get("ı").getBasiszeichen());
    }

    @Test(expected = IllegalStateException.class)
    public void getSonderzeichenMappingResourceNichtVorhanden() {
        SpecialCharPickerWidget widget = new SpecialCharPickerWidget();
        widget.setResource("nichtVorhanden");
        widget.getSonderzeichenMappings();
    }

    @Test
    public void getBasiszeichenZuSonderzeichen() {
        SpecialCharPickerWidget widget = new SpecialCharPickerWidget();

        Map<String, SpecialCharPickerMapping> sonderzeichen = widget.getSonderzeichenMappings();

        Multimap<Character, String> basiszeichenZuSonderzeichen = widget.getBasiszeichenZuSonderzeichen();

        assertNotNull(basiszeichenZuSonderzeichen);

        assertEquals(468, basiszeichenZuSonderzeichen.size());
        assertEquals(83, basiszeichenZuSonderzeichen.get('*').size());
        assertEquals(41, basiszeichenZuSonderzeichen.get('A').size());
        assertEquals(12, basiszeichenZuSonderzeichen.get('Z').size());
    }
}