package org.fundaciobit.pluginsib.core.test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataType;

import junit.framework.Assert;

/**
 * 
 * @author anadal
 * 
 */
public class TestMetadata  {

  @org.junit.Test
  public void test() {

    System.out.println();
    
    
    try {
    	 Metadata.checkMetadata(null);
    	 Assert.fail("S'esperava que llancés una excepció.");
    	 
	} catch (Exception e) {
		// TODO: handle exception
	}
   
    
    //Valor sencer no correcte
    Metadata m1 = new Metadata("clau", 33);
    Assert.assertEquals("33", m1.getValue());

    //boolean
    Metadata m2 = new Metadata("clau2", true);
    Assert.assertEquals("true", m2.getValue());
    
    //boolean
    Metadata m3 = new Metadata("clau3", false);
    Assert.assertEquals("false", m3.getValue()); 
  
    Metadata m4 = new Metadata("clau4", Math.pow(5, 8));
    Assert.assertEquals("390625.0", m4.getValue()); 
    
    Metadata m5 = new Metadata("clau5", 0.0000000053);
    Assert.assertEquals("5.3E-9", m5.getValue());
    
    
    
  
  }
  
  
  @SuppressWarnings("unchecked")
public static void main(String[] args) {
    
    try {
      
      HashMap<String, ArrayList<Metadata>> all = new HashMap<String, ArrayList<Metadata>>();

      
      
      ArrayList<Metadata> k1 = new ArrayList<Metadata>();
      k1.add(new Metadata("k1", "value11", MetadataType.STRING));
      k1.add(new Metadata("k1", "value12", MetadataType.STRING));
      
      all.put("k1", k1);
      
      
      ArrayList<Metadata> k2 = new ArrayList<Metadata>();
      k2.add(new Metadata("k2", "12", MetadataType.INTEGER));
      
      all.put("k2", k2); 
      
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
      {
      java.beans.XMLEncoder xmlEnc = new XMLEncoder(baos);
      
      xmlEnc.writeObject(all);
      
      xmlEnc.close();
      }
      
      System.out.println(new String(baos.toByteArray()));
      
      
      XMLDecoder dec = new XMLDecoder(new ByteArrayInputStream(baos.toByteArray()));

      
      HashMap<String, ArrayList<Metadata>> all2 = (HashMap<String, ArrayList<Metadata>>)dec.readObject();
      
      System.out.println(all2.size());
      
      
      dec.close();
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
  }


}
