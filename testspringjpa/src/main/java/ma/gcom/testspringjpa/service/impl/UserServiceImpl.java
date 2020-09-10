package ma.gcom.testspringjpa.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.repos.UserRepos;
import ma.gcom.testspringjpa.service.EmailService;
import ma.gcom.testspringjpa.service.UserService;

@Component
@Transactional
public class UserServiceImpl implements UserService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRepos repos;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	EmailService emailService;

	@Override
	@Cacheable("cache1")
	public List<User> findAll(Boolean light) {
		return light ? repos.findAllLight() : repos.findAll();
	}

	@Override
	public User save(User user) {
		cacheManager.getCache("cache1").clear();
		User databaseUser = repos.save(user);
		repos.updateLastLogin(databaseUser.getId());
		return databaseUser;
	}

	@Override
	public User findOne(Integer id) {
		return repos.findById(id).get();
	}

	@Override
	public List<User> findByFirstName(String firstName) {
		return repos.findByFirstName(firstName);
	}

	@Override
	public Long countByFirstName(String firstName) {
		return repos.countByFirstName(firstName);
	}

	@Override
	public Boolean delete(Integer id) {
		if (repos.existsById(id)) {
			cacheManager.getCache("cache1").clear();
			repos.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public Boolean delete(User user) {
		return delete(user.getId());
	}

	@Override
	public void showAllUsers() {
		findAll(true).forEach(user -> {
			System.out.println("----------------------------------");
			System.out.println(user.getId());
			System.out.println(user.getFullName());
			System.out.println();
		});
	}

	@Override
	public User findByLogin(String login) {
		User user = repos.findByLogin(login);
		Hibernate.initialize(user.getUserRoleList());
		return user;
	}

	@Override
	public void updatePassword(String password, Integer id) {
		cacheManager.getCache("cache1").clear();
		repos.updatePassword(password, id);
	}

	@Override
	@Scheduled(cron = "00 00 01 * * *")
	public void auditUserTable() {
		log.info("call auditUserTable @ " + new Date());
		emailService.send("salma.mouloudi.98@gmail.com", "Audit", "Number of users : " + repos.count());
	}

}
