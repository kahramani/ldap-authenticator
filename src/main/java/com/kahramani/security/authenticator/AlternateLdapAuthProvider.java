package com.kahramani.security.authenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Created by kahramani on 11/16/2016.
 */
@Component
public class AlternateLdapAuthProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(AlternateLdapAuthProvider.class);

    /**
     * to authenticate user with his/her credentials via ldap server.
     * In practice, this method does not provide any ldap features (search, roles etc) but login.
     * So there is no need to use a ldap service account credentials to run this method.
     * @param authentication holds credentials
     * @return authentication an Authentication which provides access permission
     * @throws AuthenticationException if authentication exception occurs
     */
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Authentication token = null;
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(username == null || "".equals(username.trim())) {
            logger.debug("username is null or empty authentication cannot be verified");
            return token;
        } else if(password == null || "".equals(password.trim())) {
            logger.debug("password is null or empty authentication cannot be verified");
            return token;
        }

        String URI = SecurityConfiguration.URL + SecurityConfiguration.SEARCH_BASE;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("com.sun.jndi.ldap.connect.timeout", SecurityConfiguration.SEARCH_TIMEOUT);
        env.put(Context.PROVIDER_URL, URI);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        // if ldap server use ssl, certs must be deployed keystore. Otherwise, ssl hand-shaking problem occurs.
        if(URI.toUpperCase(Locale.US).startsWith("LDAPS://")) {
            env.put(Context.SECURITY_PROTOCOL, "ssl");
        }

        try {
            new InitialDirContext(env);
            logger.info("LDAP directory context initialized for " + username);
            ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            token = new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(),
                    grantedAuthorities);
        } catch(NamingException e) {
            logger.error("Failed to initialize ldap directory context for " + username, e);
        }

        return token;
    }

    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
