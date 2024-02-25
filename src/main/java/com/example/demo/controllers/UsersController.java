package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UsersController {

    @Autowired
    private UserRepository userRepo;
    // Function take from Proffesor Bobby Chan tutorial
    @GetMapping("/users/view")
    public String getAllUsers(Model model) {
        System.out.println("Getting all users");
        // get all users from database
        List<User> users = userRepo.findAll();
        // end of database call
        model.addAttribute("us", users);
        return "users/showAll";
    }
    // Function take from Proffesor Bobby Chan tutorial
    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response) {
        System.out.println("ADD user");
        String newName = newuser.get("name");
        int newWeight = Integer.parseInt(newuser.get("weight"));
        int newHeight = Integer.parseInt(newuser.get("height"));
        String newHairColor = newuser.get("hairColor");
        double newGPA = Double.parseDouble(newuser.get("gpa"));

        userRepo.save(new User(newName, newWeight, newHeight, newHairColor, newGPA));
        response.setStatus(201);
        return "users/addedUser";
    }
    // Prompts user to just enter UID
    @GetMapping("/users/edit")
    public String editUserPage(@RequestParam("uid") int uid, Model model, HttpServletResponse response) {
        User user = userRepo.findById(uid).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            response.setStatus(HttpServletResponse.SC_OK);
            return "users/updateUserForm";
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return "users/userNotFound";
        }
    }
    // Updates the users information with the fields pre-filled with previous info
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user, HttpServletResponse response) {
        Optional<User> existingUser = userRepo.findById(user.getUid());

        if (existingUser.isPresent()) {
            
            userRepo.save(user);
            response.setStatus(HttpServletResponse.SC_OK);
            return "users/updatedUser"; 
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return "users/userNotFound"; 
        }
    }
    // If the uid exists then it is deleted from the database
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam int uid, HttpServletResponse response) {
        System.out.println("Deleting user with ID" + uid);

        if (userRepo.existsById(uid)) {
            userRepo.deleteById(uid);
            response.setStatus(HttpServletResponse.SC_OK);
            return "users/userDeleted";
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return "users/userNotFound";
        }

    }

}
