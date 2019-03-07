package de.bund.bva.isyfact.common.web.servlet.requesthandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.HttpRequestHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.ListpickerGuiKonfiguration;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;

/**
 * Servlet das Listpicker mit Informationen befüllt. </br>
 * Um ein Listpicker als Servlet nutzen zu können, muss folgendes getan werden:
 * <ul>
 * <li>ein Requesthandler der von AbstractListpickerProviderRequestHandler erbt muss die handleRequest methode
 * implementieren.</li>
 * <li>die response muss ein Json von {@link ListpickerGuiKonfiguration} sein. Benutze hierzu die Methode
 * wandleInJson</li>
 * <li>in der web.xml ein Servlet mit Servlet-Mapping für den entsprechenden Requesthandler gesetzt werden.
 * (Dafür muss es eine Bean geben)</li>
 * </ul>
 */
public abstract class AbstractListpickerProviderRequestHandler implements HttpRequestHandler {

    /**
     * Zugriff auf die Konfiguration. Wird für {@link #getMaxElementeAusKonfiguration(String)} gebraucht. Es
     * wird davon ausgegangen, dass jede Implementierung diese Methode benutzen wird und daher ist die
     * Konfiguration bereits in der abstrakten Oberklasse enthalten.
     */
    protected Konfiguration konfiguration;

    /**
     * Wandelt ein Object in einen String um.
     *
     * @param wert
     *            Das Object, das in einen String gewandelt werden soll.
     *
     * @return Den Stringwert des Objects.
     */
    protected static String ermittleStringWert(Object wert) {
        if (wert instanceof String[]) {
            return ((String[]) wert)[0];
        }
        return (String) wert;
    }

    /**
     * Ermittelt den Wert eines Parameter aus einem Request und gibt ihn als String zurück.
     *
     * @param request
     *            Der abgesendete Request.
     *
     * @param parameter
     *            Der Parameter zu dem der Wert ermittelt werden soll.
     *
     * @return Den Stringwert des Parameters.
     */
    public String getParameter(HttpServletRequest request, String parameter) {
        if (request.getParameterMap().get(parameter) != null) {
            String item = ermittleStringWert(request.getParameterMap().get(parameter));
            byte[] bytes = item.getBytes(StandardCharsets.ISO_8859_1);
            item = new String(bytes, StandardCharsets.UTF_8);
            return item;
        }
        return "";
    }

    /**
     * Wandelt ein Object in Json um.
     *
     * @param object
     *            Das Object das umgewandelt werden soll.
     *
     * @return Json String des Objects.
     *
     * @throws IOException
     *             Falls der {@link ObjectMapper} nicht geladen werden kann.
     */
    public String wandleInJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * Diese Methode dient dazu aus der Konfiguration die maximale Anzahl an Elementen zu holen, die im
     * Listpicker angezeigt werden. Der Defaultwert ist -1 und bedeutet es gibt keine Beschränkung an
     * Elementen.
     *
     * @param konfigurationsSchluessel
     *            Der Schlüssel der Konfiguration
     * @return Den Wert der in der Konfiguration konfiguriert ist, oder -1 falls nicht vorhanden.
     */
    public int getMaxElementeAusKonfiguration(String konfigurationsSchluessel) {
        return this.konfiguration.getAsInteger(konfigurationsSchluessel, -1);
    }

    @Required
    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

}
