package com.renderdeployment.renderdemo.dto;

import com.renderdeployment.renderdemo.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDto {

    Users user;
    String status;
    String message;
}
