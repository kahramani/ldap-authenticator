package com.kahramani.security.authenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.HashMap;

/**
 * Created by kahramani on 11/15/2016.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    private static final boolean useSpringLdap = true;
    // TODO fill the followings
    protected static final String URL = "ldaps://my.domain.com:636/";
    protected static final String USER = ""; // if do not have a service account change useSpringLdap to false
    protected static final String PASSWORD = "";
    protected static final String CONNECT_TIMEOUT = "10000"; // in ms

    protected static final String SEARCH_BASE = "ou=UserOU,dc=DUMMY_1,dc=DUMMY_2,dc=DUMMY_3";
    protected static final String SEARCH_FILTER_SAM = "(&(objectClass=user)(sAMAccountName={0}))";
    protected static final String SEARCH_TIMEOUT = "10000"; // in ms
    protected static final boolean SEARCH_POOLED = false;
    protected static final String SEARCH_REFERRAL = "follow";

    @Autowired
    private AlternateLdapAuthProvider alternateLdapAuthProvider;

    /**
     * to configure authentication
     * @param auth auth builder object provided by spring security
     * @throws Exception if authentication configuration exception
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        // useSpringLdap should be false if there is no service account
        if(useSpringLdap) {
            auth.ldapAuthentication().contextSource(contextSource())
                    .userSearchFilter(SEARCH_FILTER_SAM);
        } else {
            auth.authenticationProvider(this.alternateLdapAuthProvider);
        }
    }

    /**
     * to set properties for ldap connection
     * @return an LdapContextSource object which contains ldap connection properties
     */
    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(URL);
        contextSource.setBase(SEARCH_BASE);
        contextSource.setUserDn(USER);
        contextSource.setPassword(PASSWORD);
        contextSource.setReferral(SEARCH_REFERRAL);
        contextSource.setPooled(SEARCH_POOLED);

        HashMap<String, Object> env = new HashMap<>();
        env.put("com.sun.jndi.ldap.connect.timeout", CONNECT_TIMEOUT);
        env.put("com.sun.jndi.ldap.read.timeout", SEARCH_TIMEOUT);
        contextSource.setBaseEnvironmentProperties(env);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    /**
     * to exclude some url patterns
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/ldap-authenticator/checkApp"); // it is allowed to write more patterns
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and().httpBasic()
                .and().csrf().disable();
    }
}
