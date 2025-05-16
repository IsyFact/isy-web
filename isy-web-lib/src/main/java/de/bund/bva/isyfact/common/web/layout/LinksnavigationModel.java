package de.bund.bva.isyfact.common.web.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model-Klasse fuer Maskenmodel vom Typ "Linksnavigation".
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class LinksnavigationModel implements Serializable {

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Liste an Navigationselementen für eine Seite.
     */
    private List<LinksnavigationelementModel> linksnavigationelemente;

    /**
     * Die Überschrift der Linksnavigation.
     */
    private String headline;

    /**
     * Fügt ein neues LinksnavigationelementModel hinzu.
     * @param linksnavigationelementModel
     *            Das neue LinksnavigationelementModel
     */
    public void addLinksnavigationelementModel(LinksnavigationelementModel linksnavigationelementModel) {
        if (linksnavigationelemente == null) {
            linksnavigationelemente = new ArrayList<LinksnavigationelementModel>();
        }
        linksnavigationelemente.add(linksnavigationelementModel);
    }

    public List<LinksnavigationelementModel> getLinksnavigationelemente() {
        return linksnavigationelemente;
    }

    public void setLinksnavigationelemente(List<LinksnavigationelementModel> linksnavigationelemente) {
        this.linksnavigationelemente = linksnavigationelemente;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

}
