package com.kahramani.security.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kahramani on 11/15/2016.
 */
@RestController
public class AuthChecker {

    @RequestMapping("/maintenance/testAuth")
    public String checkAuth() {
        return "Successful Authentication!";
    }

}
