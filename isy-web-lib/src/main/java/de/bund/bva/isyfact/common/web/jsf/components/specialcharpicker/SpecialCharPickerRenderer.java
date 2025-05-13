package de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker;

import java.io.IOException;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * Rendert ein SonderzeichenOverlay als HTML.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class SpecialCharPickerRenderer extends Renderer {

    /** Name des HTML div Elements. */
    private static final String DIV = "div";

    /** Name des HTML div Elements. */
    private static final String TABLE = "table";

    /** Name des HTML div Elements. */
    private static final String TR = "tr";

    /** Name des HTML div Elements. */
    private static final String TD = "td";

    /** Name des HTML id Attributs. */
    private static final String ID_ATTR = "id";

    /** Name des data-filter Attributs. */
    private static final String DATA_FILTER_ATTR = "data-filter";

    /** Name des data-base-char Attributs. */
    private static final String DATA_BASE_CHAR_ATTR = "data-base-char";

    /** Name des HTML class Attributs. */
    private static final String CLASS_ATTR = "class";

    /** Name des HTML style Attributs. */
    private static final String STYLE_ATTR = "style";

    /** Name des HTML tabindex Attributs. */
    private static final String TABINDEX_ATTR = "tabindex";

    /** Name des HTML title Attributs. */
    private static final String TITLE_ATTR = "title";

    /**
     * Rendert den Anfang des SonderzeichenOverlay-Elements.
     *
     * @param context
     *            der aktuelle FacesContext.
     * @param component
     *            die zu rendernde Komponente.
     *
     * @throws IOException
     *             wenn es ein IO-Problem beim Rendern gibt.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        SpecialCharPickerWidget widget = (SpecialCharPickerWidget) component;
        String clientId = component.getClientId(context);

        // START: main div
        writer.startElement(DIV, widget);
        writer.writeAttribute(ID_ATTR, clientId, null);
        writer.writeAttribute(CLASS_ATTR,
            "picker charpicker dropdown-menu picker-dropdown picker-orient-left picker-orient-top " + //
                " special-char-picker-widget ", null);
        writer.writeAttribute(STYLE_ATTR, "display: none;", null);
        writer.writeAttribute(TABINDEX_ATTR, "-1", null);

        // Filter bar
        writer.startElement(DIV, widget);
        writer.writeAttribute(ID_ATTR, clientId, null);
        writer.writeAttribute(CLASS_ATTR, "charpicker-chars filter-bar", null);
        writer.startElement(TABLE, widget);
        writer.writeAttribute(CLASS_ATTR, "table-condensed filter-table", null);
        writer.startElement(TR, widget);
        Collection<Character> basiszeichenListe = widget.getBasiszeichenListe();
        for (Character basiszeichen : basiszeichenListe) {
            writer.startElement(TD, widget);
            writer.writeAttribute(ID_ATTR, clientId + ":" + basiszeichen, null);
            writer.writeAttribute(CLASS_ATTR, "filter-item", null);
            if (SpecialCharPickerWidget.DEFAULT_BASISZEICHEN != basiszeichen) {
                writer.writeAttribute(DATA_FILTER_ATTR,
                    new String(new char[] { basiszeichen }).toLowerCase(), null);
            }
            writer.writeText(basiszeichen, null);
            writer.endElement(TD);
        }
        writer.endElement(TR);
        writer.endElement(TABLE);
        writer.endElement(DIV);

        // Main table
        writer.startElement(DIV, widget);
        writer.writeAttribute(ID_ATTR, clientId, null);
        writer.writeAttribute(CLASS_ATTR, "charpicker-chars special-char-box", null);
        int counter = 0;
        writer.startElement(TABLE, widget);
        writer.writeAttribute(CLASS_ATTR, "table-condensed", null);
        writer.startElement(TR, widget);

        for (Character bz : widget.getBasiszeichenListe()) {

            for (String sonderzeichen : widget.getBasiszeichenZuSonderzeichen().get(bz)) {

                SpecialCharPickerMapping mapping = widget.getSonderzeichenMappings().get(sonderzeichen);
                if (istNichtBasisZeichen(sonderzeichen, basiszeichenListe)) {
                    writer.startElement(TD, widget);
                    writer.writeAttribute(ID_ATTR, clientId + ":num:" + (counter++), null);
                    writer.writeAttribute(CLASS_ATTR, "special-char", null);
                    String titel = mapping.getTitel();
                    if (titel != null) {
                        writer.writeAttribute(TITLE_ATTR, titel, null);
                    }
                    char basiszeichen = mapping.getBasiszeichen();
                    if (SpecialCharPickerWidget.DEFAULT_BASISZEICHEN != basiszeichen) {
                        writer.writeAttribute(DATA_BASE_CHAR_ATTR,
                            new String(new char[] { basiszeichen }).toLowerCase(), null);
                    }
                    writer.writeText(sonderzeichen, null);
                    writer.endElement(TD);
                }
            }
        }
        writer.endElement(TR);
        writer.endElement(TABLE);
        writer.endElement(DIV); // END: main table

        writer.endElement(DIV); // END: main div
    }

    /**
     * Prüfen ob bestimmte Zeichen nicht ein Basiszeichen ist.
     *
     *
     * @param zeichen
     *            Zeichen
     * @param basiszeichenListe
     *            Liste Basiszeichen
     *
     * @return siehe die Beschreibung der Methode
     */
    private boolean istNichtBasisZeichen(String zeichen, Collection<Character> basiszeichenListe) {

        return !istBasisZeichen(zeichen, basiszeichenListe);
    }

    /**
     * Prüfen ob bestimmte Zeichen ein Basiszeichen ist.
     *
     * @param zeichen
     *            Zeichen
     * @param basiszeichenListe
     *            Liste Basiszeichen
     *
     * @return siehe die Beschreibung der Methode
     */
    private boolean istBasisZeichen(String zeichen, Collection<Character> basiszeichenListe) {
        String asterisk = Character.toString(SpecialCharPickerWidget.DEFAULT_BASISZEICHEN);

        if (asterisk.equals(zeichen)) {
            return false;
        } else {
            for (Character basiszeichenChar : basiszeichenListe) {
                if (basiszeichenChar.toString().equals(zeichen) || basiszeichenChar.toString().toLowerCase().equals(zeichen)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Rendert das Ende eines BreadCrumbTrail-Elements.
     *
     * @param context
     *            der aktuelle FacesContext.
     * @param component
     *            die zu rendernde Komponente.
     *
     * @throws IOException
     *             wenn es ein IO-Problem beim Rendern gibt.
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }
}
