package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Verwaltet der Zustand der Detailansicht, bzw. für welche Tabelleneinträge diese aktiv ist.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTableDetailViewModel implements Serializable {

    /** Die Serial-Version UID. */
    private static final long serialVersionUID = 1L;

    /** Die IDs der Items für welches die Detailansicht aktiv ist. */
    private Set<Long> detailViewItems = new HashSet<Long>();

    @SuppressWarnings("javadoc")
    public Set<Long> getDetailViewItems() {

        return detailViewItems;
    }

    /**
     * Prüft ob die Detailansicht eines Tabelleneintrags angezeigt werden soll.
     *
     * @param itemId Die ID des zu überprüfenden Tabelleneintrags.
     * @return <code>true</code>, wenn die Detailansicht angezeigt werden soll, <code>false</code> ansonsten.
     */
    public boolean isDetailView(long itemId) {

        return (getDetailViewItems() != null) && getDetailViewItems().contains(itemId);
    }

    @SuppressWarnings("javadoc")
    public void setDetailViewItems(Set<Long> detailItems) {

        this.detailViewItems = detailItems;
    }
}
