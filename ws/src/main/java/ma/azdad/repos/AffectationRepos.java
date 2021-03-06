package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Affectation;

@Repository
public interface AffectationRepos extends JpaRepository<Affectation, Integer> {

}
