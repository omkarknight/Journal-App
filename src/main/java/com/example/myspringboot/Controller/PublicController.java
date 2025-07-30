package com.example.myspringboot.Controller;

import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Service.JournalEntryService;
import com.example.myspringboot.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Validated
public class PublicController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalEntryService journalEntryService;


    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user)
    {
//            User get =
           boolean save = userService.saveUser(user);

           if(!save){
               return new ResponseEntity<>(save,HttpStatus.INTERNAL_SERVER_ERROR);
           }
          return new ResponseEntity<>(save,HttpStatus.CREATED);

    }


}
