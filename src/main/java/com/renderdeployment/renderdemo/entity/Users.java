package com.renderdeployment.renderdemo.entity;

import com.renderdeployment.renderdemo.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Types;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "Users")
public class Users implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name="userid")
    public UUID id;

    @Column(name="username")
    public String userName;

    @Column(name="password")
    public String password;

    @Column(name = "name")
    private String name;

    @Column(name="email")
    public String email;

    @Column(name = "role")
    private Role role = Role.ADMIN;
}
