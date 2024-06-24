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
package de.bund.bva.isyfact.common.web.global;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * Eine Helper-Klasse mit Funktionen, welche die Layoutelemente benötigten.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Component
public class JsfHelper {

    /**
     * Escapet die Identifier, damit sie von JS-Bibliotheken wie JQuery korrekt gelesen werden können.
     *
     * @param identifier
     *            Der Identifier.
     *
     * @return Der neue Identifier.
     */
    public String escapeIdentifier(String identifier) {

        if (identifier == null) {
            return null;
        }

        String newIdentifier = "";

        newIdentifier = identifier.replace(":", "-");
        newIdentifier = newIdentifier.replace(".", "-");

        return newIdentifier;

    }

    /**
     * Esacpt die Identifier, damit sie von JS-Bibliotheken wie JQuery korrekt gelesen werden können.
     * Zusätzlich wird ein Präfix vorgesetzt. Als Rückgabewert wird ein mit dem angegebenen Separator
     * separierter String mit den neuen Identifier-Werten zurückgegeben.
     *
     * @param prefix
     *            Der Prefix für den Identifier.
     * @param identifier
     *            Der Identifier.
     * @param separator
     *            Der Separator.
     *
     * @return Ein separierter String mit allen neuen Identifiern.
     */
    public String escapeIdentifiersWithPrefix(String prefix, List<String> identifier, String separator) {

        List<String> resultParts = new ArrayList<String>();

        String prefixEscaped = escapeIdentifier(prefix);

        for (String id : identifier) {
            resultParts.add(prefixEscaped + escapeIdentifier(id));
        }

        return Joiner.on(separator).join(resultParts);

    }

