package de.bund.bva.isyfact.common.web.jsf.components.editablerow;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.exception.IsyFactTechnicalRuntimeException;
import de.bund.bva.isyfact.common.web.jsf.components.datatable.DataTableItem;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;

import java.util.List;

/**
 * Der Controller für die JSF-EditableRow-Komponente (<code>isy:editableRow</code>).
 * Dieser Controller bietet Methoden um eine an die Komponente uebergebene Werteliste zu pruefen und bearbeiten.
 * Dafuer werden die Methoden "liefereNichtLeereWerteliste", "hinzufuegenReihe" und "loescheReihe" bereitgestellt.
 *
 * Die Werteliste und das Objekt müssen vorher initialisiert sein, ansonsten wird eine technische
 * Fehlermeldung geworfen.
 *
 * @param <E> die konkrete Datenklasse - muss von DataTableItem abgeleitet sein
 */
public class EditableRowController<E extends DataTableItem> implements GuiController {

    /**
     * Prueft die an die EditableRow uebergebene Werteliste bei jedem Aufbau der EditableRow.
     * Ist die Liste leer, so wird die Liste initial mit einem Objekt befuellt.
     * Ein leeres Objekte muss vorher von der aufrufenden Stelle definiert worden sein
     * und an die EditableRow mittels des Attributs "objekt" uebergeben worden sein.
     *
     * Ist die Liste nicht initialisiert (also null), so wird ein technischer Fehler geworfen.
     *
     * @param werteliste die Werteliste, welche an die ER gegeben wird
     * @param objekt das neu hinzuzufuegende Objekt
     * @return die Werteliste, nachdem sie u.U. angepasst wurde
     */
    public List<E> liefereNichtLeereWerteliste(List<E> werteliste, E objekt) {

        if (werteliste == null) {
            throw new IsyFactTechnicalRuntimeException(FehlerSchluessel.ALLGEMEINER_TECHNISCHER_FEHLER_MIT_PARAMETER,
                "Die übergebene Werteliste ist nicht initialisiert. Bitte initialisieren Sie die Liste bevor diese an die EditableRow-Komponente übergeben wird.");
        }

        if (objekt == null) {
            throw new IsyFactTechnicalRuntimeException(FehlerSchluessel.ALLGEMEINER_TECHNISCHER_FEHLER_MIT_PARAMETER,
                "Das übergebene Objekt ist nicht initialisiert. Bitte initialisieren Sie das Objekt bevor dieses an die EditableRow-Komponente übergeben wird.");
        }

        if(werteliste.isEmpty()) {
            werteliste.add(objekt);
        }

        return werteliste;
    }

    /**
     * Fuegt einen neuen Eintrag der Liste hinzu.
     *
     * Je nachdem welches Plus-Zeichen betaetigt wurde, wird das neue Objekt in der Reihe unter dem
     * gedrueckten Plus-Zeichen hinzugefuegt.
     *
     * @param werteliste die Werteliste, welche verwaltet wird
     * @param ausgewaehlterEintrag der aktuell ausgewaehlte Eintrag der Zeile, in der der Plus-Button der
     *      editableRow gedrueckt wurde.
     * @param objekt das neu hinzuzufuegende Objekt
     */
    public void hinzufuegenReihe(List<E> werteliste,
        E ausgewaehlterEintrag, E objekt) {

        int listenposition;

        if(!werteliste.isEmpty()) {

            listenposition = werteliste.indexOf(ausgewaehlterEintrag);
            werteliste.add(++listenposition, objekt);
        } else {

            werteliste.add(objekt);
        }
    }

    /**
     * Loescht ein ausgewaehltes Objekt aus der Liste.
     *
     * @param werteliste die Werteliste, welche verwaltet wird
     * @param ausgewaehlterEintrag der aktuell ausgewaehlte Eintrag in der editableRow
     */
    public void loescheReihe(List<E> werteliste, E ausgewaehlterEintrag) {

        werteliste.remove(ausgewaehlterEintrag);
    }

}
