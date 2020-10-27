package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Role;
import ma.azdad.model.User;
import ma.azdad.model.UserFile;
import ma.azdad.model.UserHistory;
import ma.azdad.service.CacheService;
import ma.azdad.service.EmailService;
import ma.azdad.service.SmsService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;
import ma.azdad.utils.PasswordGenerator;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class UserView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionView sessionView;

	@Autowired
	private CacheService cacheService;

	@Autowired
	protected UserService service;

	@Autowired
	protected EmailService emailService;
	@Autowired
	protected SmsService smsService;

	protected List<User> list1 = new ArrayList<>();
	protected List<User> list2 = new ArrayList<>();
	protected List<User> list3;
	protected List<User> list4;

	protected User model;
	protected User old;
	protected String currentPath;

	protected String searchBean = "";

	protected String listPage = "userList.xhtml";
	protected String addEditPage = "addEditUser.xhtml";
	protected String viewPage = "viewUser.xhtml";
	protected long start;
	protected String username;

	protected Boolean isViewPage = false;
	protected Boolean isAddPage = false;
	protected Boolean isEditPage = false;
	protected Boolean isListPage = false;

	private Integer pageIndex;
	private Boolean warning = true;
	private Boolean internal;

	@PostConstruct
	public void init() {
		log.info("init " + getClass().getSimpleName());
		cacheEvictView();
		start();
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		initParameters();

		if (("/" + listPage).equals(currentPath))
			isListPage = true;
		else if (("/" + addEditPage).equals(currentPath) && username == null) {
			isAddPage = true;
			addPage();
		} else if (("/" + addEditPage).equals(currentPath) && username != null) {
			isEditPage = true;
			editPage();
		} else if (("/" + viewPage).equals(currentPath)) {
			isViewPage = true;
			viewPage();
		}

		refreshList();
	}

	public void redirect() {
		if (canAccess())
			return;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getRequestContextPath() + "ad.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Boolean canAccess() {
		return sessionView.getIsAdmin() || (sessionView.getIsUser() && sessionView.isTheConnectedUser(model));
	}

	public void initLists(List<User> list) {
		list2 = list1 = list;
	}

	public void refreshModel(User user) {
		model = service.findOne(user.getUsername());
	}

	public void refreshList() {
		// TODO to remove
		service.updateContractActive();

		if (isListPage)
			initLists(service.find(internal));
		else if (isViewPage)
			initLists(service.findByLineManager(model.getUsername()));
	}

	protected void addPage() {
		model = new User();
		model.toggleRole(Role.ROLE_APPS);
		model.toggleRole(Role.ROLE_IADMIN);
	}

	protected void editPage() {
		model = service.findOne(username);
		cacheEvict();
		old = service.findOne(username);
		System.err.println(model == old);
	}

	protected void viewPage() {
		if (username == null)
			username = sessionView.getUsername();
		model = service.findOne(username);
	}

	protected void filterBean(String query) {
		list3 = null;
		List<User> list = new ArrayList<>();
		query = query.toLowerCase().trim();
		for (User bean : list1) {
			if (bean.filter(query))
				list.add(bean);
		}
		list2 = list;
	}

	public Integer getRowsNumber() {
		if (list3 != null)
			return list3.size();
		else
			return list2.size();
	}

	public Integer getSelectedRowsNumber() {
		if (list4 != null)
			return list4.size();
		return 0;
	}

	public String addParameters(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public void execJavascript(String script) {
		RequestContext.getCurrentInstance().execute(script);
	}

	public String cacheEvict() {
		cacheEvictService();
		cacheEvictView();
		return addParameters(currentPath, "faces-redirect=true", "username=" + username);
	}

	public void cacheEvictService() {
		service.cacheEvict();
	}

	protected void initParameters() {
		username = UtilsFunctions.getParameter("username");
		pageIndex = UtilsFunctions.getIntegerParameter("pageIndex");
	}

	public void cacheEvictView() {
		cacheService.evictCachePrefix("userView");
	}

	public void start() {
		start = System.currentTimeMillis();
	}

	public void time() {
		log.info("time: " + Long.toString(System.currentTimeMillis() - start) + " ms");
	}

	// adv search
	public void initSearch() {
		model = new User();
		model.setInternal(null);
		model.setActive(null);
		model.setContractActive(null);
	}

	public void search() {
		if (model.getLogin() != null)
			model.setLogin("%" + model.getLogin() + "%");
		if (model.getFirstName() != null)
			model.setFirstName("%" + model.getFirstName() + "%");
		if (model.getLastName() != null)
			model.setLastName("%" + model.getLastName() + "%");

		System.err.println(model.getLogin());
		System.err.println(model.getFirstName());
		System.err.println(model.getLastName());
		System.err.println(model.getActive());
		System.err.println(model.getContractActive());
		System.err.println(model.getInternal());

		initLists(service.findLight(model));

		System.out.println(list2);
	}

	// toggle status
	public Boolean canToggle() {
		return true;
	}

	public void toggle() {
		if (!canToggle())
			return;

		if (!model.getActive() && model.getCin() != null && !model.getCin().isEmpty())
			service.updateActiveByCin(false, model.getCin());
		model.setActive(!model.getActive());
		if (model.getActive() && service.countByEmailAndActive(model) > 0) {
			FacesContextMessages.ErrorMessages("Email address should be unique per active account");
			return;
		}
		model = service.saveAndRefresh(model);
		if (isListPage)
			refreshList();
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		if (isAddPage)
			generateUsername();
		model.setFullName(model.getFirstName() + " " + model.getLastName());
		model.addHistory(new UserHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), null));

		model.getAffectation().setLob(lobService.findOneLight(model.getAffectation().getLobId()));
		model.getAffectation().setLineManager(service.findOneLight(model.getAffectation().getLineManagerUsername()));
		model.getAffectation().setHrManager(service.findOneLight(model.getAffectation().getHrManagerUsername()));
		model.getAffectation().setLogisticManager(service.findOneLight(model.getAffectation().getLogisticManagerUsername()));

		model = service.save(model);

		if (isEditPage) {
			if (!model.getAffectation().getLob().equals(old.getAffectation().getLob()))
				emailService.sendLobChangedNotification(model);
			if (!model.getAffectation().getLineManager().equals(old.getAffectation().getLineManager()))
				emailService.sendLmChangedNotification(model);
		}

		return addParameters(viewPage, "faces-redirect=true", "username=" + model.getUsername());
	}

	public Boolean validate() {
		if (service.countByLogin(model.getLogin(), model.getUsername()) > 0)
			return FacesContextMessages.ErrorMessages("login should be unique");
		if (warning && service.countByFirstNameAndLastName(model) > 0) {
			execJavascript("PF('confirmSaveDlg').show()");
			warning = false;
			return false;
		}
		if (model.getActive() && service.countByEmailAndActive(model) > 0)
			return FacesContextMessages.ErrorMessages("Email address should be unique per active account");

		return true;
	}

	public void formatFirstName() {
		model.setFirstName(UtilsFunctions.formatName(model.getFirstName()));
	}

	public void formatLastName() {
		model.setLastName(UtilsFunctions.formatName(model.getLastName()));
	}

	public void formatCin() {
		model.setCin(UtilsFunctions.cleanString(model.getCin()).replace(" ", "").toUpperCase());
	}

	public void formatEmail() {
		model.setEmail(UtilsFunctions.cleanString(model.getEmail()).replace(" ", "").toLowerCase());
	}

	public void formatEmail2() {
		model.setEmail2(UtilsFunctions.cleanString(model.getEmail2()).replace(" ", "").toLowerCase());
	}

	public void formatPhone() {
		model.setPhone(UtilsFunctions.cleanString(model.getPhone().replace(" ", "")));
	}

	public void generateUsername() {
		formatFirstName();
		formatLastName();
		String usernameSuffix = "." + (model.getLastName() == null ? "" : model.getLastName().replace(" ", ""));
		String usernamePrefix = "";
		if (model.getFirstName() != null)
			for (Character character : model.getFirstName().replace(" ", "").toCharArray()) {
				usernamePrefix += character;
				String username = usernamePrefix.toLowerCase() + usernameSuffix.toLowerCase();
				if (service.countByUsername(username) == 0 && service.countByLogin(username) == 0) {
					model.setUsername(username);
					model.setLogin(username);
					return;
				}

			}
	}

	// files
	private UserFile file;
	private String fileType;

	public Boolean canAddFile() {
		return true;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileView.handleFileUpload(event, "user");
		UserFile modelFile = new UserFile("user", file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (UserView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return sessionView.getIsAdmin();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	// photos
	public Boolean canUploadPhoto() {
		return true;
	}

	public void handlePhotoUpload(FileUploadEvent event) throws IOException {
		File file = fileView.handlePhotoUpload(event, "user", String.valueOf(model.getUsername()));
		model.setPhoto("user" + "/" + file.getName());
		synchronized (UserView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// can update in place
	public Boolean canUpdateRole() {
		return sessionView.getIsAdmin();
	}

	public void updateRole() {
		if (canUpdateRole())
			model = service.saveAndRefresh(model);
	}

	public Boolean canToggleRole() {
		return sessionView.getIsAdmin();
	}

	public void toggleRole(Role role) {
		if (!canToggleRole())
			return;
		model.toggleRole(role);
		model.getRoleList().forEach(i -> System.out.println(i.getRole()));
	}

	// delete
	public Boolean canDelete() {
		return sessionView.getIsAdmin();
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	// reset password
	public Boolean canChangePassword() {
		return sessionView.getIsAdmin();
	}

	public void changePassword() throws IOException {
		if (!canChangePassword())
			return;
		String password = PasswordGenerator.generateRandomPassword(6);
		model.setPassword(UtilsFunctions.stringToMD5(password));
		model = service.saveAndRefresh(model);
		smsService.sendSms(model.getPhone(), "Dear " + model.getFullName() + ", Your password has been reset by the administrator. You temporary password has been sent to your registered email address.");
		smsService.sendSms(model.getPhone(), "here is your new 3gcom account password : " + password);
	}

	// generic

	@Cacheable("userView.findLightByStatus")
	public List<User> findLightByStatus(Boolean active) {
		return service.findLightByStatus(active);
	}

	@Cacheable("userView.findLight")
	public List<User> findLightActive(Boolean internal) {
		return service.findLight(internal, true);
	}

	@Cacheable("userView.findLightActive")
	public List<User> findLightActive() {
		return findLightByStatus(true);
	}

	@Cacheable("userView.findLight")
	public List<User> findLight() {
		return service.findLight();
	}

	@Cacheable("userView.findLightByInternalAndActive")
	public List<User> findLightByInternalAndActive() {
		return service.findLightByInternalAndActive();
	}

	// getters & setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public CacheService getCacheService() {
		return cacheService;
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	public UserService getService() {
		return service;
	}

	public void setService(UserService service) {
		this.service = service;
	}

	public List<User> getList1() {
		return list1;
	}

	public void setList1(List<User> list1) {
		this.list1 = list1;
	}

	public List<User> getList2() {
		return list2;
	}

	public void setList2(List<User> list2) {
		this.list2 = list2;
	}

	public List<User> getList3() {
		return list3;
	}

	public void setList3(List<User> list3) {
		this.list3 = list3;
	}

	public List<User> getList4() {
		return list4;
	}

	public void setList4(List<User> list4) {
		this.list4 = list4;
	}

	public User getModel() {
		return model;
	}

	public void setModel(User model) {
		this.model = model;
	}

	public User getOld() {
		return old;
	}

	public void setOld(User old) {
		this.old = old;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public String getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
		filterBean(searchBean);
	}

	public String getListPage() {
		return listPage;
	}

	public void setListPage(String listPage) {
		this.listPage = listPage;
	}

	public String getAddEditPage() {
		return addEditPage;
	}

	public void setAddEditPage(String addEditPage) {
		this.addEditPage = addEditPage;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public Boolean getIsViewPage() {
		return isViewPage;
	}

	public void setIsViewPage(Boolean isViewPage) {
		this.isViewPage = isViewPage;
	}

	public Boolean getIsAddPage() {
		return isAddPage;
	}

	public void setIsAddPage(Boolean isAddPage) {
		this.isAddPage = isAddPage;
	}

	public Boolean getIsEditPage() {
		return isEditPage;
	}

	public void setIsEditPage(Boolean isEditPage) {
		this.isEditPage = isEditPage;
	}

	public Boolean getIsListPage() {
		return isListPage;
	}

	public void setIsListPage(Boolean isListPage) {
		this.isListPage = isListPage;
	}

	public Logger getLog() {
		return log;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

}