    /**
     * Überprüft ob ein String in einer Liste an Strings vorhanden ist. Die Liste wird dabei als einfach
     * String mit einem Seperator-Zeichen (Space, Komma, Strichpunkt) übergeben wird.
     *
     * @param list
     *            Die Liste in der String-Darstellung.
     * @param string
     *            Der zu überprüfenden String.
     * @return <code>true</code>, falls der String nicht null oder leer ist und Teil der übergebenen Liste
     *         ist, <code>false</code> ansonsten.
     */
    public boolean doesListContainString(String list, String string) {

        if (Strings.isNullOrEmpty(string)) {
            return false;
        }

        for (String stringFromList : Splitter.on(CharMatcher.anyOf(" ,;")).trimResults().split(list)) {
            if (stringFromList.equals(string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Konvertiert eine Liste in String-Darstellungen in eine Liste. Die String-Darstellung muss dabei ein
     * Separator-Zeichen (Space, Komma, Strichpunkt) enthalten.
     *
     * @param list
     *            Die Liste in der String-Darstellung.
     * @return Die Liste.
     */
    public List<String> convertToList(String list) {

        if (Strings.isNullOrEmpty(list)) {
            return new ArrayList<String>();
        } else {
            return Lists.newArrayList(Splitter.on(CharMatcher.anyOf(" ,;")).trimResults().split(list));
        }
    }

    /**
     * Gibt den Propertywert zu einer Property eines Objekts zurück.
     *
     * @param object
     *            Das Objekt.
     * @param property
     *            Die Property.
     *
     * @return Der Propertywert.
     *
     * @throws IllegalAccessException
     *             Wenn der Zugriff auf die Property nicht möglich ist.
     * @throws InvocationTargetException
     *             Wenn der Zugriff auf die Property nicht möglich ist.
     * @throws NoSuchMethodException
     *             Wenn der Zugriff auf die Property nicht möglich ist.
     */
    public Object getPropertyValueForObject(Object object, String property) throws IllegalAccessException,
        InvocationTargetException, NoSuchMethodException {
        return PropertyUtils.getProperty(object, property);
    }

    /**
     * Sucht Attribute in Parent UIComponents.
     *
     * @param attribute
     *            Das Attribut.
     * @param container
     *            Der Container.
     *
     * @return Der Wert des Attributes von einem Vater-Container oder <code>null</code>, wenn das Attribut in
     *         keinem Container existiert.
     */
    public Object searchAttributeInParents(String attribute, UIComponent container) {

        if (container == null) {
            return null;
        } else {
            if (container.getAttributes().containsKey(attribute)) {
                return container.getAttributes().get(attribute);
            } else {
                return searchAttributeInParents(attribute, container.getParent());
            }
        }

    }

    public String findClientId(String id) {
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        List<String> result = new ArrayList<String>();

        for (String part : convertToList(id)) {
            result.add(searchClientIdInChildren(viewRoot, part));
        }

        return Joiner.on(" ").join(result);

    }

    /**
     * Sucht nach der Client-ID eines Parents, welche Teile der übergebenen Id als Client-ID enthält.
     *
     * @param container
     *            Der Container.
     * @param idPart
     *            Der Teil der ID.
     *
     * @return Die ID des Parents oder null.
     */
    public String searchClientIdInParents(UIComponent container, String idPart) {

        if (container == null) {
            return null;
        } else {
            if (container.getClientId().contains(idPart)) {
                return container.getClientId();
            } else {
                return searchClientIdInParents(container.getParent(), idPart);
            }
        }

    }

    /**
     * Sucht zunächst nach dem Parent und dann einem bestimmten Kind. Falls die Client ID ein "@" oder ":"
     * beinhaltet, wird keine Auflösung durchgeführt.
     *
     * @param container
     *            Der Container.
     * @param idPartChild
     *            Der Teil der Client ID des Kinds. Hier kann auch eine JSF Liste übergeben werden.
     * @param idPartParent
     *            Der Teil der Client ID des Parents.
     * @return Die ID des Kinds oder null.
     */
    public String searchClientIdFromFirstChildInParents(UIComponent container, String idPartChild,
        String idPartParent) {

        List<String> result = new ArrayList<String>();

        for (String part : convertToList(idPartChild)) {

            if (part.contains("@")) {
                result.add(part);
                continue;
            }

            if (part.contains("uiroot:")) {
                result.add(findClientId(part.replace("uiroot:", "")));
                continue;
            }

            if (part.contains(":")) {
                result.add(part.substring(1, part.length()));
                continue;
            }

            if (container == null) {
                return null;
            } else {
                if (container.getClientId().contains(part)) {
                    result.add(container.getClientId());
                    continue;
                } else {
                    UIComponent parentComponent =
                        searchClientComponentInParents(container.getParent(), idPartParent);
                    result.add(searchClientIdInChildren(parentComponent, part));
                    continue;
                }
            }

        }

        return Joiner.on(" ").join(result);
    }

    /**
     * Sucht nach dem Parent (UI-Component), welcher mit der übergebenen Client ID endet.
     * @param container
     *            Der Container.
     * @param idPart
     *            Der letzte Teil der Parent Client ID.
     *
     * @return Die UI-Component des Parents oder null.
     */
    private UIComponent searchClientComponentInParents(UIComponent container, String idPart) {

        if (container == null) {
            return null;
        } else {
            if (container.getClientId().endsWith(idPart)) {
                return container;
            } else {
                return searchClientComponentInParents(container.getParent(), idPart);
            }
        }

    }

    /**
     * Sucht ein Kindelement anhand einer ID.
     * @param parentComponent
     *            der Parent
     * @param idPartChild
     *            die ID des Kinds.
     * @return Die ID des Kinds oder null.
     */
    public String searchClientIdInChildren(UIComponent parentComponent, String idPartChild) {

        if (parentComponent == null) {
            return null;
        } else if (parentComponent.getClientId().contains(idPartChild)) {
            return parentComponent.getClientId();
        } else {

            Iterator<UIComponent> it = parentComponent.getFacetsAndChildren();
            while (it.hasNext()) {
                UIComponent uiComponent = it.next();

                if (uiComponent.getClientId().contains(idPartChild)) {
                    return uiComponent.getClientId();
                }
            }

            it = parentComponent.getFacetsAndChildren();
            while (it.hasNext()) {
                UIComponent uiComponent = it.next();

                String clientId = searchClientIdInChildren(uiComponent, idPartChild);
                if (clientId != null) {
                    return clientId;
                }
            }

            return null;
        }
    }

}
