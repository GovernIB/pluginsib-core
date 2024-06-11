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
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;

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

    private static final String SUBJECT_ALT_NAME_OID = "2.5.29.17";

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

    /**
     * Obtiene el nombre com&uacute;n (Common Name, CN) del titular de un
     * certificado X.509. Si no se encuentra el CN, se devuelve la unidad
     * organizativa (Organization Unit, OU).
     *
     * @param c
     *          Certificado X.509 del cual queremos obtener el nombre com&uacute;n
     * @return Nombre com&uacute;n (Common Name, CN) del titular de un certificado
     *         X.509
     */
    public static String getCN(final X509Certificate c) {
        if (c == null) {
            return null;
        }
        return getCN(c.getSubjectX500Principal().toString());
    }

    /**
     * Obtiene el nombre com&uacute;n (Common Name, CN) de un <i>Principal</i>
     * X.400. Si no se encuentra el CN, se devuelve la unidad organizativa
     * (Organization Unit, OU).
     *
     * @param principal
     *          <i>Principal</i> del cual queremos obtener el nombre com&uacute;n
     * @return Nombre com&uacute;n (Common Name, CN) de un <i>Principal</i> X.400
     */
    public static String getCN(final String principal) {
        if (principal == null) {
            return null;
        }

        String rdn = getRDNvalue("cn", principal); //$NON-NLS-1$
        if (rdn == null) {
            rdn = getRDNvalue("ou", principal); //$NON-NLS-1$
        }

        if (rdn != null) {
            return rdn;
        }

        final int i = principal.indexOf('=');
        if (i != -1) {
            log.error(
                    "No se ha podido obtener el Common Name ni la Organizational Unit, se devolvera el fragmento mas significativo"); //$NON-NLS-1$
            return getRDNvalue(principal.substring(0, i), principal);
        }

        log.error("Principal no valido, se devolvera la entrada"); //$NON-NLS-1$
        return principal;
    }

    /**
     * Recupera el valor de un RDN de un principal. El valor de retorno no incluye
     * el nombre del RDN, el igual, ni las posibles comillas que envuelvan el
     * valor. La funci&oacute;n no es sensible a la capitalizaci&oacute;n del RDN.
     * Si no se encuentra, se devuelve {@code null}.
     *
     * @param rdn
     *          RDN que deseamos encontrar.
     * @param principal
     *          Principal del que extraer el RDN
     * @return Valor del RDN indicado o {@code null} si no se encuentra.
     */
    public static String getRDNvalue(final String rdn, final String principal) {

        int offset1 = 0;
        while ((offset1 = principal.toLowerCase().indexOf(rdn.toLowerCase(), offset1)) != -1) {

            if (offset1 > 0 && principal.charAt(offset1 - 1) != ',' && principal.charAt(offset1 - 1) != ' ') {
                offset1++;
                continue;
            }

            offset1 += rdn.length();
            while (offset1 < principal.length() && principal.charAt(offset1) == ' ') {
                offset1++;
            }

            if (offset1 >= principal.length()) {
                return null;
            }

            if (principal.charAt(offset1) != '=') {
                continue;
            }

            offset1++;
            while (offset1 < principal.length() && principal.charAt(offset1) == ' ') {
                offset1++;
            }

            if (offset1 >= principal.length()) {
                return ""; //$NON-NLS-1$
            }

            int offset2;
            if (principal.charAt(offset1) == ',') {
                return ""; //$NON-NLS-1$
            } else if (principal.charAt(offset1) == '"') {
                offset1++;
                if (offset1 >= principal.length()) {
                    return ""; //$NON-NLS-1$
                }

                offset2 = principal.indexOf('"', offset1);
                if (offset2 == offset1) {
                    return ""; //$NON-NLS-1$
                } else if (offset2 != -1) {
                    return principal.substring(offset1, offset2);
                } else {
                    return principal.substring(offset1);
                }
            } else {
                offset2 = principal.indexOf(',', offset1);
                if (offset2 != -1) {
                    return principal.substring(offset1, offset2).trim();
                }
                return principal.substring(offset1).trim();
            }
        }

        return null;
    }

    public static String[] getEmpresaNIFNom(X509Certificate cert) throws Exception {

        Map<String, String> map = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);
        String nif = map.get("OID.1.3.6.1.4.1.5734.1.7");
        if (nif == null) {

            final String subjectDNStr = cert.getSubjectDN().toString();
            String org = getRDNvalue("O", subjectDNStr);

            String admin_id_orgorg = getRDNvalue("OID.2.5.4.97", subjectDNStr); // 2.5.4.97 =
                                                                                // VATES-Q0100000I
                                                                                // System.err.println("ORG ADMIN ID => " + admin_id_orgorg);

            if (admin_id_orgorg == null) {

                admin_id_orgorg = getRDNvalue("OID.1.3.6.1.4.1.18838.1.1", subjectDNStr);
                if (admin_id_orgorg != null) {
                    // Obtenir Serial Number
                    admin_id_orgorg = getRDNvalue("SERIALNUMBER", subjectDNStr);
                    // System.err.println("SERIALNUMBER => " + admin_id_orgorg);
                }

                if (admin_id_orgorg == null) {
                    return null;
                }

            } else {
                int index = admin_id_orgorg.indexOf('-');
                if (index != -1) {
                    admin_id_orgorg = admin_id_orgorg.substring(index + 1);
                }
            }

            return new String[] { admin_id_orgorg, org };
        }

        int posGuio = nif.indexOf('-');

        if (posGuio != -1) {
            nif = nif.substring(posGuio + 1);
        }

        return new String[] { nif, map.get("OID.1.3.6.1.4.1.5734.1.6") };
    }

    /**
     * This static method is the default implementation of the
     * getSubjectAlternaitveNames method in X509Certificate. A
     * X509Certificate provider generally should overwrite this to
     * provide among other things caching for better performance.
     */
    public static Map<String, String> getAlternativeNamesOfExtension(X509Certificate cert, String oid)
            throws CertificateParsingException {
        try {

            Map<String, String> values = new HashMap<String, String>();

            X500Principal principal = cert.getSubjectX500Principal();
            LdapName ldapDN = new LdapName(principal.getName());
            List<String> names = new ArrayList<>();
            for (Rdn rdn : ldapDN.getRdns()) {
                if (rdn.getType().equalsIgnoreCase(oid)) {
                    String name = rdn.getValue().toString();
                    names.add(name);
                }
            }

            /*
            byte[] ext = cert.getExtensionValue(oid);
            if (ext == null) {
                return values;
            }
            
            DerValue val = new DerValue(ext);
            byte[] data = val.getOctetString();
            
            SubjectAlternativeNameExtension subjectAltNameExt = new SubjectAlternativeNameExtension(Boolean.FALSE,
                    data);
            
            GeneralNames names;
            Collection<TypeValue> col;
            try {
                names = (GeneralNames) subjectAltNameExt.get(SubjectAlternativeNameExtension.SUBJECT_NAME);
                col = makeAltNames(names);
            } catch (IOException ioe) {
                // should not occur
                col = Collections.emptySet();
            }
            
            for (TypeValue typeValue : col) {
            
                if (typeValue.getType() == GeneralNameInterface.NAME_DIRECTORY) {
            
                    List<Rdn> rdn = new LdapName(typeValue.getValue()).getRdns();
                    for (Rdn rdn2 : rdn) {
                        values.put(rdn2.getType(), rdn2.getValue().toString());
                    }
            
                }
            }
            */
            return values;

        } catch (Exception ioe) {
            throw new CertificateParsingException(ioe);
        }
    }

    public static String getSubjectCorrectName(X509Certificate cert) {

        final String subjectDNStr = cert.getSubjectDN().toString();

        String certName = getCN(subjectDNStr);

        // Parche pels certificat DNIe (eliminar FIRMA i AUTENTICACION)
        {
            final String[] dnie = { " (FIRMA)", " (AUTENTICACIÓN)" };
            for (String tipusDNIe : dnie) {
                int pos = certName.indexOf(tipusDNIe);
                if (pos != -1) {
                    // Eliminar tipus
                    certName = certName.replace(tipusDNIe, "");
                    // Posar Nom davant
                    pos = certName.lastIndexOf(',');
                    if (pos == -1) { // pels DNIv2
                        pos = certName.lastIndexOf('/');
                    }
                    if (pos != -1) {
                        String nom = certName.substring(pos + 1).trim();
                        String llinatges = certName.substring(0, pos).trim();
                        return nom + " " + llinatges;
                    }
                }
            }
        }

        try {
            // FNMT EIDAS
            // OID.1.3.6.1.4.1.5734.1.1=ANTONI
            // OID.1.3.6.1.4.1.5734.1.2=NADAL
            // OID.1.3.6.1.4.1.5734.1.3=BENNASAR

            // CAMERFIRMA
            // OID.1.3.6.1.4.1.17326.30.7=JUAN ANTONIO
            // OID.1.3.6.1.4.1.17326.30.8=CÁMARA
            // OID.1.3.6.1.4.1.17326.30.9=ESPAÑOL

            Map<String, String> values = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);

            if (!values.isEmpty()) {

                if (values.get("OID.1.3.6.1.4.1.5734.1.1") != null) {

                    return values.get("OID.1.3.6.1.4.1.5734.1.1") + " " + values.get("OID.1.3.6.1.4.1.5734.1.2") + " "
                            + values.get("OID.1.3.6.1.4.1.5734.1.3");

                } else if (values.get("OID.1.3.6.1.4.1.17326.30.7") != null) {

                    return values.get("OID.1.3.6.1.4.1.17326.30.7") + " " + values.get("OID.1.3.6.1.4.1.17326.30.8")
                            + " " + values.get("OID.1.3.6.1.4.1.17326.30.9");

                }
            }

        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        // Parche pels certificats FNMT que contenen la paraula NOMBRE al principi i
        // el NIF del Firmant
        if (certName.startsWith("NOMBRE ")) {
            certName = certName.substring("NOMBRE ".length());
        }
        int posNIF = certName.indexOf(" - NIF ");
        if (posNIF != -1) {
            certName = certName.substring(0, posNIF);
        }
        // Parche Certificats d'Administracio Pública de FNMT
        int posDNI = certName.indexOf(" - DNI ");
        if (posDNI != -1) {
            certName = certName.substring(0, posDNI);
        }

        // sn
        String llinatges = getRDNvalue("surname", subjectDNStr);

        if (llinatges != null && llinatges.trim().length() != 0) {

            // cercam nom
            String nom = getRDNvalue("givenname", subjectDNStr);

            if (nom == null || nom.trim().length() == 0) {
                nom = getRDNvalue("g", subjectDNStr);
            }

            if (nom != null && nom.trim().length() != 0) {
                String fullName = nom + " " + llinatges;

                if (fullName.length() >= certName.length()) {
                    return fullName;
                }
            }

        }

        return certName;

    }
    
    private final static DNIExtractor defaultDNIExtractor;

    static {
        defaultDNIExtractor = new PatternDNIExtractor(DNIExtractor.DEFAULT_DNI_PATTERN, false);
    }
    

    /**
     * Recupera el DNI a partir de los datos del certificado
     *
     * @param certificate
     *          Objeto X509Certificate
     * @return String con el DNI del usuario
     */
    public static String getDNI(X509Certificate certificate) {
        return getDNIWithExtractor(certificate, defaultDNIExtractor);
    }

    private static final ConcurrentMap<List<String>, DNIExtractor> extractorCache = new ConcurrentHashMap<List<String>, DNIExtractor>();

    /**
     * Recupera el DNI a partir de los datos del certificado
     *
     * @param certificate
     *          Objeto X509Certificate
     * @return String con el DNI del usuario
     */
    public static String getDNI(X509Certificate certificate, List<String> patterns) {
        DNIExtractor extractor = extractorCache.get(patterns);
        if (extractor == null) {
            extractor = new ComposedPatternDNIExtractor(patterns);
            extractorCache.putIfAbsent(patterns, extractor);
        }

        return getDNIWithExtractor(certificate, extractor);
    }

    private static String getDNIWithExtractor(X509Certificate certificate, DNIExtractor dniExtractor) {
        if (certificate == null) {
            return null;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        String value = certificate.getSubjectDN().getName().trim();

        String[] split = value.split(",");
        for (int i = 0; i < split.length; i++) {
            if (split[i].indexOf('=') != -1) {
                String[] split2 = split[i].split("=");
                if (split2.length == 2) {
                    map.put(split2[0].trim(), split2[1].trim());
                }
            }
        }
        String nif = map.get("SERIALNUMBER");
        if (nif == null) {
            // Per certificats tipus FNMT
            String cadena = map.get("CN");
            if (cadena != null) {
                int finom = cadena.indexOf(" - NIF ");
                if (finom != -1) {
                    int iniciNif = finom + " - NIF ".length();
                    nif = cadena.substring(iniciNif);
                }
            }
        } else {
            nif = dniExtractor.extract(nif);
        }

        return nif;
    }

    public static String getPseudonym(X509Certificate certificate) {
        if (certificate == null) {
            return null;
        }

        final String subjectDNStr = certificate.getSubjectDN().toString();
        return CertificateUtils.getRDNvalue("OID.2.5.4.65", subjectDNStr);
    }



    public static String getUnitatAdministrativa(X509Certificate cert) throws Exception {

        Map<String, String> map = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);

        return map.get("OID.2.16.724.1.3.5.3.2.10");
    }
    
    public static String getCarrec(X509Certificate cert) throws Exception {
        Map<String, String> map = getAlternativeNamesOfExtension(cert, SUBJECT_ALT_NAME_OID);
        String carrec = map.get("OID.2.16.724.1.3.5.3.2.11");
        if (carrec == null) {
            carrec = map.get("OID.2.16.724.1.3.5.7.2.11");
        }
        return carrec;
    }

}
