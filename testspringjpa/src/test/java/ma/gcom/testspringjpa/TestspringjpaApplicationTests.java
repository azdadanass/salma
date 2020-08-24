package ma.gcom.testspringjpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ma.gcom.testspringjpa.repos.UserRepos;

@SpringBootTest
class TestspringjpaApplicationTests {

	@Autowired
	UserRepos userRepos;

	@Test
	void contextLoads() {

	}

}
