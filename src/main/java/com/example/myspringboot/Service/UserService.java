package com.example.myspringboot.Service;



import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // use this when you waant to create a instance

    public Boolean saveUser(User user) {

        try{
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            user.setJournalEntries(new ArrayList<>());

            this.userRepository.save(user);

            return true;
        }
        catch(Exception e){
            /// This logger is only working with instance of their class
//          logger.error("Same user name or Password occurred for : {} ",user.getUserName());

            /// No need to take instance only use log instead of logger and annotate class with @SlF4j
            log.info("Same User name or Password occurred for : {} ",user.getUserName());
            log.warn("Same User name or Password occurred for : {} ",user.getUserName());
            log.error("Same User name or Password occurred for : {} ",user.getUserName());
            log.debug("Same User name or Password occurred for : {} ",user.getUserName());
            return false;
        }

    }

    ///  very imp method otherwise cause major error
    public void saveUserWithoutEncoding(User user) {
         this.userRepository.save(user);
    }

    /// this save is for Admin
    public void saveAdmin(User user) {

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        if(user.getRoles() == null || user.getRoles().isEmpty()){
            user.setRoles(List.of("USER", "ADMIN"));
        }
         this.userRepository.save(user);
    }

    /// Update User
    public User updateUser(User newUser) {

        User existingUser = this.userRepository.findByUserName(newUser.getUserName());

        if(existingUser == null){
            throw new RuntimeException("User not found");
        }

        if(!this.passwordEncoder.matches(newUser.getPassword(), existingUser.getPassword())){
            existingUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        }

        return this.userRepository.save(existingUser);
    }

    public List<User> getAll() {
       return this.userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
      return this.userRepository.findById(id);
    }

    public User findByUserName(String username) {
        return this.userRepository.findByUserName(username);
    }

    public void deleteById(ObjectId id) {
     this.userRepository.deleteById(id);
    }


    public void deleteByName(String username) {
        this.userRepository.deleteByUserName(username);
    }

}
