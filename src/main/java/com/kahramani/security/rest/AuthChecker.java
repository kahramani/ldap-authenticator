package com.kahramani.security.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kahramani on 11/15/2016.
 */
@RestController
public class AuthChecker {

    private static final Logger logger = LoggerFactory.getLogger(AuthChecker.class);

    @RequestMapping("/ldap-authenticator/checkApp")
    public String checkApp() {
        return "App is running!";
    }

    @RequestMapping("/ldap-authenticator/checkAuth")
    public String checkAuth() {
        return "Successful Authentication!";
    }

}
