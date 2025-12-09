package com.renderdeployment.renderdemo.controller;

import com.renderdeployment.renderdemo.entity.Users;
import com.renderdeployment.renderdemo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;
    @PostMapping("/addUsers")
    public ResponseEntity<?> getEmployee(@RequestBody Users users, @RequestHeader HttpHeaders headers){
        Users user = usersService.addUser(users);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/getEmpById/{id}")
    public ResponseEntity<?> getEmployee(@RequestParam("id") UUID id, @RequestHeader HttpHeaders headers){
        Optional<Users> users = usersService.findUserById(id);
        if(users.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(users.get());
        }
        return ResponseEntity.notFound().build();
    }
}
