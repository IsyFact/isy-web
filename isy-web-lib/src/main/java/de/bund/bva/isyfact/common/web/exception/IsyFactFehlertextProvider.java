package de.bund.bva.isyfact.common.web.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import de.bund.bva.isyfact.exception.FehlertextProvider;

/**
 * Diese Klasse stellt Methoden zum Auslesen von Fehlertexten auf Basis von AusnahmeIDs und Parametern zur
 * Verfügung.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class IsyFactFehlertextProvider implements FehlertextProvider {

    /**
     * Das ResourceBoundle mit den Fehlertexten.
     */
    public static final ResourceBundle FEHLERTEXT_BUNDLE = ResourceBundle.getBundle(
        "resources/nachrichten/isyweb_fehler", Locale.getDefault());

    /**
     * Holt den Fehlertext zum Schlüssel.
     * @param schluessel
     *            der Schlüssel
     * @return der Fehlertext
     */
    public static String getMessage(String schluessel) {
        return FEHLERTEXT_BUNDLE.getString(schluessel);
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage(String schluessel, String... parameter) {
        return MessageFormat.format(getMessage(schluessel), (Object[]) parameter);
    }

}
