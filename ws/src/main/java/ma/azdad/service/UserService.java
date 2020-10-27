package ma.azdad.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.repos.UserRepos;

@Component
@Transactional
public class UserService {

	@Autowired
	private UserRepos repos;

	@Autowired
	private CacheService cacheService;

	public User findOne(String username) {
		User u = repos.findById(username).get();
		Hibernate.initialize(u.getUser());
		Hibernate.initialize(u.getSupplier());
		Hibernate.initialize(u.getCustomer());
		Hibernate.initialize(u.getAffectation());
		Hibernate.initialize(u.getRoleList());
		return u;
	}

	public User findOneLight(String username) {
		return repos.findById(username).get();
	}

}
