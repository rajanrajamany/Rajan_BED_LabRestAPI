package com.gl.collegefest.repository;

import com.gl.collegefest.dto.UserDto;
import com.gl.collegefest.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	void save(UserDto userDto);

}
