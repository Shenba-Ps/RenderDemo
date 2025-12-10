package com.renderdeployment.renderdemo.services;

import com.renderdeployment.renderdemo.Repo.UsersRepo;
import com.renderdeployment.renderdemo.entity.Users;
import com.renderdeployment.renderdemo.util.PasswordUtil;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.*;

@Service("userService")
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    public Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userOptional = usersRepo.findByUserName(username);
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Users user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }


    @CachePut(value = "USERS", key = "#result.id")
    @CacheEvict(value = "USERS_ALL", allEntries = true)
    public Users addUser( Users users){
        logger.info("addUser::"+users.getUserName());
        users.setPassword(PasswordUtil.getEncryptedPassword(users.getPassword()));
        logger.info("Password::"+users.getPassword());
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
    //password generator randomly
    public static String generateRandomPassword(int len) {

        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
