package org.fundaciobit.pluginsib.core.v3.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.jboss.logging.Logger;

/**
*
* @author anadal
*
*/
public class CertificateUtils {

    private final static Logger log = Logger.getLogger(CertificateUtils.class.getName());
    
    

    /**
     * Codifica la informacion almacenada en un objeto X509Certificate
     *
     * @param certificate
     *          Objeto X509Certificate con la informacion de un certificado de
     *          usuario
     *
     * @return Array de bytes con la informacion codificada
     *
     */
    public static byte[] encodeCertificate(X509Certificate certificate) throws Exception {
        return certificate.getEncoded();
    }

    /**
     * Obtiene el objeto X509Certificate a partir de los datos codificados de un
     * certificado
     *
     * @param is
     *          Stream de bytes con la informacion codificada de un certificado
     *
     * @return Objeto X509Certificate
     *
     */
    public static X509Certificate decodeCertificate(InputStream is) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate result = (X509Certificate) cf.generateCertificate(is);
        is.close();
        return result;
    }
    

    /**
     * 
     * @param is
     * @param passwordks
     * @return
     * @throws KeyStoreException
     * @throws NoSuchProviderException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public static List<Certificate> readCertificatesOfKeystore(InputStream is, String passwordks)
            throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException,
            CertificateException, FileNotFoundException, Exception {
        KeyStore ks = KeyStore.getInstance("pkcs12", "SunJSSE");
        ks.load(is, passwordks.toCharArray());

        Enumeration<String> aliases = ks.aliases();
        List<Certificate> certificats = new ArrayList<Certificate>();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate[] cc = ks.getCertificateChain(alias);
            if (cc != null && cc.length != 0) {
                certificats.addAll(Arrays.asList(cc));
            }
        }

        return certificats;
    }

    /**
     * 
     * @param p12
     * @param p12Password
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     */
    public static PublicCertificatePrivateKeyPair readPKCS12(InputStream p12, String p12Password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
            UnrecoverableKeyException {

        log.info("Reading PKCS12 certificate");

                String keyAlias = null;
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(p12, p12Password.toCharArray());

        Enumeration<String> aliases = keystore.aliases();

        while (aliases.hasMoreElements()) {
            keyAlias = aliases.nextElement();
        }

        return new PublicCertificatePrivateKeyPair((X509Certificate) keystore.getCertificate(keyAlias),
                (PrivateKey) keystore.getKey(keyAlias, p12Password.toCharArray()));

    }
}
