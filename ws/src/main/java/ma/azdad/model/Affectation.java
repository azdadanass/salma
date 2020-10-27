package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity

public class Affectation extends GenericBean {

	private User user;
	private User hrManager;
	private User lineManager;
	private User logisticManager;

	public Affectation() {
	}

	public Affectation(User user) {
		this.user = user;
	}

	@Transient
	public String getLineManagerUsername() {
		return lineManager == null ? null : lineManager.getUsername();
	}

	@Transient
	public void setLineManagerUsername(String lineManagerUsername) {
		if (lineManager == null || !lineManagerUsername.equals(lineManager.getUsername()))
			lineManager = new User();
		lineManager.setUsername(lineManagerUsername);
	}

	@Transient
	public String getLineManagerFullName() {
		return lineManager == null ? null : lineManager.getFullName();
	}

	@Transient
	public void setLineManagerFullName(String lineManagerFullName) {
		if (lineManager == null)
			lineManager = new User();
		lineManager.setFullName(lineManagerFullName);
	}

	@Transient
	public String getHrManagerUsername() {
		return hrManager == null ? null : hrManager.getUsername();
	}

	@Transient
	public void setHrManagerUsername(String hrManagerUsername) {
		if (hrManager == null || !hrManagerUsername.equals(hrManager.getUsername()))
			hrManager = new User();
		hrManager.setUsername(hrManagerUsername);
	}

	@Transient
	public String getHrManagerFullName() {
		return hrManager == null ? null : hrManager.getFullName();
	}

	@Transient
	public void setHrManagerFullName(String hrManagerFullName) {
		if (hrManager == null)
			hrManager = new User();
		hrManager.setFullName(hrManagerFullName);
	}

	@Transient
	public String getLogisticManagerUsername() {
		return logisticManager == null ? null : logisticManager.getUsername();
	}

	@Transient
	public void setLogisticManagerUsername(String logisticManagerUsername) {
		if (logisticManager == null || !logisticManagerUsername.equals(logisticManager.getUsername()))
			logisticManager = new User();
		logisticManager.setUsername(logisticManagerUsername);
	}

	@Transient
	public String getLogisticManagerFullName() {
		return logisticManager == null ? null : logisticManager.getFullName();
	}

	@Transient
	public void setLogisticManagerFullName(String logisticManagerFullName) {
		if (logisticManager == null)
			logisticManager = new User();
		logisticManager.setFullName(logisticManagerFullName);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idaffectation", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "users_hr")
	public User getHrManager() {
		return hrManager;
	}

	public void setHrManager(User hrManager) {
		this.hrManager = hrManager;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "users_lm")
	public User getLineManager() {
		return lineManager;
	}

	public void setLineManager(User lineManager) {
		this.lineManager = lineManager;
	}

	@OneToOne
	@JoinColumn(name = "users_username")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "users_logistic_manager")
	public User getLogisticManager() {
		return logisticManager;
	}

	public void setLogisticManager(User logisticManager) {
		this.logisticManager = logisticManager;
	}

}
