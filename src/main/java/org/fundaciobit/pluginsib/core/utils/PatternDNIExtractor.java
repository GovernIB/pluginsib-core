package org.fundaciobit.pluginsib.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternDNIExtractor implements DNIExtractor {

    private final Pattern pattern;
    private final boolean nullIfNotMatch;

    public PatternDNIExtractor(String regex, boolean nullIfNotMatch) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        this.nullIfNotMatch = nullIfNotMatch;
    }

    public PatternDNIExtractor(String regex) {
        this(regex, false);
    }

    @Override
    public String extract(String serialnumber) {
        Matcher matcher = pattern.matcher(serialnumber);
        return matcher.matches() ? matcher.group(1) : (nullIfNotMatch ? null : serialnumber);
    }

}
