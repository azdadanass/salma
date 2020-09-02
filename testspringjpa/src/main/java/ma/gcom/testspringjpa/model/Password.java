package ma.gcom.testspringjpa.model;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class Password {
	
	private String password;
	
	
	
	
	
	
	public Password(String password) {
		super();
		this.password = password;
	}

	public Password() {
		
		byte[] array = new byte[7]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
	    this.password=generatedString;
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
	
	public String CryptMd5() 
			  throws NoSuchAlgorithmException {
			   
			    MessageDigest md = MessageDigest.getInstance("MD5");
			    md.update(this.password.getBytes());
			    byte[] digest = md.digest();
			    return(DatatypeConverter.printHexBinary(digest).toUpperCase());
			    
			        
			}
	
	
}
