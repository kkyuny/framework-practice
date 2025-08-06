package com.example.mvc.controller;

import com.example.mvc.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserListController implements Controller {

    @Override
    public String handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", UserRepository.findAll());
        return "/user/list";
    }
}
