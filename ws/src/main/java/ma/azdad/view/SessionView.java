package ma.azdad.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ma.azdad.model.User;
import ma.azdad.service.UserService;

@ManagedBean
@Component
@Scope("session")

public class SessionView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected UserService userService;

	private String username;
	private User user;

	@PostConstruct
	public void init() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		username = auth.getName().toLowerCase().trim();
		user = userService.findOne(username);
		log.info("init SessionView !");
		log.info(username + " is connected");
	}

	public Boolean isTheConnectedUser(String username) {
		return this.username.equalsIgnoreCase(username);
	}

	public Boolean isTheConnectedUser(String... usernameTab) {
		for (int i = 0; i < usernameTab.length; i++)
			if (this.username.equalsIgnoreCase(usernameTab[i]))
				return true;
		return false;
	}

	public Boolean isTheConnectedUser(User user) {
		return isTheConnectedUser(user.getUsername());
	}

	public Boolean getIsUser() {
		return user.getIsUser();
	}

	public Boolean getIsAdmin() {
		return user.getIsAdmin();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}