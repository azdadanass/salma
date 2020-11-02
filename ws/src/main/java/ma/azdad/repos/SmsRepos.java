package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Sms;

@Repository
public interface SmsRepos extends JpaRepository<Sms, Integer> {

}
