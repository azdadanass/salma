package ma.gcom.testspringjpa;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import ma.gcom.testspringjpa.service.EmailService;
import ma.gcom.testspringjpa.service.UserService;

@SpringBootTest
@Rollback(false)
@Transactional
class TestService {

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@Test
	public void testCaching() {
		System.out.println("1-->" + userService.findAll(true));
		System.out.println("2-->" + userService.findAll(true));
		System.out.println("3-->" + userService.findAll(true));
		userService.delete(24);
		System.out.println("4-->" + userService.findAll(true));
	}

//	@Test
	public void testSendEmail() {
		emailService.sendTestEmail();
	}

//	@Test
	public void test() {
		userService.showAllUsers();
		userService.findAll(false);
	}

}
