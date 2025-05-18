package com.saurabh.securenotesapi.repository;

import com.saurabh.securenotesapi.model.AppRole;
import com.saurabh.securenotesapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole appRole);
}
