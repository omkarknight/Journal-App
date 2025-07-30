package com.example.myspringboot.Controller;


import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Service.JournalEntryService;
import com.example.myspringboot.Service.UserService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable ObjectId id)
    {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> find = userService.findById(id);
        if(find.isEmpty())
        {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "User Not Found!!"));
        }

        User user = find.get();

        if(!user.getUserName().equals(authenticatedUsername)){
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Unauthorized access"));
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user)
    {
        String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        User UserIndb = userService.findByUserName(authenticatedUser);

        if(UserIndb == null)
        {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "User not found"));
        }

        UserIndb.setPassword(user.getPassword());

        User saveUser = userService.updateUser(UserIndb);

        return ResponseEntity.status(202).body(saveUser);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteByUserName()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user =  userService.findByUserName(username);

        if(user == null){
            return ResponseEntity.status(404)
                    .body(Map.of("message", "User not found"));
        }

        userService.deleteByName(username);
        return ResponseEntity.status(200).body(Map.of("message", "User deleted","username",username));
    }

}
