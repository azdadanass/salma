package ma.gcom.testspringjpa.view;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.gcom.testspringjpa.model.User;
import ma.gcom.testspringjpa.service.UserService;

@ManagedBean
@Component
@Scope("session")
public class TestView {

	@Autowired
	UserService userService;

	private String test1 = "aaaaaaaaaaaaaaaaa";

	public User findUser(Integer id) {
		return userService.findOne(id);
	}

	public void testVoid() {
		System.out.println("testVoid");
	}

	public String testString() {
		System.out.println("exec testString");
		return "testString";
	}

	public String getTest1() {
		return test1;
	}

	public void setTest1(String test1) {
		this.test1 = test1;
	}

}
