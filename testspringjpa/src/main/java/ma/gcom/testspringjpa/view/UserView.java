package ma.gcom.testspringjpa.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.UserService;

@ManagedBean
@Component
@Scope("request")
public class UserView {

	@Autowired
	UserService service;

	private List<User> userList;
	private User user;

	@PostConstruct
	public void init() {
		System.out.println("UserView.init()");
		userList = service.findAll(false);
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
