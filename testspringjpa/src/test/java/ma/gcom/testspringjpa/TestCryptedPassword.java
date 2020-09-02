package ma.gcom.testspringjpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;




@SuppressWarnings("deprecation")
public class TestCryptedPassword {
	
	
	//@Test
	public void testSpringEncoder() {
	
	
	    String hashedPass = new MessageDigestPasswordEncoder("MD5").encode("koala");
	  

	    assertEquals("a564de63c2d0da68cf47586ee05984d7", hashedPass);
	}
	
	@Test
	public void CryptMd5() 
	  throws NoSuchAlgorithmException {
	    String hash = "35454B055CC325EA1AF2126E27707052";
	    String password = "ILoveJava";
	        
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(password.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter
	      .printHexBinary(digest).toUpperCase();
	    
	    assertEquals(hash,myHash );
	        
	}

}
