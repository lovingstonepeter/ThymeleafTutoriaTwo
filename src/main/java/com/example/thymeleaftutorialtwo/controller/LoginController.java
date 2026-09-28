package com.example.thymeleaftutorialtwo.controller;

// This annotation tells Spring that this class handles web requests
// and returns HTML views.
import org.springframework.stereotype.Controller;

// @GetMapping maps a GET request to a Java method.
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    /*
     * ================================================================
     * DISPLAY LOGIN PAGE
     * ================================================================
     *
     * When a user visits:
     *
     *     http://localhost:8080/login
     *
     * Spring will execute this method.
     *
     * The method returns:
     *
     *     "login"
     *
     * which tells Thymeleaf to look for:
     *
     *     src/main/resources/templates/login.html
     */
    @GetMapping("/login")
    public String login() {

        // Return the name of our Thymeleaf HTML template.
        return "login";
    }
}

