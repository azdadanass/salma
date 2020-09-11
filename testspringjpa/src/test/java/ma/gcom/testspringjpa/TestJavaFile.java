package ma.gcom.testspringjpa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

class TestJavaFile {

	@Test
	void contextLoads() throws IOException {
		File in = new File("/home/azdad/Bureau/in.txt");
		if (!in.exists())
			in.createNewFile();
		InputStream is = new FileInputStream(in);
		byte[] buffer = new byte[is.available()];
		showBytes(buffer);
		is.read(buffer);
		showBytes(buffer);
		File out = new File("/home/azdad/Bureau/out.txt");
		OutputStream os = new FileOutputStream(out);
		os.write(buffer);
		os.write(buffer);
		os.write(buffer);
		os.close();
	}

	public void showBytes(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes)
			sb.append(String.format("%02X ", b));
		System.out.println(sb.toString());
	}

}
