package ma.gcom.testspringjpa;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import ma.gcom.testspringjpa.service.UserService;

@SpringBootTest
@Rollback(false)
@Transactional
class TestService {

	@Autowired
	UserService userService;

	@Test
	public void test() {
		userService.showAllUsers();
		userService.findAll(false);
	}

}
