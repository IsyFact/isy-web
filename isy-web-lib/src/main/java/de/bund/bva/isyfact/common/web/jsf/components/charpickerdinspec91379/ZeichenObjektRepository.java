package de.bund.bva.isyfact.common.web.jsf.components.charpickerdinspec91379;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker.SpecialCharPickerWidget;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

public class ZeichenObjektRepository {

    /** List of character objects. */
    private List<ZeichenObjekt> zeichenObjektListe;

    /** Logger. */
    private static final IsyLogger LOG =
            IsyLoggerFactory.getLogger(ZeichenObjektRepository.class);

    /** Default resource for access to all usable DIN SPEC 91379 characters. */
    private static final String DEFAULT_RESOURCE =
            "/resources/isy-web/sonderzeichen/sonderzeichen-din-spec-91379.txt";

    /** The resource of all characters which will be displayed. */
    private String resource;

    /**
     * Returns the resource path. Default path is returned if variable has not been set.
     * @return resource file path
     */
    public String getResource() {
        if (resource != null) {
            return resource;
        }
        return DEFAULT_RESOURCE;
    }

    /**
     * Sets the resource of the characters on a custom path and deletes the zeichenObjektListe.
     * @param resource Path of the resource
     */
    public void setResource(String resource) {
        this.zeichenObjektListe = null;
        this.resource = resource;
    }

    /**
     * Sets the resource back to default and deletes the zeichenObjektListe.
     */
    public void resetResource() {
        this.zeichenObjektListe = null;
        this.resource = null;
    }

    /**
     * Returns all characters that are allowed in DIN SPEC 91379.
     * @return List of all usable character objects
     */
    public List<ZeichenObjekt> getZeichenliste() {
        return getZeichenliste(Datentyp.DATENTYP_E);
    }

    /**
     * Returns all characters that are allowed in the passed data type.
     * @param typ Data type that is allowed to use
     * @return List of character objects that are allowed to use
     */
    public List<ZeichenObjekt> getZeichenliste(Datentyp typ) {
        if (zeichenObjektListe == null || zeichenObjektListe.isEmpty()) {
            String line;
            zeichenObjektListe = new ArrayList<>();

            int currentLine = 1;
            try (InputStream inputResource = SpecialCharPickerWidget.class.getResourceAsStream(getResource());
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputResource, StandardCharsets.UTF_8))) {
                while ((line = reader.readLine()) != null) {
                    // Separating the first entry of the line from the rest of the String.
                    // This is necessary in case the first character of a row is a semicolon, because
                    // it is also used as a separator for the split function.
                    int index = line.indexOf(";", 1);
                    String zeichen = line.substring(0, index);
                    line = line.substring(index + 1);
                    String[] split = line.split(";");

                    // Add a character object to the list.
                    if (lineIsValid(split, getResource(), currentLine)) {
                        zeichenObjektListe.add(new ZeichenObjekt(zeichen, split[0],
                                Schriftzeichengruppe.valueOf(split[1]),
                                split[2], split[3]));
                    }
                    currentLine++;
                }
            } catch (NullPointerException | IOException  | StringIndexOutOfBoundsException e) {
                LOG.warn(EreignisSchluessel.E_SONDERZEICHEN_EINLESEN_FEHLER, "Sonderzeichenliste " +
                        getResource() + " konnte nicht gelesen werden.");
                return Collections.emptyList();
            }

        }
        return zeichenObjektListe.stream()
                .filter(zeichenObjekt -> schriftzeichengruppeIsLegal(
                        zeichenObjekt.getSchriftzeichengruppe(), typ))
                .collect(Collectors.toList());
    }

    /**
     * Validates if a character object can be created from a line read by the BufferedReader.
     * @param split Contains all entries of the line except the first one.
     * @param resource Path of the loaded resource
     * @param currentLine number of the current line
     * @return 'true' if a character object can be created from the line, 'false' if not
     */
    private boolean lineIsValid(String[] split, String resource, int currentLine) {
        if (split.length != 4) {
            LOG.warn(EreignisSchluessel.E_SONDERZEICHEN_EINLESEN_FEHLER, "Fehler beim Einlesen " +
                    "der Sonderzeichenliste " + resource + " in Zeile " + currentLine + ":\n" +
                    "Die Anzahl der Einträge ist nicht korrekt");
            return false;
        }
        if (split[0].length() > 1) {
            LOG.warn(EreignisSchluessel.E_SONDERZEICHEN_EINLESEN_FEHLER, "Fehler beim Einlesen " +
                    "der Sonderzeichenliste " + resource + " in Zeile " + currentLine + ":\n" +
                    "Der Grundbuchstabe für das Zeichen ist nicht korrekt");
            return false;
        }
        try {
            Schriftzeichengruppe.valueOf(split[1]);
        } catch (IllegalArgumentException e) {
            LOG.warn(EreignisSchluessel.E_SONDERZEICHEN_EINLESEN_FEHLER, "Fehler beim Einlesen " +
                    "der Sonderzeichenliste " + resource + " in Zeile " + currentLine + ":\n" +
                    "Die angegebene Schriftzeichengruppe ist nicht korrekt");
            return false;
        }
        return true;
    }

    /**
     * Determines if a character of a given character group is allowed to be used in the given data type.
     * @param gruppe The group of a character
     * @param typ The data type of the char picker
     * @return 'true' if the character group is included in data type, 'false' if not
     */
    public boolean schriftzeichengruppeIsLegal(Schriftzeichengruppe gruppe, Datentyp typ) {
        boolean isLegal;
        switch (typ) {
            case DATENTYP_A:
                isLegal = gruppe == Schriftzeichengruppe.LATEIN || gruppe == Schriftzeichengruppe.N1;
                break;
            case DATENTYP_B:
                isLegal = gruppe == Schriftzeichengruppe.LATEIN || gruppe == Schriftzeichengruppe.N1
                        || gruppe == Schriftzeichengruppe.N2;
                break;
            case DATENTYP_C:
                isLegal = gruppe == Schriftzeichengruppe.LATEIN || gruppe == Schriftzeichengruppe.N1
                        || gruppe == Schriftzeichengruppe.N2 || gruppe == Schriftzeichengruppe.N3
                        || gruppe == Schriftzeichengruppe.N4;
                break;
            case DATENTYP_D:
                isLegal = gruppe == Schriftzeichengruppe.LATEIN || gruppe == Schriftzeichengruppe.N1
                        || gruppe == Schriftzeichengruppe.N2 || gruppe == Schriftzeichengruppe.N3
                        || gruppe == Schriftzeichengruppe.E1 || gruppe == Schriftzeichengruppe.GRIECHISCH;
                break;
            case DATENTYP_E:
                isLegal = gruppe == Schriftzeichengruppe.LATEIN || gruppe == Schriftzeichengruppe.N1
                        || gruppe == Schriftzeichengruppe.N2 || gruppe == Schriftzeichengruppe.N3
                        || gruppe == Schriftzeichengruppe.N4 || gruppe == Schriftzeichengruppe.E1
                        || gruppe == Schriftzeichengruppe.GRIECHISCH
                        || gruppe == Schriftzeichengruppe.KYRILLISCH;
                break;
            default:
                isLegal = false;
                break;

        }
        return isLegal;
    }
}
