package ma.gcom.testspringjpa.service.impl;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.gcom.testspringjpa.model.Car;
import ma.gcom.testspringjpa.repos.CarRepos;
import ma.gcom.testspringjpa.service.CarService;

@Component
@Transactional
public class CarServiceImpl implements CarService {
	
	@Autowired
	CarRepos repos;

	@Override
	public Car save(Car car) {
		
		Car databaseCar = repos.save(car);
		return databaseCar;
	}

	
}
