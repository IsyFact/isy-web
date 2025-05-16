package de.bund.bva.isyfact.common.web.global;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import de.bund.bva.isyfact.exception.FehlertextProvider;

/**
 * Kapselt den Zugriff auf das Resources-Bundle mit den Maskentexten.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public final class NachrichtenProvider implements FehlertextProvider {

    /**
     * Das ResourceBoundle mit den Texten.
     */
    private static final ResourceBundle MESSAGES_BUNDLE = ResourceBundle.getBundle(
        "resources/nachrichten/isyweb_maskentexte", Locale.getDefault());

    /**
     * Liefert die Nachricht mit dem Schlüssel.
     * @param schluessel
     *            Nachrichten-Schlüssel.
     * @return Die Nachricht.
     */
    public String getMessage(String schluessel) {
        return MESSAGES_BUNDLE.getString(schluessel);
    }

    /**
     * Liefert die Nachricht mit dem Schlüssel. Darin werden die angegebenen Parameter ersetzt.
     * 
     * @param schluessel
     *            Nachrichten-Schlüssel.
     * @param parameter
     *            Parameter für die Nachricht.
     * @return Die Nachricht.
     */
    @Override
    public String getMessage(String schluessel, String... parameter) {
        return MessageFormat.format(getMessage(schluessel), (Object[]) parameter);
    }

}
