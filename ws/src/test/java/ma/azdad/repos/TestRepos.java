package ma.azdad.repos;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import ma.azdad.GenericTest;;

@Transactional
@Rollback(false)
public class TestRepos extends GenericTest {

	@Autowired
	UserRepos ur;

	@Test
	public void test1() throws Exception {
	}

}
