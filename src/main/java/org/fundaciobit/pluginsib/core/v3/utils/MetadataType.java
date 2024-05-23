package org.fundaciobit.pluginsib.core.v3.utils;

import java.io.Serializable;

/**
 * 
 * @author anadal
 *
 */
public enum MetadataType implements Serializable {

    STRING, INTEGER, // java.lang.BigInteger
    DECIMAL, // java.lang.BigDecimal
    BOOLEAN, // java.lang.Boolean
    BASE64, DATE, // ISO8601

}
