package de.bund.bva.isyfact.common.web.servlet.requesthandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.ListpickerGuiItem;
import de.bund.bva.isyfact.common.web.jsf.components.listpicker.ListpickerGuiKonfiguration;

/**
 * Servlet das Listpicker mit Informationen befüllt. <br/>
 * Um ein Listpicker als Servlet nutzen zu können, muss folgendes getan werden:
 * <ul>
 * <li>Eine konkrete Implementierung dieser Klasse bereitstellen.</li>
 * <li>Die Implementierung als Bean zur Verfügung stellen, z.B. folgendermaßen: <br/>
 *    <pre>
 *    &#064;Bean {@code
 *    BeispielListpickerProviderRequestHandler beispielListpickerProviderRequestHandler() {
 *       return new BeispielListpickerProviderRequestHandler();
 *    }
 *    }</pre>
 *    (kann auch über Annotation der Klasse als Component geregelt werden)
 *    Spring benötigt im nächsten Schritt den Namen dieser Bean.

 * </li>
 * <li> HttpRequestHandlerServlet registrieren.
 * Der Name der obigen Bean muss angegeben werden, sowie der Pfad unter dem das Servlet erreichbar sein soll.
 *    <pre>
 *    &#064;Bean {@code
 *    public ServletRegistrationBean<HttpRequestHandlerServlet> listPickerServletBean() {
 *         ServletRegistrationBean<HttpRequestHandlerServlet> bean = new ServletRegistrationBean<>(
 *             //Pfad für das Servlet
 *             new HttpRequestHandlerServlet(), "/app/beispiellistpicker"
 *         );
 *         // hier wird der Name der obigen Bean angegeben!
 *         bean.setName("beispielListpickerProviderRequestHandler");
 *         bean.setLoadOnStartup(1);
 *         return bean;
 *     }
 *    }</pre>
 * </li>
 * </ul>
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class AbstractListpickerProviderRequestHandler implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Für jede Implementierung wird der Parameter "filter" benötigt.
        String filter = getParameter(request, "filter");

        // Etwaige weitere Filter-Parameter setzen.
        List<String> weitereParameter = getWeitereFilterParameter();
        Map<String, String> weitereFilter = new HashMap<>();
        if (weitereParameter != null && !weitereParameter.isEmpty()) {
            for (String param : weitereParameter) {
                weitereFilter.put(param, getParameter(request, param));
            }
        }
        // ListpickerGuiItems von Implementierung beziehen.
        List<ListpickerGuiItem> guiItems = erzeugeGuiItems(filter, weitereFilter);

        // Ggf. begrenzen.
        boolean begrenzt = false;
        int maxElemente = getMaxElemente();
        if (guiItems.size() > maxElemente && maxElemente > 0) {
            guiItems = Lists.newArrayList(guiItems.subList(0, maxElemente).iterator());
            begrenzt = true;
        }

        String jsonString =
            wandleInJson(new ListpickerGuiKonfiguration(guiItems, getMessageBegrenzung(), begrenzt));
        response.getWriter().write(jsonString);

    }

    /**
     * Erzeugt die Liste von {@link ListpickerGuiItem}s anhand des übergebenen Filters und ggf. der weiteren
     * Filterkriterien, falls diese von der konkreten Implementierung benötigt werden. Der Filterwert wird als
     * obligatorisch vorausgesetzt, weil jeder Listpicker mit Filter-Feld mindestens anhand dieses Wertes
     * filtern wird. Die weiteren Kriterien sind optional. Jede Implementierung muss selber wissen, von
     * welchen Kriterien die Filterung insgesamt abhängt.
     * @param filterWert
     *            Der Wert des Filterfeldes, der vom Anwender eingeben wurde.
     * @param weitereFilterKriterien
     *            Optional: Eine Zuordnung von weiteren Filter-Parametern und deren jeweiligem Wert
     *            (extrahiert aus der URL in {@link #handleRequest(HttpServletRequest, HttpServletResponse)}).
     *            Dies kann auch {@code null} sein, falls die konkrete Implementierung diese nicht benötigt.
     *            Siehe {@link #getWeitereFilterParameter()}.
     * @return Die gefilterte Liste von {@link ListpickerGuiItem}s. Die Begrenzung (falls gewünscht; siehe
     *         {@link #getMaxElemente()}) wird von der abstrakten Oberklasse vorgenommen und ist somit von der
     *         konkreten Implementierung NICHT zu leisten.
     */
    public abstract List<ListpickerGuiItem> erzeugeGuiItems(String filterWert,
        Map<String, String> weitereFilterKriterien);

    /**
     * Liefert eine Liste von weiteren Filter-Parametern, die der URL entnommen werden sollen. Falls keine
     * weiteren Filter-Parameter benötigt werden, kann einfach {@code null} zurückgegeben werden.
     * @return Die weiteren Filter-Parameter oder {@code null}, falls keine benötigt werden.
     */
    public abstract List<String> getWeitereFilterParameter();

    /**
     * Legt fest, wie viele {@link ListpickerGuiItem}s maximal gerendert werden sollen, bevor der Hinweis
     * angezeigt wird (siehe {@link #getMessageBegrenzung()}). Falls keine Begrenzung gewünscht ist, soll -1
     * zurückgegeben werden.
     * @return Die maximale Anzahl.
     */
    public abstract int getMaxElemente();

    /**
     * Liefert den Text, der angezeigt werden soll, falls {@link #erzeugeGuiItems(String, Map)} mehr Items
     * liefert, als {@link #getMaxElemente()} vorschreibt.
     * @return Der Text, der im Listpicker erscheint.
     */
    public abstract String getMessageBegrenzung();

    /**
     * Wandelt ein Object in einen String um.
     *
     * @param wert
     *            Das Object, das in einen String gewandelt werden soll.
     *
     * @return Den Stringwert des Objects.
     */
    private static String ermittleStringWert(Object wert) {
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
    private String getParameter(HttpServletRequest request, String parameter) {
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
    private String wandleInJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
