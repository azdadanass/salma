package ma.gcom.testspringjpa.service.impl;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.EmailService;
import ma.gcom.testspringjpa.service.UserService;

@Component
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired 
	UserService userService;

	@Override
	public void sendTestEmail() {
		mailSender.send(new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				message.setFrom("gcomappstest@gmail.com", "AA");
				message.setTo("a.azdad@3gcom-int.com");
				message.setSubject("Subject");
				message.setText("Message", true);
				String[] cc = { "salma.mouloudi.98@gmail.com", "azdadanass@gmail.com" };
				message.setCc(cc);
			}
		});

	}
	
	@Override
	public void sendAllEmail() {
		mailSender.send(new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				message.setFrom("gcomappstest@gmail.com", "AA");
				for (User user : userService.findAll(true)) {
					
					if(user.getEmail() != null) {
						
						message.setTo(user.getEmail());
						message.setSubject("Subject");
						message.setText("Message", true);
						
					}
					
					
				} 
				
			}
		});

	}

	@Override
	public void sendOneEmail(Integer id,String password) {
		
		mailSender.send(new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				User user = userService.findOne(id);
				message.setFrom("testspringboot1", "testSpringMail");
				message.setTo(user.getEmail());
				message.setSubject("Subject");
				message.setText("Bonjour M."+user.getLastName()+" votre email:"+user.getEmail()+" votre mot de passe: " + password, true);
				System.out.println(user.getPassword());
				
			}
		});

		
	}
	
	

}
