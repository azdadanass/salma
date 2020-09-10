package ma.gcom.testspringjpa.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.UserService;

@ManagedBean
@Component
@Scope("view")
public class UserView {

	@Autowired
	UserService service;

	@Autowired
	SessionView sessionView;

	private List<User> userList;
	private User user = new User();

	@PostConstruct
	public void init() {
		System.out.println("UserView.init()");

		String page = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		String idStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		switch (page) {
		case "/userList.xhtml":
			userList = service.findAll(false);
			break;
		case "/user.xhtml":
			if (idStr != null)
				user = service.findOne(Integer.valueOf(idStr));
			break;
		case "/userForm.xhtml":
			if (idStr != null)
				user = service.findOne(Integer.valueOf(idStr));
			break;
		default:
			break;
		}

	}

	public void showThis() {
		System.out.println(this);
	}

	public String save() {
		if (user.getEmail() == null || !user.getEmail().matches("[a-zA-Z0-9]+@[a-z]+(.)[a-z]+")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email not correct", null));
			return null;
		}
		user = service.save(user);
		return "user.xhtml?faces-redirect=true&id=" + user.getId();
	}

	public String delete(Integer id) {
		service.delete(id);
		return "userList.xhtml?faces-redirect=true";
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
