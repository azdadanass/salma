package ma.gcom.testspringjpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import ma.gcom.testspringjpa.model.Car;
import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.CarService;
import ma.gcom.testspringjpa.service.UserService;

@Controller
public class CarController {

	@Autowired
	CarService carService;
	@Autowired
	UserService userService;

	@PostMapping(path = "/addCar")
	public String insert(WebRequest request) {

		String userId = request.getParameter("userId");
		int id = Integer.parseInt(userId);

		User user = userService.findOne(id);
		Car car = new Car();

		car.setMatricule(request.getParameter("mtricule"));

		user.addCar(car);
		carService.save(car);

		return "redirect:/carList/" + userId;
	}

}
