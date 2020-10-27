package ma.azdad.repos;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import ma.azdad.GenericTest;
import ma.azdad.service.UserService;;

@Rollback(false)
public class Merge extends GenericTest {

	@Autowired
	UserService us;

	@Test
	@Transactional
	public void test1() throws Exception {

		us.generateUserDataId();

	}

}
