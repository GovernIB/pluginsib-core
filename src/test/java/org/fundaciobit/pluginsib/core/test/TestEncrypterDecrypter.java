package org.fundaciobit.pluginsib.core.test;


import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.fundaciobit.pluginsib.core.utils.EncrypterDecrypter;

import junit.framework.Assert;

public class TestEncrypterDecrypter {

	
	
	
	@org.junit.Test
	public void test() throws Exception {
		
		String stringclau = "clauclau";
		byte[] byteclau = stringclau.getBytes();
		Key clau = generateKey(byteclau, EncrypterDecrypter.ALGORITHM_DES);
		
		final String data = "to be encrypted";
		
		
		String encrypted = EncrypterDecrypter.encrypt(EncrypterDecrypter.ALGORITHM_DES, clau.getEncoded(), data);
		String decrypted = EncrypterDecrypter.decrypt(EncrypterDecrypter.ALGORITHM_DES, clau.getEncoded(), encrypted);
		Assert.assertEquals(data, decrypted);
		
		
	}
	
	
	public static Key generateKey(byte[] keyValue, String algorithm) throws Exception {
	    Key key = new SecretKeySpec(keyValue, algorithm);
	    return key;
	  }
}
