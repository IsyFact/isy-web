package de.bund.bva.isyfact.common.web.exception.common;

import de.bund.bva.isyfact.exception.FehlertextProvider;

/**
 * Anhand dieses Mappers werden Ausnahme-IDs für die Fehler hinterlegt, welche nicht in der Anwendung speziell
 * erzeugt werden. Für Isy-Exceptions dürfen keine Ausnahme-ID hinterlegt werden, weil sie bereits über eine
 * Ausnahme-ID verfügen und von der WebflowExceptionHandler ohne weiteres behandelt werden können.
 * <p>
 * Die zu den Ausnahme-IDs gehörenden Fehlertexte dürfen nicht parameterisiert sein.
 * <p>
 * Beispiele für die Fehler, welche nicht in der Anwendung speziell erzeugt werden: bestimmte Datenbankfehler,
 * Fehler durch Programmierfehler (NullPointerException,…).
 * <p>
 * <b>Als Fallback für undifferenzierte Fehler muss eine allgemeine Fehler-ID hinterlegt werden.</b>
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface AusnahmeIdMapper {

    /**
     * Liefert eine Ausnahme-ID für eine Exception, welche nicht in der Anwendung speziell erzeugt wird (z.b.
     * DataAccessException, TransactionException).
     *
     * @param throwable
     *            Die Exception.
     *
     * @return eine Ausnahme-ID für eine Exception, welche nicht in der Anwendung speziell erzeugt wird. Der
     *         Ruckgabewert ist <code>null</code>, wenn es sich um einen unvorhergesehenen Fehler handelt.
     */
    public String getAusnahmeId(Throwable throwable);

    /**
     * Gibt die AusnahmeId zurück, die verwendet werden soll, wenn ein unvorhergesehener Fehler aufgetreten
     * ist.
     * @return die AusnahmeId zurück, die verwendet werden soll, wenn ein unvorhergesehener Fehler aufgetreten
     *         ist
     */
    public String getFallbackAusnahmeId();

    /**
     * Liefert einen {@link FehlertextProvider} zurück, mit dem die Anwendung spezifische Fehlertexte laden
     * kann.
     *
     * @return einen {@link FehlertextProvider} zurück, mit dem die Anwendung spezifische Fehlertexte laden
     *         kann.
     */
    public FehlertextProvider getFehlertextProvider();

}
