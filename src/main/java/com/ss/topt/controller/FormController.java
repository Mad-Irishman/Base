package com.ss.topt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    @GetMapping("/form")
    public String showForm() {
        return "formForUrl"; // Имя HTML-шаблона без расширения
    }
}