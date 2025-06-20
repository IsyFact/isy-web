package de.bund.bva.isyfact.common.web.tempwebresource;

/**
 * Komponentenschnittstelle zum Zugriff auf temporäre Web-Ressourcen.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface TempWebResourceZugriff {
    /**
     * Speichert eine temporäre Web-Ressource.
     *
     * @param benutzerkennung
     *            die Kennung des aktuellen Benutzers
     * @param bhknz
     *            das Behördenkennzeichen des aktuellen Benutzers
     * @param ressource
     *            die Binärdaten der Ressource
     * @param kennzeichen
     *            ein Kennzeichen, dass z.B. den Typ der Ressource beschreibt (darf <code>null</code> sein)
     * @param mimeType
     *            der MIME-Type der Ressource
     * @return die gespeicherte TempWebResource
     */
    // XXX benutzerkennung und bhknz sollten nicht explizit übergeben werden, sondern über den
    // AufrufKontextVerwalter geladen werden.
    public TempWebResourceRo speichereTempWebResource(String benutzerkennung, String bhknz, byte[] ressource,
        String kennzeichen, String mimeType);

    /**
     * Speichert eine temporäre Web-Ressource.
     *
     * @param benutzerkennung
     *            die Kennung des aktuellen Benutzers
     * @param bhknz
     *            das Behördenkennzeichen des aktuellen Benutzers
     * @param ressource
     *            die Binärdaten der Ressource
     * @param kennzeichen
     *            ein Kennzeichen, dass z.B. den Typ der Ressource beschreibt (darf <code>null</code> sein)
     * @param mimeType
     *            der MIME-Type der Ressource
     * @param dateiname
     *            der Dateiname der Ressource
     * @return die gespeicherte TempWebResource
     */
    // XXX benutzerkennung und bhknz sollten nicht explizit übergeben werden, sondern über den
    // AufrufKontextVerwalter geladen werden.
    public TempWebResourceRo speichereTempWebResource(String benutzerkennung, String bhknz, byte[] ressource,
        String kennzeichen, String mimeType, String dateiname);

    /**
     * Lädt die temporäre Web-Ressource mit der gegebenen ID.
     *
     * @param id
     *            die ID
     * @param benutzerkennung
     *            die Kennung des aktuellen Benutzers
     * @param bhknz
     *            das Behördenkennzeichen des aktuellen Benutzers
     * @return die gelesene TempWebResource
     * @throws BerechtigungException
     *             falls der Nutzer zu dieser Aktion nicht berechtigt ist.
     */
    // XXX benutzerkennung und bhknz sollten nicht explizit übergeben werden, sondern über den
    // AufrufKontextVerwalter geladen werden.
    public TempWebResourceRo ladeTempWebResource(long id, String benutzerkennung, String bhknz);

    /**
     * Löscht die temporäre Web-Ressource mit der gegebenen ID.
     *
     * @param id
     *            die ID
     * @param benutzerkennung
     *            die Kennung des aktuellen Benutzers
     * @param bhknz
     *            das Behördenkennzeichen des aktuellen Benutzers
     * @throws BerechtigungException
     *             falls der Nutzer zu dieser Aktion nicht berechtigt ist.
     */
    // XXX benutzerkennung und bhknz sollten nicht explizit übergeben werden, sondern über den
    // AufrufKontextVerwalter geladen werden.
    public void loescheTempWebResource(long id, String benutzerkennung, String bhknz);

    /**
     * Löscht alle temporären Web-Ressourcen, die den Kriterien entsprechen.
     *
     * @param benutzerkennung
     *            die Kennung des aktuellen Benutzers
     * @param bhknz
     *            das Behördenkennzeichen des aktuellen Benutzers
     * @param kennzeichen
     *            das Kennzeichen der zu löschenden Ressourcen
     */
    // XXX benutzerkennung und bhknz sollten nicht explizit übergeben werden, sondern über den
    // AufrufKontextVerwalter geladen werden.
    public void loescheTempWebResources(String benutzerkennung, String bhknz, String kennzeichen);
}
