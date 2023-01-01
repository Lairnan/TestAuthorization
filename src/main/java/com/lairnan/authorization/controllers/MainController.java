package com.lairnan.authorization.controllers;

import com.lairnan.authorization.models.User;
import com.lairnan.authorization.repositories.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/")
    public String mainGet(HttpServletRequest request, Model model) {
        if(!isAuth(request.getSession())) return "authorization";
        Iterable<User> users = usersRepository.findAll();
        model.addAttribute("users", users);
        return "index";
    }

    @GetMapping("/registration")
    public String registrationGet(HttpServletRequest request, Model model) {
        return "registration";
    }

    @PostMapping(value = "/getusers")
    public String getUsersPost(Model model) {
        Iterable<User> users = usersRepository.findAll();
        model.addAttribute("users", users);
        return "index :: #listUsers";
    }

    @PostMapping(value = "/users/{id}/remove")
    public String removeUserPost(@PathVariable(value = "id") long id, Model model) {
        User user = usersRepository.findById(id).orElseThrow();
        usersRepository.delete(user);
        Iterable<User> users = usersRepository.findAll();
        model.addAttribute("users", users);
        return "index :: #listUsers";
    }

    @GetMapping(value = "/users/{id}/edit")
    public String editUserGet(@PathVariable(value = "id") long id, HttpServletRequest request, Model model) {
        if(!isAuth(request.getSession())) {
            return "authorization";
        }
        if(((User)request.getSession().getAttribute("user")).getAccess() < 2) throw new SecurityException("У вас нету доступа к данной странице");
        User user = usersRepository.findById(id).orElseThrow();
        model.addAttribute("surname", user.getSurname());
        model.addAttribute("name", user.getName());
        model.addAttribute("patronymic", user.getPatronymic());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("access", user.getAccess());
        return "editUser";
    }

    private boolean isAuth(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping(value = "/exit")
    public RedirectView exitGet(HttpServletRequest request) {
        request.getSession().invalidate();
        return new RedirectView("/");
    }

    @GetMapping(value = "/addUser")
    public String addUserGet(HttpServletRequest request, Model model) {
        if(((User)request.getSession().getAttribute("user")).getAccess() < 2) throw new SecurityException("У вас нету доступа к данной странице");
        return "addUser";
    }
}