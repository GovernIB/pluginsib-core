package org.fundaciobit.pluginsib.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author anadal
 *
 */
public class ComposedPatternDNIExtractor implements DNIExtractor {

    private final List<DNIExtractor> extractors = new ArrayList<DNIExtractor>();

    public ComposedPatternDNIExtractor(List<String> regexes) {
        for (String regex : regexes) {
            extractors.add(new PatternDNIExtractor(regex, true));
        }
    }

    @Override
    public String extract(String serialnumber) {
        for (DNIExtractor extractor : extractors) {
            String result = extractor.extract(serialnumber);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
