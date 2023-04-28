package com.gl.collegefest.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gl.collegefest.dto.UserDto;
import com.gl.collegefest.entity.User;
import com.gl.collegefest.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AuthController {

	private UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@PostMapping("/")
	public String postHome() {
		return "index";
	}

	@GetMapping("/login")
	public String loginForm() {
		System.out.println("test 3");
		return "login";
	}

	@RequestMapping("/users/edit")
	public String afterEdit(HttpServletRequest request) {
		String userName = request.getRemoteUser();

		if (request.isUserInRole("ROLE_ADMIN")) {
			return "redirect:/users";
		}
		return "redirect:/userview/" + userName;
	}

	@RequestMapping("/default")
	public String defaultAfterLogin(HttpServletRequest request) {
		String userName = request.getRemoteUser();

		if (request.isUserInRole("ROLE_ADMIN")) {
			return "redirect:/users";
		}
		return "redirect:/userview/" + userName;
	}

	@GetMapping("/userview/{userName}")
	public String userView(@PathVariable String userName, Model model) {
		User user = userService.findByEmail(userName);
		UserDto users = userService.findById(user.getId());
		model.addAttribute("users", users);
		return "view";
	}

	@GetMapping("/useredit/{userName}")
	public String userEdit(@PathVariable String userName, Model model) {
		User user = userService.findByEmail(userName);
		UserDto users = userService.findById(user.getId());
		model.addAttribute("users", users);
		return "edit";
	}

	@PostMapping("/login")
	public String postLoginForm() {
		return "users";
	}

	// handler method to handle user registration request
	@GetMapping("register")
	public String showRegistrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "register";
	}

	// handler method to handle register user form submit request
	@PostMapping("/register/save")
	public String registration(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model) {
		User existing = userService.findByEmail(user.getEmail());

		if (existing != null) {
			result.rejectValue("email", null, "There is already an account registered with that email");
		}
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "register";
		}
		userService.saveUser(user);
		return "redirect:/register?success";
	}

	@PostMapping("/register/update/{id}")
	public String updation(@PathVariable Long id, @ModelAttribute("users") UserDto users, Model model) {

		UserDto existing = userService.findById(id);

		existing.setFirstName(users.getFirstName());
		existing.setLastName(users.getLastName());
		existing.setEmail(users.getEmail());
		existing.setPassword(users.getPassword());
		existing.setCourse(users.getCourse());
		existing.setCountry(users.getCountry());

		userService.updateUser(users);

		// return "redirect:/users/edit?success";
		return "redirect:/users/edit";
	}

	@GetMapping("/users")
	public String listRegisteredUsers(Model model, HttpServletRequest request) {

		if (request.isUserInRole("ROLE_ADMIN")) {
			List<UserDto> users = userService.findAllUsers();

			model.addAttribute("users", users);

			return "list";
		}
		return "redirect:/index";

	}

	@GetMapping("/access-denied")
	public String getAccessDenied() {
		return "/error/accessDenied";
	}

	@GetMapping("/users/view/{id}")
	public String viewUser(@PathVariable Long id, Model model) {
		UserDto users;
		users = userService.findById(id);

		model.addAttribute("users", users);
		return "view";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		UserDto users;
		users = userService.findById(id);

		model.addAttribute("users", users);
		return "edit";
	}

	@GetMapping("/users/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}

}
