package com.example.mvc;

import com.example.mvc.controller.Controller;
import com.example.mvc.controller.HomeController;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingHandlerMapping {
    // key: url path 값, value: 실행할 controller
    private Map<String, Controller> mappings = new HashMap<>();

    void init(){
        mappings.put("/", new HomeController());
    }

    public Controller findHandler(String uriPath) {
        return mappings.get(uriPath);
    }
}
