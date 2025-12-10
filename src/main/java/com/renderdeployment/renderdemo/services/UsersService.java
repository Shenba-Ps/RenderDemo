package com.renderdeployment.renderdemo.services;

import com.renderdeployment.renderdemo.Repo.UsersRepo;
import com.renderdeployment.renderdemo.entity.Users;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {

    @Autowired
    private UsersRepo usersRepo;

    public Logger logger = LoggerFactory.getLogger(UsersService.class);

    @CachePut(value = "USERS", key = "#result.id")
    @CacheEvict(value = "USERS_ALL", allEntries = true)
    public Users addUser( Users users){
        logger.info("addUser::"+users.getUserName());
        return usersRepo.save(users);
    }

    @Cacheable(value = "USERS", key = "#userId" )
    public Optional<Users> findUserById(UUID userId){
        logger.info("Hitting db::");
        return usersRepo.findById(userId);
    }

    @Cacheable(value = "USERS_ALL")
    public List<Users> findAllUsers(){
        logger.info("findAllUsers hitting db::");
        return usersRepo.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "USERS", key = "#userId"),
            @CacheEvict(value = "USERS_ALL", allEntries = true)
    })
    public void deleteUserById(UUID userId){
        logger.info("delete user::");
        usersRepo.deleteById(userId);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void schedulingtest(){
        logger.info("schedulingtest ::");
      List<Users> userList =  usersRepo.findAll();
        logger.info("userList:: ::"+ userList);
    }

}
