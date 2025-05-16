package de.bund.bva.isyfact.common.web.layout;

import java.io.Serializable;

/**
 * Model für ein LinksnavigationelementModel.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class LinksnavigationelementModel implements Serializable {

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Der Text der angezeigt wird.
     */
    private String anzuzeigenderText;

    /**
     * Der dahinterliegende Link.
     */
    private String link;

    /**
     * Ist dieser Link gerade aktiv?
     */
    private boolean active;

    public String getAnzuzeigenderText() {
        return anzuzeigenderText;
    }

    public void setAnzuzeigenderText(String anzuzeigenderText) {
        this.anzuzeigenderText = anzuzeigenderText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
