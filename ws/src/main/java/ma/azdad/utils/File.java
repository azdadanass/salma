package ma.azdad.utils;

import java.util.Date;

import ma.azdad.model.User;
import ma.azdad.service.UtilsFunctions;

public class File {

	private String parentType;
	private String parentReference;
	private Integer id;
	private Date date;
	private String link = "noimage.jpg";
	private String extension;
	private String type;
	private String size;
	private String name;
	private User user;

	public File(String parentType, String parentReference, Integer id, Date date, String link, String extension, String type, String size, String name, User user) {
		super();
		this.parentType = parentType;
		this.parentReference = parentReference;
		this.id = id;
		this.date = date;
		this.link = link;
		this.extension = extension;
		this.type = type;
		this.size = size;
		this.name = name;
		this.user = user;
	}

	public String getParentTypeStyleClass() {
		switch (parentType) {
		case "Po":
			return "badge badge-success";
		case "Acceptance":
			return "badge badge-info";
		case "Invoice":
			return "badge badge-warning";
		case "Payment":
			return "badge badge-purple";
		default:
			return null;
		}
	}

	public Boolean getIsImage() {
		return UtilsFunctions.contains(extension, "png", "jpg", "jpeg", "gif", "bmp");
	}

	public Boolean getIsPdf() {
		return "pdf".equals(extension);
	}

	public String getParentType() {
		return parentType;
	}

	public String getParentReference() {
		return parentReference;
	}

	public Integer getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getLink() {
		return link;
	}

	public String getExtension() {
		return extension;
	}

	public String getType() {
		return type;
	}

	public String getSize() {
		return size;
	}

	public String getName() {
		return name;
	}

	public User getUser() {
		return user;
	}

}
