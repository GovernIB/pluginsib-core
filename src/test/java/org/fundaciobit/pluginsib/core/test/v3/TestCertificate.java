package org.fundaciobit.pluginsib.core.test.v3;

import org.fundaciobit.pluginsib.core.v3.test.TestUtils;


/**
 * 
 * @author anadal
 *
 */
public class TestCertificate extends TestUtils {
/*
  public static Logger log = Logger.getLogger(TestCertificate.class);

  public static boolean debug = false;

  public static String[] getCertificates() {
    String certsStr = getProperty("certificates");
    if (certsStr == null || certsStr.trim().length() == 0) {
      return new String[0];
    } else {
      return certsStr.split(",");
    }

  }

  public void testCertificate() {

    String[] certs = getCertificates();

    for (String certNumber : certs) {
      log.info(" ---- " + certNumber + " -------");

      String certInfo = getProperty("cert." + certNumber);

      String[] fields = certInfo.split("\\|");

      String filePath = fields[0];
      String passwordks = fields[1];
      String type = fields[2];

      // String passwordcert = passwordks;

      // log.info("filePath: " + filePath);
      // log.info("passwordks: " + passwordks);

      // Stirng alias
      // Stirng password
      try {

        // log.info("XXXXXXXXX = " + KeyStore.getDefaultType());

        X509Certificate certificate1;

        if ("pkcs12".equals(type)) {

          List<Certificate> cc = CertificateUtils.readCertificatesOfKeystore(
              new FileInputStream(new File(filePath)), passwordks);
          if (cc == null || cc.size() == 0) {
            throw new Exception("Certificat " + certNumber + " Esta buit.");
          }
          certificate1 = (X509Certificate) cc.get(0);

        } else {
          InputStream certstream = new FileInputStream(new File(filePath));
          certificate1 = CertificateUtils.decodeCertificate(certstream);


        }
        if (debug == true) {
          log.info("Certificate: " + certificate1);

          log.info("Subject Name DN: " + certificate1.getSubjectDN().getName());
          log.info("Subject Name: " + CertificateUtils.getCN(certificate1));
          log.info("Emissior Name DN: " + certificate1.getIssuerDN().toString());
          log.info("Emissior Name: "
              + CertificateUtils.getCN(certificate1.getIssuerDN().toString()));
          log.info();
        }

        //
        log.info("Subject getSimpleName: "
            + CertificateUtils.getSubjectCorrectName(certificate1));
        log.info("Subject NIF: " + CertificateUtils.getDNI(certificate1));
        String unitatAdministrativa = CertificateUtils.getUnitatAdministrativa(certificate1);
        if (unitatAdministrativa != null) {
          log.info("Unitat Administrativa: " + unitatAdministrativa);
        }
        String carrec = CertificateUtils.getCarrec(certificate1);
        if (carrec != null) {
          log.info("Carrec: " + carrec);
        }
        log.info("Emissor: "
            + CertificateUtils.getCN(certificate1.getIssuerDN().toString()));
        
        
        String[] infoEmpresa = CertificateUtils.getEmpresaNIFNom(certificate1);
        if (infoEmpresa != null) {
          log.info("Empresa-NIF: " + infoEmpresa[0]);
          log.info("Empresa-Nom: " + infoEmpresa[1]);
        }

      } catch (Exception e) {
        log.info("ERROR [" + certNumber + "]");
        e.printStackTrace();
      }

    }

  }

  
  public static void main(String[] args) {

    new TestCertificate().testCertificate();

  }
  
  */

}
