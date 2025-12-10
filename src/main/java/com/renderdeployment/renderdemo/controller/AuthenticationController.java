package com.renderdeployment.renderdemo.controller;

import com.renderdeployment.renderdemo.Repo.UsersRepo;
import com.renderdeployment.renderdemo.dto.ErrorDto;
import com.renderdeployment.renderdemo.dto.LoginRequest;
import com.renderdeployment.renderdemo.entity.Users;
import com.renderdeployment.renderdemo.response.ResponseGenerator;
import com.renderdeployment.renderdemo.response.TransactionContext;
import com.renderdeployment.renderdemo.security.JwtTokenUtil;
import com.renderdeployment.renderdemo.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthenticationController {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private  JwtTokenUtil jwtTokenUtil;
    @Autowired
    private  ResponseGenerator responseGenerator;
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, @RequestHeader HttpHeaders httpHeader) throws Exception {
        ErrorDto errorDto = null;
        System.out.println("username"+request.getUsername());
        System.out.println("password"+request.getPassword());
        Map<String, Object> response = new HashMap<String, Object>();
        if (null == request) {
            errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Invalid Request Payload.!");
            response.put("status", 0);
            response.put("error", errorDto);
            return ResponseEntity.badRequest().body(response);
        }
        Optional<Users> userOptional = usersRepo.findByUserName(request.getUsername());
        if(!userOptional.isPresent()) {
            errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Invalid Username.!");
            response.put("status", 0);
            response.put("error", errorDto);
            return ResponseEntity.badRequest().body(response);
        }
        Users user = userOptional.get();

        String encryptedPassword = PasswordUtil.getEncryptedPassword(request.getPassword());
        if(!user.getPassword().equals(encryptedPassword)) {
		    /*
        if(!user.getPassword().equals(request.getPassword())) { */
            errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Password is wrong.!");
            response.put("status", 0);
            response.put("error", errorDto);
            return ResponseEntity.badRequest().body(response);
        }

        final String token = jwtTokenUtil.generateToken(user);
        response.put("status", 1);
        response.put("message","Logged in Successfully.!");
        response.put("jwt", token);
        response.put("role", user.getRole());
        response.put("name", user.getName());
       // response.put("employeeId", user.getEmpId());
        response.put("userId", user.getId());
        response.put("isOtpVerified", true);
        TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
        try {
            return responseGenerator.successResponse(context, response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
