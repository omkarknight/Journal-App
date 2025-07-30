package com.example.myspringboot.Service;

import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


/// This is for Mockito initializer on java 17+
@ExtendWith(MockitoExtension.class)
@Disabled
class UserDetailsServiceIMPLTest {

    @InjectMocks
    public UserDetailsServiceIMPL userDetailsService;

    @InjectMocks
    public UserService userService;

    @Mock
    public UserRepository userRepository;

    @Mock
    public PasswordEncoder passwordEncoder;

//    @BeforeEach                              /// this is for Mockito Initializer
//    void setUp(){                            /// After java 8 this is not required
//        MokitoAnnotations.intitMocks(this);
//    }


    @Disabled
    @Test
    void loadUserByUsernameTest(){

        when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("ram").password("$2%3java34").roles(new ArrayList<>()).build());

        UserDetails user = userDetailsService.loadUserByUsername("ram");

        Assertions.assertNotNull(user);
    }

    @Disabled
    @Test
    void saveAdminTest(){

        User adminUser = User.builder()
                .userName("ram")
                .password("$2$python")
                .roles(new ArrayList<>())
                .build();

        //Mock behavior
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword123");

        // Act
        userService.saveAdmin(adminUser);


        // Assert
        Assertions.assertEquals("encodedPassword123", adminUser.getPassword());
        Assertions.assertTrue(adminUser.getRoles().contains("USER"));
        Assertions.assertTrue(adminUser.getRoles().contains("ADMIN"));



        verify(userRepository, times(1)).save(adminUser);
    }


}
