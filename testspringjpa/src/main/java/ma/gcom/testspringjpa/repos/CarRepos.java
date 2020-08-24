package ma.gcom.testspringjpa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.gcom.testspringjpa.model.Car;

@Repository
public interface CarRepos extends JpaRepository<Car, Integer> {

	@Query("from Car where user.firstName = ?1")
	List<Car> findByUserFirstName(String userFirstName);

	@Query("from Car c,User u where c.user.id = u.id and u.firstName = ?1")
	List<Car> findByUserFirstName2(String userFirstName);

	@Query("select c.matricule from Car c")
	List<String> findMatriculeList();

}
