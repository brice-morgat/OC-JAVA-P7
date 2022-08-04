package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.IUserService;
import com.nnk.springboot.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        logger.debug("User List page");
        model.addAttribute("user", userService.getAllUser());
        logger.info("user's List getted");
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUserForm(User user) {
        logger.debug("Show User Form");
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        logger.debug("Add a User");
        if (!result.hasErrors()) {
            try {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
                userService.saveUser(user);
                logger.info("User has been saved");
                return "redirect:/user/list";
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
                logger.error("Error : " + e.getMessage());
                return "error";
            }
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.debug("Show User's Form");
        try {
            model.addAttribute("user", userService.getUserById(id));
            logger.info("User has been getting");
            return "user/update";
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "redirect:/user/list";
        }
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                               BindingResult result, Model model) {
        logger.debug("Update User");
        if (!result.hasErrors()) {
            try {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
                user.setId(id);
                userService.saveUser(user);
                logger.info("User has been updated");
                return "redirect:/user/list";
            } catch (Exception e) {
                logger.error("Error : " + e.getMessage());
                model.addAttribute("errorMsg", e.getMessage());
                return "error";
            }
        }
        return "user/update";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        logger.debug("Delete user by id");
        try {
            User user = userService.getUserById(id);
            userService.deleteUser(user);
            logger.info("User has been deleted");
            return "redirect:/user/list";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
//    @Autowired
//    private IUserService userService;
//
//    @RequestMapping("/user/list")
//    public String home(Model model)
//    {
//        model.addAttribute("users", userService.getAllUser());
//        return "user/list";
//    }
//
//    @GetMapping("/user/add")
//    public String addUser(User bid) {
//        return "user/add";
//    }
//
//    @PostMapping("/user/validate")
//    public String validate(@Valid User user, BindingResult result, Model model) {
//        if (!result.hasErrors()) {
//            userService.saveUser(user);
//            model.addAttribute("users", userService.getAllUser());
//            return "redirect:/user/list";
//        }
//        return "user/add";
//    }
//
//    @GetMapping("/user/update/{id}")
//    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
//        User user = userService.getUserById(id);
//        user.setPassword("");
//        model.addAttribute("user", user);
//        return "user/update";
//    }
//
//    @PostMapping("/user/update/{id}")
//    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
//                             BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "user/update";
//        }
//        try {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            user.setPassword(encoder.encode(user.getPassword()));
//            user.setId(id);
//            userService.saveUser(user);
//            model.addAttribute("users", userService.getAllUser());
//            return "redirect:/user/list";
//        } catch (Exception e) {
//            model.addAttribute("errorMsg", e.getMessage());
//            return "error";
//        }
//    }
//
//    @GetMapping("/user/delete/{id}")
//    public String deleteUser(@PathVariable("id") Integer id, Model model) {
//        try {
//            User user = userService.getUserById(id);
//            userService.deleteUser(user);
//            model.addAttribute("users", userService.getAllUser());
//            return "redirect:/user/list";
//        } catch(Exception e) {
//            model.addAttribute("errorMsg", e.getMessage());
//            return "error";
//        }
//    }
}
