package com.renderdeployment.renderdemo.services;

import com.renderdeployment.renderdemo.Repo.UsersRepo;
import com.renderdeployment.renderdemo.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepo usersRepo;
    public Logger logger = LoggerFactory.getLogger(UsersService.class);
    @CachePut(value = "USERS", key = "#result.id")
    public Users addUser( Users users){
        logger.info("addUser::"+users.getUserName());
        return usersRepo.save(users);
    }
    @Cacheable(value = "USERS", key = "#userId")
    public Optional<Users> findUserById(UUID userId){
        logger.info("Hitting db::");
        return usersRepo.findById(userId);
    }

}
