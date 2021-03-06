package ma.gcom.testspringjpa.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.EmailService;
import ma.gcom.testspringjpa.service.UserService;
import ma.gcom.testspringjpa.utiles.PasswordCrypter;
import ma.gcom.testspringjpa.utiles.PasswordGenerator;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@GetMapping("/user")
	public String findAll(Model model) {
		List<User> userList = userService.findAll(true);
		String test = "abcdefg";
		model.addAttribute("userList", userList);
		model.addAttribute("test", test);
		model.addAttribute("connectedUser", userService.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName()));
		return "users.html";
	}

	@GetMapping("/user/{id}")
	public String findOne(@PathVariable Integer id, Model model) {
		model.addAttribute("user", userService.findOne(id));
		return "viewUser.html";
	}

	@GetMapping("/deleteUser/{id}")
	@Secured("ROLE_ADMIN")
	public String delete(@PathVariable Integer id) {
		userService.delete(id);
		return "redirect:/user";
	}

	@GetMapping("/carList/{id}")
	public String getCarListUer(@PathVariable Integer id, Model model) {
		model.addAttribute("carList", userService.findOne(id).getCarList());
		model.addAttribute("user", userService.findOne(id));
		return "carListUser.html";

	}

	@GetMapping("/insertUser")
	public String friendForm(Model model) {
		model.addAttribute("userForm", new User());
		return "insertUser";
	}

	@PostMapping("/insertUser")
	@Secured("ROLE_ADMIN") // secured by spring security
	public String insert(@ModelAttribute("userForm") User user) throws NoSuchAlgorithmException {
		String passwordClair = PasswordGenerator.generateRandomPassword(6);
		user.setPassword(PasswordCrypter.CryptMd5(passwordClair));
		User createdUser = userService.save(user);
//		userService.updatePassword(PasswordCrypter.CryptMd5(myPassword),createdUser.getId());
		emailService.sendOneEmail(createdUser.getId(), passwordClair);
		System.out.println("point");
		return "redirect:/user/" + createdUser.getId();
	}

}
