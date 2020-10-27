package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.User;

@Repository
public interface UserRepos extends JpaRepository<User, String> {

}
