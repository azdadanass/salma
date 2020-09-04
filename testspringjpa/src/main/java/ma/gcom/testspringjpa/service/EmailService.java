package ma.gcom.testspringjpa.service;

public interface EmailService {

	public void sendTestEmail();

	public void sendAllEmail();

	public void sendOneEmail(Integer id, String passwordClair);

	public void send(String to, String subject, String text);

}
