package com.example.myspringboot.Controller;

import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Service.UserService;
import com.example.myspringboot.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user)
    {
           boolean save = userService.saveUser(user);

           if(!save){
               return ResponseEntity.status(500)
                       .body(Map.of("message","Something went wrong while signup","status",false));
           }
          return ResponseEntity.status(200)
                  .body(Map.of("message","SignUp completed","status",true));
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody User user)
    {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());

            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.status(200).body(jwt);

        }catch (Exception e){

            log.error("Exception occurred while createAuthentication ", e);

            return ResponseEntity.status(502)
                    .body(Map.of("message","Login failed","status",false));
        }
    }


}
