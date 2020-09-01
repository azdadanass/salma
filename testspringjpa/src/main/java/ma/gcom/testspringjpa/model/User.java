package ma.gcom.testspringjpa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Table(name = "utilisateur")
public class User {

	private Integer id;
	private String login;
	private String password;
	private String firstName;
	private String lastName;
	private Date birthday;
	private Date lastLogin;
	private Double weight;
	private Boolean enabled;

	private List<Car> carList = new ArrayList<Car>();
	private List<UserRole> userRoleList = new ArrayList<UserRole>();

	public User() {
	}

	public User(Integer id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void addCar(Car car) {
		car.setUser(this);
		carList.add(car);
	}

	public void removeCar(Car car) {
		car.setUser(null);
		carList.remove(car);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

//	@Column(name = "prenom", nullable = false, unique = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Car> getCarList() {
		return carList;
	}

	public void setCarList(List<Car> carList) {
		this.carList = carList;
	}

	@Column(unique = true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthday=" + birthday + "]";
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserRole> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<UserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public boolean hasRole(String role) {
//		return userRoleList.stream().map(userRole -> userRole.getRole()).anyMatch(r -> r.equals(role));
		for (UserRole userRole : userRoleList) {
			if (role.equals(userRole.getRole())) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public boolean getIsAdmin() {
		return this.hasRole("ROLE_ADMIN");
	}

	@Transient
	public boolean getIsUser() {
		return this.hasRole("ROLE_USER");
	}

}
