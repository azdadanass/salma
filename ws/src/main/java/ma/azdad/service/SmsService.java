package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Sms;
import ma.azdad.repos.SmsRepos;

@Component
public class SmsService extends GenericService<Sms, SmsRepos> {

	@Override
	@Cacheable("smsService.findAll")
	public List<Sms> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("smsService.findOne")
	public Sms findOne(Integer id) {
		Sms sms = super.findOne(id);

		return sms;
	}

}
