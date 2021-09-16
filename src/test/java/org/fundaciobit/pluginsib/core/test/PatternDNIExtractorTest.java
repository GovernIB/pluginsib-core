package org.fundaciobit.pluginsib.core.test;

import org.fundaciobit.pluginsib.core.utils.CertificateUtils;
import org.fundaciobit.pluginsib.core.utils.DNIExtractor;
import org.fundaciobit.pluginsib.core.utils.PatternDNIExtractor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternDNIExtractorTest {

    private DNIExtractor extractor;

    @Before
    public void setup() {
        extractor = new PatternDNIExtractor(CertificateUtils.DEFAULT_DNI_PATTERN);
    }

    @Test
    public void testNIE_PNOES() {
        Assert.assertEquals("X1234567X", extractor.extract("PNOES-X1234567X"));
    }

    @Test
    public void testNIF_IDCES() {
        Assert.assertEquals("99999999R", extractor.extract("IDCES-99999999R"));
    }

    @Test
    public void testNIF_Plain() {
        Assert.assertEquals("99999990S", extractor.extract("99999990S"));
    }
}
