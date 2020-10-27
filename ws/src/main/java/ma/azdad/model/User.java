package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private Boolean internal = true;
	private String username;
	private String login;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private String email2;
	private String password;
	private String photo = "none.png";
	private String phone;
	private String phone2;
	private String job;
	private String cin;
	private Boolean gender = true;
	private Boolean active = true;
	private Boolean contractActive;
	private Date birthday;

	private Affectation affectation = new Affectation(this);
	private CompanyType companyType;
	private Customer customer;
	private Supplier supplier;
	private String company;

	private User user;
	private Date date;

	private List<UserRole> roleList = new ArrayList<>();

	private User lineManager;

	public User() {
	}

	public boolean filter(String query) {
		return contains(fullName, query) || contains(job, query) || contains(cin, query) || contains(company, query);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	protected Boolean contains(String string, String query) {
		return string != null && string.toLowerCase().contains(query);
	}

	protected Boolean contains(Double d, String query) {
		return d != null && String.valueOf(d).toLowerCase().contains(query);
	}

	protected Boolean contains(Date date, String query) {
		return date != null && UtilsFunctions.getFormattedDate(date).toLowerCase().contains(query);
	}

	@Transient
	public String getCompanyName() {
		if (companyType == null)
			return null;
		switch (companyType) {
		case CUSTOMER:
			return customer.getName();
		case SUPPLIER:
			return supplier.getName();
		case OTHER:
			return company;
		default:
			return null;
		}
	}

	@Transient
	public Integer getCustomerId() {
		return customer == null ? null : customer.getId();
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		if (customer == null || !customerId.equals(customer.getId()))
			customer = new Customer();
		customer.setId(customerId);
	}

	@Transient
	public Integer getSupplierId() {
		return supplier == null ? null : supplier.getId();
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		if (supplier == null || !supplierId.equals(supplier.getId()))
			supplier = new Supplier();
		supplier.setId(supplierId);
	}

	@Transient
	public void setCompany(Customer customer, Supplier supplier, String company) {
		switch (companyType) {
		case CUSTOMER:
			this.customer = customer;
			this.supplier = null;
			this.company = null;
			break;
		case SUPPLIER:
			this.customer = null;
			this.supplier = supplier;
			this.company = null;
			break;
		case OTHER:
			this.customer = null;
			this.supplier = null;
			this.company = company;
		}
	}

	public void addRole(UserRole role) {
		if (roleList.contains(role))
			return;
		role.setUser(this);
		roleList.add(role);
	}

	public void removeRole(UserRole role) {
		roleList.remove(role);
		role.setUser(null);
	}

	public void toggleRole(Role role) {
		if (hasRole(role)) {
			roleList.stream().filter(i -> i.getRole().equals(role)).forEach(i -> i.setUser(null));
			roleList.removeIf(i -> i.getRole().equals(role));
		} else {
			if (role.getRole().startsWith("MR_")) {
				roleList.stream().filter(i -> i.getRole().getRole().startsWith("MR_")).forEach(i -> i.setUser(null));
				roleList.removeIf(i -> i.getRole().getRole().startsWith("MR_"));
			}
			addRole(new UserRole(role));
		}
	}

	@Transient
	public Boolean hasRole(Role role) {
		return roleList.stream().filter(i -> i.getRole().equals(role)).count() > 0;
	}

	@Transient
	public Boolean getIsUser() {
		return hasRole(Role.ROLE_IADMIN);
	}

	@Transient
	public Boolean getIsAdmin() {
		return hasRole(Role.ROLE_IADMIN_ADMIN);
	}

	@Id
	@Column(nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "info")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public String getName() {
		return fullName;
	}

	@Column(name = "fullname")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "id_photo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<UserRole> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Column(unique = true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	@Column(name = "info2")
	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	@Enumerated
	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "firstname")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "lastname")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getContractActive() {
		return contractActive;
	}

	public void setContractActive(Boolean contractActive) {
		this.contractActive = contractActive;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public Affectation getAffectation() {
		return affectation;
	}

	public void setAffectation(Affectation affectation) {
		this.affectation = affectation;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

	@Transient
	public User getLineManager() {
		return lineManager;
	}

	@Transient
	public void setLineManager(User lineManager) {
		this.lineManager = lineManager;
	}

}
