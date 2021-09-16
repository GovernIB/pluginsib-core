package org.fundaciobit.pluginsib.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternDNIExtractor implements DNIExtractor {

    private final Pattern pattern;

    public PatternDNIExtractor(String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String extract(String serialnumber) {
        Matcher matcher = pattern.matcher(serialnumber);
        return matcher.matches() ? matcher.group(1) : serialnumber;
    }

}
