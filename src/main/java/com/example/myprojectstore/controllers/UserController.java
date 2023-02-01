package com.example.myprojectstore.controllers;


import com.example.myprojectstore.models.User;
import com.example.myprojectstore.repositories.UserRepository;
import com.example.myprojectstore.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping("/login")
    public String login() {
        return "login";
    }


@ModelAttribute("user")
    public User user(){
        return new User();

    }



   @RequestMapping(value = "/registration",method = RequestMethod.GET)
    public String registration(@ModelAttribute("userValid") User user) {
        return "registration";
    }


    @PostMapping("/registration")
    public String createUser(@ModelAttribute("userValid") @Valid User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (userService.createUser(user)) {
            return "redirect:/login";

        }
        return "registration";
    }



    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal){
        model.addAttribute("user",user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("products",user.getProducts());
        return "user-info";
    }



}
