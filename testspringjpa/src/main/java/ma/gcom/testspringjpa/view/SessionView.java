package ma.gcom.testspringjpa.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.UserService;

@ManagedBean
@Component
@Scope("session")
public class SessionView {

	@Autowired
	UserService userService;

	private User user;

	@PostConstruct
	public void init() {
		user = userService.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println("*******************************************");
		System.out.println(user.getFullName() + " is connected");
		System.out.println("*******************************************");
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
