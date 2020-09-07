package ma.gcom.testspringjpa.view;

import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean
@Component
@Scope("session")
public class TestView {

	private String test1 = "aaaaaaaaaaaaaaaaa";

	public void testVoid() {
		System.out.println("testVoid");
	}

	public String testString() {
		return "testString";
	}

	public String getTest1() {
		return test1;
	}

	public void setTest1(String test1) {
		this.test1 = test1;
	}

}
