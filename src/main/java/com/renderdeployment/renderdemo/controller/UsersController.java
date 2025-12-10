package com.renderdeployment.renderdemo.controller;

import com.renderdeployment.renderdemo.entity.Users;
import com.renderdeployment.renderdemo.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsersController {
 public Logger log = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    private UsersService usersService;
    @PostMapping("/addUsers")
    public ResponseEntity<?> getEmployee(@RequestBody Users users, @RequestHeader HttpHeaders headers){
        Users user = usersService.addUser(users);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/getEmpById/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable("id") UUID id, @RequestHeader HttpHeaders headers){
        Optional<Users> users = usersService.findUserById(id);
        if(users.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(users.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findAllUsers")
    public ResponseEntity<?> findAllUsers(@RequestHeader HttpHeaders httpHeader){

        List<Users> users = usersService.findAllUsers();
        return ResponseEntity.status(HttpStatus.FOUND).body(users);
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") UUID id,@RequestHeader HttpHeaders headers){
        try{
            usersService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully with ID: " + id);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
