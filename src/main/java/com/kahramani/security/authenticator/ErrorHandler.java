package com.kahramani.security.authenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;

/**
 * Created by kahramani on 11/16/2016.
 */
@Controller
public class ErrorHandler implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
