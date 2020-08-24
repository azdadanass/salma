package ma.gcom.testspringjpa;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import ma.gcom.testspringjpa.repos.CarRepos;
import ma.gcom.testspringjpa.repos.UserRepos;

@SpringBootTest
@Rollback(false)
@Transactional
class TestRepos {

	@Autowired
	UserRepos userRepos;

	@Autowired
	CarRepos carRepos;

	@Test
	public void test() {
		System.out.println("\n");
		System.out.println(userRepos.findByFirstName("c"));
		System.out.println(userRepos.countByFirstName("c"));
		System.out.println(userRepos.findByFirstNameHql("c"));
		System.out.println(userRepos.findByFirstNameHql("c"));
		System.out.println(carRepos.findByUserFirstName("c"));

		System.out.println(userRepos.findAllLight());
		userRepos.updateLastLogin(6);
		System.out.println("\n");
	}

}
