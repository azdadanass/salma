package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Affectation;
import ma.azdad.repos.AffectationRepos;

@Component
@Transactional
public class AffectationService extends GenericService<Affectation, AffectationRepos> {

}