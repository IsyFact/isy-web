/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

/**
 * Zeigt das Overlay für eine Sonderzeichenliste an.
 */
public class SpecialCharPickerWidget extends UIComponentBase {

    /** Default resource für den Zugriff auf die Sonderzeichen. */
    private static final String DEFAULT_RESOURCE = "/resources/isy-web/sonderzeichen/sonderzeichen.txt";

    /** Das Basiszeichen, das verwendet wird, wenn keines definiert ist. */
    public static final char DEFAULT_BASISZEICHEN = '*';

    /** Die Ressource mit den anzuzeigenden Sonderzeichen, absolut im Klassenpfad. */
    private String resource;

    /** Das Mapping für die Sonderzeichen. */
    private transient Map<String, SpecialCharPickerMapping> sonderzeichenMapping;

    /** Das Mapping für die Sonderzeichen. */
    private transient Multimap<Character, String> basiszeichenZuSonderzeichen;

    private static final Pattern zeichen = Pattern.compile("[^;]+");
    private static final Pattern zeichenMapping = Pattern.compile("([^;]+);([^;])");
    private static final Pattern zeichenMappingTitel = Pattern.compile("([^;]+);([^;]?);(.+)");

    /**
     * Gibt die Id der Familie der Komponente zurück.
     *
     * @return die Id der Komponenten-Familie.
     */
    @Override
    public String getFamily() {
        return ("de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker.SpecialCharPickerWidget");
    }

    /**
     * Speichert den Zustand der Komponente in einem serialisierbaren Objekt und gibt dieses zurück. Das
     * Objekt ist so gebaut, dass {@link #restoreState} den Zustand der Komponente wieder aus dem Objekt
     * herstellen kann.
     *
     * @param context
     *            der aktuelle FacesContext.
     *
     * @return das Objekt mit dem Zustand der Komponente.
     *
     * @see javax.faces.component.UIComponentBase#saveState(FacesContext)
     */
    @Override
    public Object saveState(FacesContext context) {
        Object[] state = new Object[2];
        state[0] = super.saveState(context);
        state[1] = getResource();
        return state;
    }

    /**
     * Liest den Zustand der Komponente aus einem serialisierbaren Objekt und setzt den Zustand in dieser
     * Komponente Das Objekt ist mit {@link #saveState} erzeugt.
     *
     * @param context
     *            der aktuelle Faces-Kontext.
     * @param state
     *            der Zustand, der wiederhergestellt werden soll, nicht null.
     *
     * @see javax.faces.component.UIComponentBase#restoreState(FacesContext, Object)
     */
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        this.resource = (String) values[1];
    }

    /**
     * Gibt die Ressource mit den anzuzeigenden Sonderzeichen, absolut im Klassenpfad, zurück.
     *
     * @return die Ressource, nicht null.
     */
    public String getResource() {
        if (this.resource != null) {
            return this.resource;
        }
        ValueExpression expression = getValueExpression("resource");
        if (expression != null) {
            return (String) expression.getValue(getFacesContext().getELContext());
        }

        // Default
        return DEFAULT_RESOURCE;
    }

    /**
     * Setzt die Ressource mit den anzuzeigenden Sonderzeichen, absolut im Klassenpfad.
     *
     * @param resource
     *            die Ressource, nicht null.
     *
     * @throws NullPointerException
     *             wenn resource null ist.
     */
    public void setResource(String resource) {
        if (resource == null) {
            throw new NullPointerException("resource ist null");
        }
        this.resource = resource;
    }

    /**
     * Gibt die Komponente als String zurück.
     *
     * @return die Komponente als String.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("resource=").append(this.resource);
        return result.toString();
    }

    /**
     * Gibt alle Basiszeichen des Mappings zurück, geordnet nach ihrem ersten Auftreten.
     *
     * @return die Liste aller Basiszeichen, nicht null, enthält nicht null.
     */
    public Collection<Character> getBasiszeichenListe() {
        List<Character> basiszeichenListe = new ArrayList<>();
        for (Map.Entry<String, SpecialCharPickerMapping> mapping : getSonderzeichenMappings().entrySet()) {
            Character basiszeichen = mapping.getValue().getBasiszeichen();
            String sonderzeichen = mapping.getKey();
            if (basiszeichen == null) {
                basiszeichen = DEFAULT_BASISZEICHEN;
            }
            if (sonderzeichen.equalsIgnoreCase(basiszeichen.toString())) {
                // wenn das Zeichen auf sich selbst gemappt ist definiert das noch kein basiszeichen
                continue;
            }
            if (!basiszeichenListe.contains(basiszeichen)) {
                basiszeichenListe.add(basiszeichen);
            }
        }

        Collections.sort(basiszeichenListe);

        return basiszeichenListe;
    }

    /**
     * Gibt alle erlaubten Zeichen als String zurück.
     *
     * @return alle erlaubten Zeichen, nicht null.
     */
    public String getSonderzeichenAsString() {
        StringBuilder result = new StringBuilder();
        for (String sonderzeichen : getSonderzeichenMappings().keySet()) {
            result.append(sonderzeichen);
        }
        return result.toString();
    }

    /**
     * Gibt das eingelesene Sonderzeichen-Mapping zurück. Wenn das Mapping noch nicht eingelesen ist, dann
     * wird es eingelesen.
     *
     * @return das eingelesene Sonderzeichen-Mapping.
     */
    public Map<String, SpecialCharPickerMapping> getSonderzeichenMappings() {
        if (sonderzeichenMapping != null) {
            return sonderzeichenMapping;
        }

        sonderzeichenMapping = new TreeMap<>();
        basiszeichenZuSonderzeichen = TreeMultimap.create();

        try (InputStream mappingResource = SpecialCharPickerWidget.class.getResourceAsStream(getResource());
             BufferedReader reader = new BufferedReader(new InputStreamReader(mappingResource, StandardCharsets.UTF_8))) {
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] mapping = leseMappingZeile(line);

                sonderzeichenMapping.put(mapping[0], new SpecialCharPickerMapping(mapping[1].charAt(0), mapping[2]));
                basiszeichenZuSonderzeichen.put(mapping[1].charAt(0), mapping[0]);
            }

        } catch (NullPointerException | IOException e) {
            throw new IllegalStateException("Konnte Sonderzeichen-Mapping nicht einlesen", e);
        }

        return sonderzeichenMapping;
    }

    private String[] leseMappingZeile(String zeile) {

        String[] mapping = new String[3];
        mapping[1] = "" + DEFAULT_BASISZEICHEN;

        if (";".equals(zeile)) {
            mapping[0] = ";";
            return mapping;
        }

        Matcher matcher;
        if ((matcher = zeichen.matcher(zeile)).matches()) {
            mapping[0] = matcher.group();
        } else if ((matcher = zeichenMapping.matcher(zeile)).matches()) {
            mapping[0] = matcher.group(1);
            mapping[1] = matcher.group(2);
        } else if ((matcher = zeichenMappingTitel.matcher(zeile)).matches()) {
            mapping[0] = matcher.group(1);
            if (!matcher.group(2).isEmpty()) {
                mapping[1] = matcher.group(2);
            }
            mapping[2] = matcher.group(3);
        } else {
            throw new IllegalStateException("Die Zeile <" + zeile + "> ist nicht im erwarteten Format <Sonderzeichen>[;<Basiszeichen>][;<Titel>]");
        }

        return mapping;
    }

    public Multimap<Character, String> getBasiszeichenZuSonderzeichen() {
        if (this.basiszeichenZuSonderzeichen == null) {
            getSonderzeichenMappings();
        }
        return this.basiszeichenZuSonderzeichen;
    }
}
