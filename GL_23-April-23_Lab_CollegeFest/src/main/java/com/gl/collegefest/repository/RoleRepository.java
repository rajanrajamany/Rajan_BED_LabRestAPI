package com.gl.collegefest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.collegefest.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
