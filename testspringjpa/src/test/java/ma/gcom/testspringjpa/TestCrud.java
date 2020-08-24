package ma.gcom.testspringjpa;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import ma.gcom.testspringjpa.model.Car;
import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.repos.CarRepos;
import ma.gcom.testspringjpa.repos.UserRepos;

@SpringBootTest
@Rollback(false)
@Transactional
class TestCrud {

	@Autowired
	UserRepos userRepos;

	@Autowired
	CarRepos carRepos;

	@Test
	void testttt() {
		User user = new User(null, "aaaaa", "bbbb");
		System.out.println(user.getId());
		user = userRepos.save(user);
		System.out.println(user.getId());
	}

//	@Test
	void testLazyEager() {
		Car car = carRepos.getOne(3);
		System.out.println();
		System.out.println(car.getMatricule());
		System.out.println();
		System.out.println(car.getUser().getFirstName());
		System.out.println();

	}

//	@Test
	void testDeleteCascade() {
		userRepos.deleteById(1);
	}

//	@Test
	void testCreateCascade2() {
		User user = new User();
		user.setFirstName("C");
		user.setLastName("C");

		Car c1 = new Car();
		c1.setMatricule("AAAAAAAAAAAAA");
		Car c2 = new Car();
		c2.setMatricule("AAAAAAAAAAAAA");

		user.addCar(c1);
		user.addCar(c2);

		userRepos.save(user);
	}

//	@Test
	void testCreateCascade1() {
		User user = new User();
		user.setFirstName("C");
		user.setLastName("C");

		Car c1 = new Car();
		c1.setMatricule("AAAAAAAAAAAAA");
		c1.setUser(user);
		Car c2 = new Car();
		c2.setMatricule("AAAAAAAAAAAAA");
		c2.setUser(user);

		user.getCarList().add(c1);
		user.getCarList().add(c2);

		userRepos.save(user);
	}

//	@Test
	void testDelete() {
		userRepos.deleteById(5);
	}

//	@Test
	void testUpdate() {
		User user = userRepos.getOne(1);
		user.setLastName("newLastName");
		userRepos.save(user);
	}

//	@Test
	void testRead2() {
		User user = userRepos.getOne(1);
		System.out.println(user);
	}

//	@Test
	void testRead1() {
		List<User> userList = userRepos.findAll();

		System.out.println(userList);

	}

//	@Test
	void testCreate() {
		User user = new User();
		user.setFirstName("AAAAAAA");
		user.setLastName("Bbbb");
		user.setBirthday(new Date());
		userRepos.save(user);
	}

}
