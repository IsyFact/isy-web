package de.bund.bva.isyfact.common.web.tempwebresource.persistence;

/**
 * Diese Klasse enthaelt die Namen aller Named Queries in der Datei <tt>NamedQueries.hbm.xml</tt>. Zugriff auf
 * die Queries dieser Datei sollte nur ueber Konstanten dieser Klasse geschehen, damit ihre Verwendung
 * nachverfolgt werden kann.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public final class NamedQueries {

    /**
     * Keine Instanziierung erlaubt.
     */
    private NamedQueries() {
    }

    /** Sucht alle TempWebResources anhand von Benutzerkennung, Behördenkennzeichen und Kennzeichen. */
    public static final String TEMP_WEB_RESOURCE_SUCHEN = "tempwebresource.suchen";

    /** Löscht alle TempWebResources, die älter als das angegebene Datum sind. */
    public static final String TEMP_WEB_RESOURCE_LOESCHEN_NACH_DATUM = "tempwebresource.loeschen.datum";

    /** Löscht alle TempWebResources mit den gesuchten Kriterien. */
    public static final String TEMP_WEB_RESOURCE_LOESCHEN_NACH_KRITERIEN =
        "tempwebresource.loeschen.kriterien";

}
