package ma.azdad.service;

import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SmsService {
	protected final Logger log = LoggerFactory.getLogger(SmsService.class);

	@Value("${utilsIpAddress}")
	private String utilsIpAddress;

	@Async
	public void sendSms(String numero, String message) {
		try {
			String url = "http://" + utilsIpAddress + "/sendSms";
			HashMap<String, String> params = new HashMap<>();
			params.put("numero", numero);
			params.put("message", message);
			UtilsFunctions.sendHttpRequest("POST", url, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	public void sendSms(Set<String> numeroSet, String message) {
		if (numeroSet != null)
			for (String numero : numeroSet)
				sendSms(numero, message);
	}

}
