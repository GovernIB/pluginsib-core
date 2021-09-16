package org.fundaciobit.pluginsib.core.test;

import junit.framework.Assert;
import org.fundaciobit.pluginsib.core.utils.CertificateUtils;
import org.junit.Test;

import java.io.InputStream;
import java.security.cert.X509Certificate;

public class CertificateUtilsTest {

    @Test
    public void testFnmt() throws Exception {
        InputStream is = getClass().getResourceAsStream("/fnmt_pf.cer");
        X509Certificate cert = CertificateUtils.decodeCertificate(is);

        Assert.assertEquals("99999999R", CertificateUtils.getDNI(cert));
        Assert.assertEquals("PRUEBAS EIDAS CERTIFICADO", CertificateUtils.getSubjectCorrectName(cert));
    }

    @Test
    public void testFirmaProfesionalPnoes() throws Exception {
        InputStream is = getClass().getResourceAsStream("/firmaprofesional-pnoes.cer");
        X509Certificate cert = CertificateUtils.decodeCertificate(is);

        Assert.assertEquals("X9999999J", CertificateUtils.getDNI(cert));
        Assert.assertEquals("Nombre Apellido1 Apellido2", CertificateUtils.getSubjectCorrectName(cert));
    }

    @Test
    public void testIzenpe() throws Exception {
        InputStream is = getClass().getResourceAsStream("/izenpe_pf.cer");
        X509Certificate cert = CertificateUtils.decodeCertificate(is);

        Assert.assertEquals("99999990S", CertificateUtils.getDNI(cert));
        Assert.assertEquals("CIUDADANO FICTICIO ACTIVO", CertificateUtils.getSubjectCorrectName(cert));
    }

    @Test
    public void testCamefirmaRp() throws Exception {
        InputStream is = getClass().getResourceAsStream("/camerfirma_rp.cer");
        X509Certificate cert = CertificateUtils.decodeCertificate(is);

        Assert.assertEquals("00000000T", CertificateUtils.getDNI(cert));
        Assert.assertEquals("JUAN ANTONIO CÁMARA ESPAÑOL", CertificateUtils.getSubjectCorrectName(cert));

        String[] organization = CertificateUtils.getEmpresaNIFNom(cert);
        Assert.assertNotNull(organization);
        Assert.assertEquals("R0599999J", organization[0]);
        Assert.assertEquals("[SOLO PRUEBAS] ENTIDAD", organization[1]);
    }
}
