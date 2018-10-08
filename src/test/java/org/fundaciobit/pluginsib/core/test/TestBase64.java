package org.fundaciobit.pluginsib.core.test;

import java.io.IOException;

import org.fundaciobit.pluginsib.core.utils.Base64;

import junit.framework.Assert;

/**
 * 
 * @author anadal
 *
 */
public class TestBase64 {

  @org.junit.Test
  public void test() throws IOException {

    String text = "to be encoded";
    String textencoded = Base64.encode(text);
    byte[] bytesdecoded = Base64.decode(textencoded);

    Assert.assertEquals(text, new String(bytesdecoded));

  }

}
