package com.renderdeployment.renderdemo.controller;

import com.renderdeployment.renderdemo.constant.Messages;
import com.renderdeployment.renderdemo.dto.UserDto;
import com.renderdeployment.renderdemo.entity.Users;
import com.renderdeployment.renderdemo.enumeration.RequestType;
import com.renderdeployment.renderdemo.response.ResponseGenerator;
import com.renderdeployment.renderdemo.response.TransactionContext;
import com.renderdeployment.renderdemo.services.UsersService;
import com.renderdeployment.renderdemo.validator.UserValidation;
import com.renderdeployment.renderdemo.validator.ValidationResult;
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
    @Autowired
    private  ResponseGenerator responseGenerator;

    @Autowired
    private UserValidation userValidation;


    @PostMapping("/addUsers")
    public ResponseEntity<?> createUser(@RequestBody UserDto users, @RequestHeader HttpHeaders headers){
        TransactionContext context = responseGenerator.generateTransationContext(headers);
        try{
            ValidationResult validationResult = userValidation.validate(RequestType.POST, users);
            Users user = usersService.addUser((Users)validationResult.getObject());
            return responseGenerator.successResponse(context, Messages.SUCESS_MESSAGE,user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping(value="/getEmpById/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("id") UUID id, @RequestHeader HttpHeaders httpHeader){
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            Optional<Users> users = usersService.findUserById(id);
                return responseGenerator.successGetResponse(context, Messages.USER_RETRIVED, users,
                        HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping(value="/findAllUsers", produces = "application/json")
    public ResponseEntity<?> findAllUsers(@RequestHeader HttpHeaders httpHeader){
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            List<Users> users = usersService.findAllUsers();
            return responseGenerator.successGetResponse(context, Messages.USER_RETRIVED,
                    users, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") UUID id,@RequestHeader HttpHeaders httpHeader){
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try{
            usersService.deleteUserById(id);
            return responseGenerator.successResponse(context,Messages.USER_DELETED,HttpStatus.OK);
        }catch(Exception e){
            log.error(e.getMessage(), e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
