package com.gl.collegefest.service;

import com.gl.collegefest.dto.UserDto;
import com.gl.collegefest.entity.User;

import java.util.List;

public interface UserService {
	void saveUser(UserDto userDto);

	void updateUser(UserDto userDto);

	User findByEmail(String email);

	List<UserDto> findAllUsers();

	UserDto findById(Long id);

	void deleteUser(Long id);
}
