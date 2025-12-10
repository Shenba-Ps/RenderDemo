package com.renderdeployment.renderdemo.validator;

import com.renderdeployment.renderdemo.constant.Messages;
import com.renderdeployment.renderdemo.controllerAdvice.ObjectInvalidException;
import com.renderdeployment.renderdemo.dto.UserDto;
import com.renderdeployment.renderdemo.entity.Users;
import com.renderdeployment.renderdemo.enumeration.RequestType;
import com.renderdeployment.renderdemo.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserValidation {

    Logger logger = LoggerFactory.getLogger(UserValidation.class);
    @Autowired
    private UsersService usersService;

    List<String> errors = null;
    List<String> errorsObj = null;
    Optional<Subject> subject = null;


    public ValidationResult validate(RequestType requestType, UserDto request) throws Exception {
        errors = new ArrayList<>();

        Users userObj = null;
        logger.info("requestType:"+requestType.toString());
        if (requestType.equals(RequestType.POST)) {
            if (!ValidationUtil.isNull1(request.getId())) {
                throw new ObjectInvalidException(Messages.INVALID_REQ_PAYLOAD);
            }
            if (ValidationUtil.isNullOrEmpty(request.getUserName())) {
                errors.add(Messages.USER_NAME_REQUIRED);
            }
            if (ValidationUtil.isNullOrEmpty(request.getEmail())) {
                errors.add(Messages.EMAIL_REQUIRED);
            }
            if(ValidationUtil.isNullOrEmpty(request.getPassword())) {
                errors.add(Messages.PASSWORD_REQUIRED);
            }
        }else{
            if (ValidationUtil.isNull1(request.getId())) {
                throw new ObjectInvalidException(Messages.INVALID_REQ_PAYLOAD);
            }
            Optional<Users> usersOptional = usersService.findUserById(request.getId());
            if (!usersOptional.isPresent()) {
                throw new ObjectInvalidException(Messages.USER_NOT_FOUND);
            }

            userObj = usersOptional.get();
        }
        ValidationResult result = new ValidationResult();
        if (errors.size() > 0) {
            String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
            throw new ObjectInvalidException(errorMessage);
        }
        if(null==userObj) {
            userObj = Users.builder().userName(request.getUserName())
                    .password(request.getPassword()).email(request.getEmail()).role(request.getRole())
                            .name(request.getName()).
                    build();

        }
        else {

            userObj.setUserName(request.getUserName());
            userObj.setEmail(request.getEmail());
            userObj.setPassword(request.getPassword());
            userObj.setName(request.getName());
            userObj.setRole(request.getRole());


        }
        result.setObject(userObj);
        return result;
    }
}
