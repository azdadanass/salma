package ma.azdad.repos;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import ma.azdad.GenericTest;;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	UserRepos ur;

	@Test
	@Transactional
	public void test1() throws Exception {
		ur.updateContractActive();
	}

}
