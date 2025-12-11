package com.renderdeployment.renderdemo.services;

import com.renderdeployment.renderdemo.Repo.UsersRepo;
import com.renderdeployment.renderdemo.config.MessagingConfig;
import com.renderdeployment.renderdemo.dto.MessageDto;
import com.renderdeployment.renderdemo.entity.Users;
import com.renderdeployment.renderdemo.util.PasswordUtil;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.security.core.GrantedAuthority;
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
    @Autowired
    public RabbitTemplate rabbitTemplate;
    public Logger logger = LoggerFactory.getLogger(UsersService.class);
    @Autowired
    private JavaMailSender javaMailSender;
//    @Value("${spring.mail.username}") private String sender;

    public void sendEmail(String to,String subject,String message){
        SimpleMailMessage sm=new SimpleMailMessage();
        sm.setFrom("ssrcooling@gmail.com");
        sm.setTo(to);
        sm.setSubject(subject);
        sm.setText(message);
        javaMailSender.send(sm);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = usersRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

        // Build authorities based on user's role from DB
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities
        );
    }



    @CachePut(value = "USERS", key = "#result.id")
    @CacheEvict(value = "USERS_ALL", allEntries = true)
    public Users addOrUpdateUser( Users users){
        if(users.getId() == null){
            logger.info("addUser::"+users.getUserName());
            users.setPassword(PasswordUtil.getEncryptedPassword(users.getPassword()));
            logger.info("Password::"+users.getPassword());
            String to = "tndsenbag@gmail.com";
            String subject = "New User";
            String message = "New User";
            //sendEmail(to,subject,message);
            return usersRepo.saveAndFlush(users);
        }else {
            MessageDto userobj = new MessageDto(users,"ordered","test");
           // System.out.println("ENV RABBITMQ URI = " + System.getenv("SPRING_RABBITMQ_URI"));
            Object response =  rabbitTemplate.convertSendAndReceive(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY,userobj);
            logger.info("response RabbitMQ"+response);
            users.setPassword(PasswordUtil.getEncryptedPassword(users.getPassword()));
            return usersRepo.saveAndFlush(users);
        }

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
