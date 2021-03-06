package ma.gcom.testspringjpa.service;

import java.util.List;

import ma.gcom.testspringjpa.model.User;

public interface UserService {

	List<User> findAll(Boolean light);

	User save(User user);

	User findOne(Integer id);

	List<User> findByFirstName(String firstName);

	Long countByFirstName(String firstName);

	Boolean delete(Integer id);

	Boolean delete(User user);

	void showAllUsers();

	User findByLogin(String login);

	void updatePassword(String password, Integer id);

	void auditUserTable();

}
