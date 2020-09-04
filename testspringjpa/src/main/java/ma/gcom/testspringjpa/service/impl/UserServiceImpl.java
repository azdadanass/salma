package ma.gcom.testspringjpa.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	EmailService emailService;

	@Override
	public List<User> findAll(Boolean light) {
		System.out.println("############################findAll");
		return light ? repos.findAllLight() : repos.findAll();
	}

	@Override
	public User save(User user) {
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
		return repos.findByLogin(login);
	}

	@Override
	public void updatePassword(String password, Integer id) {
		repos.updatePassword(password, id);
	}

	@Override
	@Scheduled(cron = "00 00 01 * * *")
	public void auditUserTable() {
		log.info("call auditUserTable @ " + new Date());
		emailService.send("salma.mouloudi.98@gmail.com", "Audit", "Number of users : " + repos.count());
	}

}
