package org.fundaciobit.pluginsib.core.test.v3;


import org.fundaciobit.pluginsib.core.v3.utils.CertificateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.X509Certificate;

/**
 * 
 * @author anadal
 * 8 may 2025 8:20:27
 */
public class CertificatAmbPseudonim extends TestCertificate {

    public static void main(String[] args) {
        try {

            String[] certs = getCertificates();

            for (String certNumber : certs) {
                System.out.println(" ---- " + certNumber + " -------");

                String certInfo = getProperty("cert." + certNumber);
                String[] fields = certInfo.split("\\|");

                String filePath = fields[0];
                //String passwordks = fields[1];
                String type = fields[2];

                if ("cer".equals(type)) {
                    X509Certificate certificate1;

                    InputStream certstream = new FileInputStream(new File(filePath));
                    certificate1 = CertificateUtils.decodeCertificate(certstream);

                    String pseudonim = CertificateUtils.getPseudonymValue(certificate1);

                    System.out.println("isPseudonim[" + filePath + "]: "
                            + ((pseudonim == null) ? "false" : ("true  { " + pseudonim + " }")));
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
