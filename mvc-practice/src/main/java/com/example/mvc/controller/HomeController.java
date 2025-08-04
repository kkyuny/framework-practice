package com.example.mvc.controller;

import com.example.annotation.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController implements com.example.mvc.controller.Controller {

    @Override
    public String handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return "home";
    }
}
