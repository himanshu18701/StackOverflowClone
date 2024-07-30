package com.himanshu.stackoverflow.controller;

import com.himanshu.stackoverflow.entity.User;
import com.himanshu.stackoverflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login-page";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("newUser", new User());
        return "signup-page";
    }

    @PostMapping("/registerForm")
    public String register(@ModelAttribute("newUser") User user, Model model) {
        User newUser = userService.registerUser(user);
        if (newUser == null) {
            model.addAttribute("error", "User already exists");
            return "signup-page";
        }
        return "redirect:/users/login";
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping({"/{id}"})
    public String getUserById(@PathVariable("id") int userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/search")
    public String search(@RequestParam("userName") String userName, Model model) {
        List<User> users = userService.findUsersByName(userName);
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/profile")
    public String getProfile (Model model) {
        User user=userService.getLoggedUser();
        model.addAttribute("user", user);
        model.addAttribute("answers",user.getAnswers());
        model.addAttribute("questions",user.getQuestions());
        return "profile";
    }
}
