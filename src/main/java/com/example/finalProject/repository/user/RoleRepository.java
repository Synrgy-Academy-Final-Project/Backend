package com.example.finalProject.repository.user;

import com.example.finalProject.model.user.ERole;
import com.example.finalProject.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findRoleByName(ERole name);
}
