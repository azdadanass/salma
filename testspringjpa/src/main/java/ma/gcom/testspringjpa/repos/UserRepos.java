package ma.gcom.testspringjpa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.gcom.testspringjpa.model.User;

@Repository
public interface UserRepos extends JpaRepository<User, Integer> {

	List<User> findByFirstName(String firstName);

	Long countByFirstName(String firstName);

	User findByLogin(String login);

	List<User> findByFirstNameAndLastName(String firstName, String lastName);

	//////////////////////////////////////////////////////////////////////////////////////////////////

	@Query("from User where firstName = ?1")
	List<User> findByFirstNameHql(String firstName);

	@Query("select new User(id,firstName,lastName) from User")
	List<User> findAllLight();

	@Query("select sum(weight) from User where firstName = ?1")
	Double findTotalWeightByFirstName(String firstName);

	@Modifying
	@Query("update User set lastLogin = current_date where id = ?1")
	void updateLastLogin(Integer id);

	@Modifying
	@Query("delete from  User where  firstName = ?1")
	void deleteByFirstName(String firstName);

	@Query("from User where firstName in (?1)")
	List<User> findByFirstNameList(List<String> firstName);

	@Modifying
	@Query("update User set password = ?1 where id = ?2 ")
	void updatePassword(String password, Integer id);

}
