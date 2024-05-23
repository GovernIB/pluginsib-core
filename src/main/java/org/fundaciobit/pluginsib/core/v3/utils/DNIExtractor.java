package org.fundaciobit.pluginsib.core.v3.utils;

/**
 * 
 * @author anadal
 *
 */
public interface DNIExtractor {
    
    /**
     * Patró emprat per defecte per extreure el NIF/NIE de l'atribut SERIALNUMBER del Subject del certificat.
     * El NIF/NIE ha de ser el grup 1. Per tant si es fan altres grups han d'anar marcats com a "non-capturing" amb "(?:".
     * Per veure els formats, veure l'apartat 5.1.3 de ETSI EN 319 412-1 V1.4.4
     */
    public static final String DEFAULT_DNI_PATTERN = "^(?:(?:PAS|IDC|PNO|TAX|TIN)ES-)?([X-Z]?[0-9]{7,8}[A-Z])$";


    String extract(String serialnumber);
}
