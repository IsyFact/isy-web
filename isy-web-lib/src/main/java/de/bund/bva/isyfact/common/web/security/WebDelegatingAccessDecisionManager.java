package de.bund.bva.isyfact.common.web.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import de.bund.bva.isyfact.security.core.Security;
import de.bund.bva.isyfact.security.core.Berechtigungsmanager;

@Deprecated
public class WebDelegatingAccessDecisionManager implements AccessDecisionManager {

    /**
     * Access to the security module.
     */
    private final Security security;

    public WebDelegatingAccessDecisionManager(Security security) {
        this.security = security;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
        InsufficientAuthenticationException {
        Berechtigungsmanager bm;
        try {
            bm = security.getBerechtigungsmanager();
        } catch (AuthenticationException e) {
            throw new InsufficientAuthenticationException("Berechtigungsmanager nicht verfuegbar", e);
        }

        for (Object obj : configAttributes) {
            ConfigAttribute attribute = (ConfigAttribute) obj;

            String gefordertesRecht = attribute.getAttribute();
            if (!bm.hatRecht(gefordertesRecht)) {
                throw new AccessDeniedException("Keine Berechtigung für: " + gefordertesRecht + " (Autorisierung fehlgeschlagen)");
            }
        }
    }
}

