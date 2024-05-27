package org.fundaciobit.pluginsib.core.test.v3;

import java.io.IOException;

import org.fundaciobit.pluginsib.core.v3.utils.Base64;
import org.junit.Assert;


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
