package com.renderdeployment.renderdemo.dto;

import com.renderdeployment.renderdemo.enumeration.Role;
import lombok.Data;

import java.util.UUID;
@Data
public class UserDto {

    public UUID id;
    public String userName;
    public String password;
    public String email;
    public String name;
    private Role role = Role.ADMIN;
}
