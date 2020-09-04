package ma.gcom.testspringjpa.utiles;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class PasswordCrypter {
	
	
	public  static String CryptMd5(String password) 
			  throws NoSuchAlgorithmException {
			   
			    MessageDigest md = MessageDigest.getInstance("MD5");
			    md.update(password.getBytes());
			    byte[] digest = md.digest();
			    return(DatatypeConverter.printHexBinary(digest).toLowerCase());
			    
			        
			}

}
