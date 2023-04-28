package com.gl.collegefest.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gl.collegefest.dto.UserDto;
import com.gl.collegefest.entity.Role;
import com.gl.collegefest.entity.User;
import com.gl.collegefest.repository.RoleRepository;
import com.gl.collegefest.repository.UserRepository;
import com.gl.collegefest.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void saveUser(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getFirstName() + " " + userDto.getLastName());
		user.setEmail(userDto.getEmail());

		// encrypt the password once we integrate spring security
		// user.setPassword(userDto.getPassword());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		// Role role = roleRepository.findByName("ROLE_ADMIN");
		Role role = roleRepository.findByName("ROLE_USER");
		if (role == null) {
			role = checkRoleExist();
		}
		user.setRoles(Arrays.asList(role));
		user.setCourse(userDto.getCourse());
		user.setCountry(userDto.getCountry());
		System.out.println("test registration");
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();

		//        System.out.println("test user id");
		//        users.forEach(
		//        		(temp) -> System.out.println(temp.getId()));

		return users.stream().map((user) -> convertEntityToDto(user)).collect(Collectors.toList());
	}

	private UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto();

		String[] name = user.getName().split(" ");

		userDto.setId(user.getId());
		userDto.setFirstName(name[0]);
		userDto.setLastName(name[1]);
		userDto.setEmail(user.getEmail());
		userDto.setCourse(user.getCourse());
		userDto.setCountry(user.getCountry());

		return userDto;
	}

	/*
	 * private User convertDtoToEntity(UserDto userDto){ User existing =
	 * userRepository.findById(userDto.getId()).get();
	 * 
	 * existing.setName(userDto.getFirstName() + " " + userDto.getLastName());
	 * existing.setEmail(userDto.getEmail());
	 * existing.setPassword(userDto.getPassword());
	 * existing.setCourse(userDto.getCourse());
	 * existing.setCountry(userDto.getCountry());
	 * 
	 * return existing; }
	 */

	private Role checkRoleExist() {
		Role role = new Role();
		// role.setName("ROLE_ADMIN");
		role.setName("ROLE_USER");
		return roleRepository.save(role);
	}

	@Override
	public UserDto findById(Long id) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(id).get();
		return convertEntityToDto(user);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateUser(UserDto userDto) {
		// TODO Auto-generated method stub

		User userToUpdate = userRepository.getById(userDto.getId());
		// User userToUpdate = userRepository.findByEmail(userDto.getEmail());
		System.out.println("test 1234");
		userToUpdate.setName(userDto.getFirstName() + " " + userDto.getLastName());
		System.out.println("test 12345");
		userToUpdate.setEmail(userDto.getEmail());
		userToUpdate.setPassword(userDto.getPassword());
		userToUpdate.setCourse(userDto.getCourse());
		userToUpdate.setCountry(userDto.getCountry());

		userRepository.save(userToUpdate);

	}

	public void deleteUser(Long id) {
		System.out.println("delete the id");
		userRepository.deleteById(id);

	}

}
