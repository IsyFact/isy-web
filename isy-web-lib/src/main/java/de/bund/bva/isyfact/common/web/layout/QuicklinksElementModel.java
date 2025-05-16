package de.bund.bva.isyfact.common.web.layout;

import java.io.Serializable;

/**
 * Modell für ein Quicklinkselement.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class QuicklinksElementModel implements Serializable {

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die ID eines Quicklinks.
     */
    private String id;

    /**
     * Der Text der angezeigt wird.
     */
    private String anzuzeigenderText;

    /**
     * Der dahinterliegende Link.
     */
    private String link;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnzuzeigenderText() {
        return this.anzuzeigenderText;
    }

    public void setAnzuzeigenderText(String anzuzeigenderText) {
        this.anzuzeigenderText = anzuzeigenderText;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
