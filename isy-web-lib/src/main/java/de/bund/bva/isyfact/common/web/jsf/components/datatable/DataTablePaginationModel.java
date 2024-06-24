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
package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;

/**
 * Das Modell des Paginators einer Tabelle.
 * <p>
 * Hier wird der Zustand der Paginierung verwaltet, z.B. aktuelle Seite und Anzahl der
 * Tabelleneinträge pro Seite und Anzahl der Seiten.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class DataTablePaginationModel implements Serializable {

    /** Der Paginator-Typ. */
    public enum PaginationType {
        /** Kein Paginator. */
        NONE,
        /** Normallen Paginator, mit Seiten-Angaben. */
        NORMAL,
        /** Mehr Anzeigen Knopf */
        SIMPLE;
    }

    private static final long serialVersionUID = 1L;

    /** Die aktuelle Seite, die Zählung startet bei 1, 0 ist nicht erlaubt. */
    private int currentPage = 1;

    /** Die letzte Seite, wird von Kontroller verwaltet. */
    private int pageCount = 0;

    /** Die Anzahl of Tabelleneinträge pro Seite. */
    private int pageSize = 20;

    /** Die Anzahl der Seiten Knöpfe im Paginator wird so berechnet: <code>(paginatorSize * 2 - 1)<code>. */
    private int paginatorSize = 3;

    /** Das Paginationstyp: Volle Paginierung, "Mehr Anzeigen" Knopf, keine Paginierung. */
    private PaginationType type = PaginationType.NORMAL;

    @SuppressWarnings("javadoc")
    public int getCurrentPage() {

        return this.currentPage;
    }

    @SuppressWarnings("javadoc")
    public int getPageCount() {

        return pageCount;
    }

    /**
     * Liefert die Nummer der erste Seite im Paginator.
     * <p>
     *
     * @return die Nummer der erste Seite im Paginator
     */
    public int getPageFrom() {

        return Math.max(getCurrentPage() - getPaginatorSize() + 1, 1);
    }

    @SuppressWarnings("javadoc")
    public int getPageSize() {

        return pageSize;
    }

    /**
     * Liefert die Nummer der letzte Seite im Paginator.
     * <p>
     *
     * @return die Nummer der letzte Seite im Paginator
     */
    public int getPageTo() {

        return Math.min(getCurrentPage() + getPaginatorSize() - 1, getPageCount());
    }

    @SuppressWarnings("javadoc")
    public int getPaginatorSize() {

        return paginatorSize;
    }

    /**
     * Liefert die Liste der Seitennummern die im Paginator explizit angezeigt werden sollen.
     *
     * @return die Liste der Seitennummern
     */
    public List<Integer> getShowPages() {

        return new AbstractList<Integer>() {

            @Override
            public Integer get(int index) {

                return getPageFrom() + index;
            }

            @Override
            public int size() {

                return getPageTo() - getPageFrom() + 1;
            }
        };
    }

    @SuppressWarnings("javadoc")
    public PaginationType getType() {

        return type;
    }

    @SuppressWarnings("javadoc")
    public boolean isFirstPageSelected() {

        return getCurrentPage() == 1;
    }

    @SuppressWarnings("javadoc")
    public boolean isLastPageSelected() {

        return getCurrentPage() == getPageCount();
    }

    @SuppressWarnings("javadoc")
    public void setCurrentPage(int currentPage) {

        this.currentPage = currentPage;
    }

    @SuppressWarnings("javadoc")
    public void setPageCount(int pageCount) {

        this.pageCount = pageCount;
    }

    @SuppressWarnings("javadoc")
    public void setPageSize(int pageSize) {

        this.pageSize = pageSize;
    }

    @SuppressWarnings("javadoc")
    public void setPaginatorSize(int paginatorSize) {

        this.paginatorSize = paginatorSize;
    }

    @SuppressWarnings("javadoc")
    public void setType(PaginationType type) {

        this.type = type;
    }
}
