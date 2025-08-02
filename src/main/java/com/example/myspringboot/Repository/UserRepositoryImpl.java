package com.example.myspringboot.Repository;

import com.example.myspringboot.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public class UserRepositoryImpl {



    @Autowired
    private MongoTemplate mongoTemplate;


    public List<User> getUserForSa(){

        Query query = new Query();

        query.addCriteria(Criteria.where("userName").is("tester"));
        return mongoTemplate.find(query, User.class);
    }
}
