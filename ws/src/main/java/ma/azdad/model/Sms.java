package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Sms extends GenericBean {

	private String message;
	private User user;

	@Override
	public boolean filter(String query) {
		return contains(message, query);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	@Transient
	public String getUserFullName() {
		return user != null ? user.getFullName() : null;
	}

	@Transient
	public String getUserPhone() {
		return user != null ? user.getPhone() : null;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
