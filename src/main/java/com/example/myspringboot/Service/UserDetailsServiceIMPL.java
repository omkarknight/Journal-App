package com.example.myspringboot.Service;

import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceIMPL implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username);
        if (user != null) {

            return org.springframework.security.core.userdetails.User.builder()
                       .username(user.getUserName())
                       .password(user.getPassword())
                       .roles(user.getRoles().toArray(new String[0]))
                       .build();
        }
        throw new UsernameNotFoundException("username not found" + username);
    }

}
