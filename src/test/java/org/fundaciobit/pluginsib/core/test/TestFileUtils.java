package org.fundaciobit.pluginsib.core.test;



import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.fundaciobit.pluginsib.core.utils.FileUtils;

import junit.framework.Assert;




public class TestFileUtils {
	
	
	@org.junit.Test
	public void deleteRecursiveTest() {

		File d1 = new File("dir1");
		d1.mkdirs();

		@SuppressWarnings("unused")
		File d2 = new File(d1,"dir2");

		FileUtils.deleteRecursive(d1);
		Assert.assertFalse(d1.exists());

	}
	
	@org.junit.Test
	public void readFromFileTest() throws Exception {
		File file1 = new File("file1");
		try {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file1));
		String data = "To be written in a file";
		writer.write(data);

		writer.close();

		byte[] readbytes = FileUtils.readFromFile(file1);
		String read = new String(readbytes);
		Assert.assertEquals(data, read);
		
		} finally {
			if(file1.exists()) {
				file1.delete();
			}
		}
		
	}


	@org.junit.Test
	public void readResourceTest() throws IOException {
		
	    InputStream in = FileUtils.readResource(TestFileUtils.class, "log4j.properties");
	    Assert.assertNotNull(in);
	    byte[] bytes = FileUtils.toByteArray(in);
	    int longi = 841;
	    
	    Assert.assertEquals(longi, bytes.length);
	    
	}
	
	
	

	@org.junit.Test
	public void getResourceAsURLTest() {
		URL url = FileUtils.getResourceAsURL(TestFileUtils.class, "log4j.properties");
		Assert.assertNotNull(url);
		
	}

	@org.junit.Test	
	public void toByteArrayTest() throws IOException {
		String str = "to be converted";
		byte[] bytes = str.getBytes();
		InputStream in = new ByteArrayInputStream(bytes);
		byte[] finalbytes = FileUtils.toByteArray(in);
		
		Assert.assertEquals(str, new String(finalbytes));
	}



}
