package ma.gcom.testspringjpa.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean
@Component
@Scope("session")
public class FileView {

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		System.out.println("handleFileUpload");
		FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		InputStream is = event.getFile().getInputstream();
		byte[] buffer = new byte[is.available()];
		is.read(buffer);

		String webappPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/");
		File targetFile = new File(webappPath + "files/" + event.getFile().getFileName());
		OutputStream os = new FileOutputStream(targetFile);
		os.write(buffer);
		os.close();

	}

}
