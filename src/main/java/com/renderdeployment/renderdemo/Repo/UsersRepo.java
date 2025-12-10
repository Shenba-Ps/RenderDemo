package com.renderdeployment.renderdemo.Repo;

import com.renderdeployment.renderdemo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepo extends JpaRepository<Users, UUID> {

    @Query(value = "SELECT * FROM users  where user_name =:username", nativeQuery = true)
    Optional<Users> findByUserName(String username);
}
