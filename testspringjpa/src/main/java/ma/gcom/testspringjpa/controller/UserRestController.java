package ma.gcom.testspringjpa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.UserService;

@RestController
public class UserRestController {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	@GetMapping("/rest/user")
	public List<User> findAll() {
		return userService.findAll(false);
	}

	@GetMapping("/rest/user/{id}")
	public User findOne(@PathVariable Integer id) {
		return userService.findOne(id);
	}

	@GetMapping("/rest/deleteUser/{id}")
	public String delete(@PathVariable Integer id) {
		return userService.delete(id) ? "success" : "error";
	}

	@PostMapping("/rest/user")
	public User insert(User user) {
		return userService.save(user);
	}

}
