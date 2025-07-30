package com.example.myspringboot.Controller;

import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll();

        if (all != null && !all.isEmpty()) {
            return ResponseEntity.ok(all);
        }

        return ResponseEntity.status(404).build();
    }

    @PostMapping("/create/admin-user")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {

        userService.saveAdmin(user);
        return ResponseEntity.ok().build();

    }

}

