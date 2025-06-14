package com.interview.employee_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle the home page and welcome screen
 */
@Controller
public class HomeController {

    /**
     * Handles the root URL and returns the welcome page
     * @return the name of the welcome page template
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
