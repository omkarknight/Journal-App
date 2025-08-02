package com.example.myspringboot.Repository;

import com.example.myspringboot.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    void testCriteria() {

      List<User> user = userRepository.getUserForSa();

      assertNotNull(user);
      assertFalse(user.isEmpty());
    }
}
