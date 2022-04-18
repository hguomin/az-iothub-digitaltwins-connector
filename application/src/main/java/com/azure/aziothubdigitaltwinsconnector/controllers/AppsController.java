package com.azure.aziothubdigitaltwinsconnector.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/apps")
public class AppsController {
    @GetMapping()
    public String index() {
        return "apps/index";
    }

    @RequestMapping(value="/designer", method=RequestMethod.GET)
    public String designer() {
        return "apps/designer/index";
    }
}
