package de.bund.bva.isyfact.common.web.jsf.components.charpickerdinspec91379;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ZeichenObjektRepositoryTest {

    /**
     * Tests if the complete list of characters can be read.
     */
    @Test
    public void getZeichenliste() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste();
        assertEquals(902, zeichenliste.size());
    }

    /**
     * Tests if all characters of the text file are read and returned in the character list.
     * This includes that the letter 'LATIN CAPITAL LETTER L WITH COMBINING RING BELOW AND COMBINING MACRON'
     * (as an example for a letter consisting of multiple Unicode character) and the semicolon are
     * recognized correctly.
     */
    @Test
    public void getZeichenlisteNoTypeTest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-test.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste();
        assertEquals(8, zeichenliste.size());

        // Tests if the object for a character is created correctly.
        assertEquals("L̥̄", zeichenliste.get(0).getZeichen());
        assertEquals("L", zeichenliste.get(0).getGrundzeichen());
        assertEquals(Schriftzeichengruppe.LATEIN, zeichenliste.get(0).getSchriftzeichengruppe());
        assertEquals("LATIN CAPITAL LETTER L WITH COMBINING RING BELOW AND COMBINING MACRON",
                zeichenliste.get(0).getName());
        assertEquals("<U+004C,U+0325,U+0304>", zeichenliste.get(0).getCodepoint());

        // Tests if the character L̥̄ correctly consists of 3 characters.
        assertEquals(3, zeichenliste.get(0).getZeichen().length());

        // Tests if the semicolon is recognized as a character correctly, although it is also used as a separator.
        assertEquals(";", zeichenliste.get(2).getZeichen());

        // Tests if the base symbol of a symbol is "*" if not specified.
        assertEquals("", zeichenliste.get(2).getGrundzeichen());
    }

    /**
     * Tests if the characters are filtered by their data type correctly.
     */
    @Test
    public void getZeichenlistedatentypATest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-test.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste(Datentyp.DATENTYP_A);
        assertEquals(2, zeichenliste.size());
    }

    /**
     * Tests if the characters are filtered by their data type correctly.
     */
    @Test
    public void getZeichenlistedatentypBTest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-test.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste(Datentyp.DATENTYP_B);
        assertEquals(3, zeichenliste.size());
    }

    /**
     * Tests if the characters are filtered by their data type correctly.
     */
    @Test
    public void getZeichenlistedatentypCTest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-test.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste(Datentyp.DATENTYP_C);
        assertEquals(5, zeichenliste.size());
    }

    /**
     * Tests if the characters are filtered by their data type correctly.
     */
    @Test
    public void getZeichenlistedatentypDTest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-test.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste(Datentyp.DATENTYP_D);
        assertEquals(6, zeichenliste.size());
    }

    /**
     * Tests if the characters are filtered by their data type correctly.
     */
    @Test
    public void getZeichenlistedatentypETest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-test.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste(Datentyp.DATENTYP_E);
        assertEquals(8, zeichenliste.size());
    }

    /**
     * Tests if getZeichenliste returns an empty list when all lines of the resource file
     * contain an invalid group.
     */
    @Test
    public void getZeichenlisteInvalidGroupTest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-invalid-group.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste();
        assertEquals(0, zeichenliste.size());
    }

    /**
     * Tests if getZeichenliste returns an empty list when all lines of the resource file
     * contain a base symbol that consists of more than one character.
     */
    @Test
    public void getZeichenlisteInvalidBaseSymbol() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource(
                "/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-invalid-base-symbol.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste();
        assertEquals(0, zeichenliste.size());
    }

    /**
     * Tests if getZeichenliste returns an empty list when all lines of the resource file
     * do not contain the correct number of entries.
     */
    @Test
    public void getZeichenlisteInvalidLine() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379-invalid-line.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste();
        assertEquals(0, zeichenliste.size());
    }

    /**
     * Tests if getZeichenliste returns an empty list when all lines of the resource file
     * do not contain the correct number of entries.
     */
    @Test
    public void getZeichenlisteNoResource() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();
        repository.setResource("/resources/isy-web/sonderzeichen/not-existing.txt");
        List<ZeichenObjekt> zeichenliste = repository.getZeichenliste();
        assertEquals(0, zeichenliste.size());
    }

    /**
     * Tests if the method that filters illegal characters out when the character group is not matching
     * the data type.
     */
    @Test
    public void legalTest() {
        ZeichenObjektRepository repository = new ZeichenObjektRepository();

        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.LATEIN, Datentyp.DATENTYP_A));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.LATEIN, Datentyp.DATENTYP_B));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.LATEIN, Datentyp.DATENTYP_C));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.LATEIN, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.LATEIN, Datentyp.DATENTYP_E));

        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N1, Datentyp.DATENTYP_A));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N1, Datentyp.DATENTYP_B));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N1, Datentyp.DATENTYP_C));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N1, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N1, Datentyp.DATENTYP_E));

        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N2, Datentyp.DATENTYP_A));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N2, Datentyp.DATENTYP_B));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N2, Datentyp.DATENTYP_C));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N2, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N2, Datentyp.DATENTYP_E));

        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N3, Datentyp.DATENTYP_A));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N3, Datentyp.DATENTYP_B));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N3, Datentyp.DATENTYP_C));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N3, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N3, Datentyp.DATENTYP_E));

        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N4, Datentyp.DATENTYP_A));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N4, Datentyp.DATENTYP_B));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N4, Datentyp.DATENTYP_C));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N4, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.N4, Datentyp.DATENTYP_E));

        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.E1, Datentyp.DATENTYP_A));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.E1, Datentyp.DATENTYP_B));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.E1, Datentyp.DATENTYP_C));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.E1, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.E1, Datentyp.DATENTYP_E));

        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.GRIECHISCH, Datentyp.DATENTYP_A));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.GRIECHISCH, Datentyp.DATENTYP_B));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.GRIECHISCH, Datentyp.DATENTYP_C));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.GRIECHISCH, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.GRIECHISCH, Datentyp.DATENTYP_E));

        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.KYRILLISCH, Datentyp.DATENTYP_A));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.KYRILLISCH, Datentyp.DATENTYP_B));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.KYRILLISCH, Datentyp.DATENTYP_C));
        assertFalse(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.KYRILLISCH, Datentyp.DATENTYP_D));
        assertTrue(repository.schriftzeichengruppeIsLegal(Schriftzeichengruppe.KYRILLISCH, Datentyp.DATENTYP_E));
    }


}