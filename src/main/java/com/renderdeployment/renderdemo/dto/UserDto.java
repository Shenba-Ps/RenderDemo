package com.renderdeployment.renderdemo.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class UserDto {

    public UUID id;
    public String userName;
    public String password;
    public String email;
}
