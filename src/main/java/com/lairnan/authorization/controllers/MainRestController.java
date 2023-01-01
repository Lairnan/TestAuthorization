package com.lairnan.authorization.controllers;

import com.lairnan.authorization.models.User;
import com.lairnan.authorization.repositories.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class MainRestController {
    @Autowired
    private UsersRepository usersRepository;

    @PostMapping(value = "/authorization")
    public Map<Object, Object> authorizationPost(@RequestParam(name = "email") String email,
                                                 @RequestParam(name = "password") String password,
                                                 HttpServletRequest request) {
        if(email.trim() == null || email.trim().length() == 0) throw new NullPointerException("Почта не может быть пустой");
        if(password.trim() == null || password.trim().length() == 0) throw new NullPointerException("Пароль не может быть пустым");
        List<User> users = usersRepository.findByEmail(email);
        if(users.size() < 1) throw new NullPointerException("Неверный email");
        if(!Objects.equals(users.get(0).getPassword(), password)) throw new NullPointerException("Неверный пароль");
        request.getSession().setAttribute("user", users.get(0));
        request.getSession().setAttribute("access", users.get(0).getAccess());
        Map<Object, Object> map = new HashMap<>();
        map.put("isAuth", true);
        return map;
    }

    @PostMapping(value = "/registration")
    public Map<Object, Object> registrationPost(@RequestParam(name = "surname") String surname,
                                                @RequestParam(name = "name") String name,
                                                @RequestParam(name = "patronymic") String patronymic,
                                                @RequestParam(name = "email") String email,
                                                @RequestParam(name = "password") String password,
                                                HttpServletRequest request) {
        checkNullValues(surname, name, email, password);
        List<User> users = usersRepository.findByEmail(email);
        if(users.size() > 0) throw new NullPointerException("Данная почта уже существует");
        User user = new User(surname, name, patronymic, email, password, 1);
        usersRepository.save(user);
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("access", user.getAccess());
        Map<Object, Object> map = new HashMap<>();
        map.put("isAuth", true);
        return map;
    }

    @PostMapping(value = "/saveUser/{id}")
    public Map<Object, Object> saveUserPost(@PathVariable(name = "id") Long id,
                                            @RequestParam(name = "surname") String surname,
                                            @RequestParam(name = "name") String name,
                                            @RequestParam(name = "patronymic") String patronymic,
                                            @RequestParam(name = "email") String email,
                                            @RequestParam(name = "password") String password,
                                            @RequestParam(name = "access") int access,
                                            HttpServletRequest request) {
        checkNullValues(surname, name, email, password);
        User user = usersRepository.findById(id).orElseThrow();
        List<User> users = usersRepository.findByEmail(email);
        if(users.size() > 0 && !Objects.equals(user.getEmail(), email)) throw new NullPointerException("Данная почта уже существует");
        user.setSurname(surname);
        user.setName(name);
        user.setPatronymic(patronymic);
        user.setEmail(email);
        user.setPassword(password);
        user.setAccess(access);
        usersRepository.save(user);
        request.getSession().setAttribute("user", user);
        Map<Object, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    private static void checkNullValues(String surname, String name, String email, String password) {
        if(surname == null || surname.trim().length() == 0) throw new NullPointerException("Фамилия не может быть пустой");
        if(name == null || name.trim().length() == 0) throw new NullPointerException("Имя не может быть пустой");
        if(email == null || email.trim().length() == 0) throw new NullPointerException("Почта не может быть пустой");
        if(password == null || password.trim().length() == 0) throw new NullPointerException("Пароль не может быть пустым");
    }

    @PostMapping(value = "/addUser")
    public Map<Object, Object> addUserPost(@RequestParam(name = "surname") String surname,
                                            @RequestParam(name = "name") String name,
                                            @RequestParam(name = "patronymic") String patronymic,
                                            @RequestParam(name = "email") String email,
                                            @RequestParam(name = "password") String password,
                                            @RequestParam(name = "access") int access,
                                            HttpServletRequest request) {
        checkNullValues(surname, name, email, password);
        List<User> users = usersRepository.findByEmail(email);
        if(users.size() > 0) throw new NullPointerException("Данная почта уже существует");
        User user = new User(surname, name, patronymic, email, password, access);
        usersRepository.save(user);
        Map<Object, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }
}
