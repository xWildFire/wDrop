package pl.wildfire.drop.compare;

import pl.wildfire.api.NumberUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compares {
    private static final Pattern EQUALS_PATTERN;
    private static final Pattern LESS_THAN_PATTERN;
    private static final Pattern GREATER_THAN_PATTERN;
    private static final Pattern BETWEEN_PATTERN;

    static {
        EQUALS_PATTERN = Pattern.compile("=([0-9]+)");
        LESS_THAN_PATTERN = Pattern.compile("<([0-9]+)");
        GREATER_THAN_PATTERN = Pattern.compile(">([0-9]+)");
        BETWEEN_PATTERN = Pattern.compile("([0-9]+)\\-([0-9]+)");
    }

    public static Compare<Integer> parseString(String string) {
        if (string == null || string.length() < 1) {
            return null;
        }
        try {
            string = "=" + Integer.parseInt(string);
        } catch (Exception ex) {
        }
        final Matcher equalsM = Compares.EQUALS_PATTERN.matcher(string);
        if (equalsM.find()) {
            final int value = Integer.parseInt(equalsM.group(1));
            return new IntEqualsCompare(value);
        }
        final Matcher lessThanM = Compares.LESS_THAN_PATTERN.matcher(string);
        if (lessThanM.find()) {
            final int value = Integer.parseInt(lessThanM.group(1));
            return new IntLessThanCompare(value);
        }
        final Matcher greaterThanM = Compares.GREATER_THAN_PATTERN.matcher(string);
        if (greaterThanM.find()) {
            final int value = Integer.parseInt(greaterThanM.group(1));
            return new IntGreaterThanCompare(value);
        }
        final Matcher betweenM = Compares.BETWEEN_PATTERN.matcher(string);
        if (betweenM.find()) {
            final int value2 = Integer.parseInt(betweenM.group(1));
            final int value3 = Integer.parseInt(betweenM.group(2));
            return new IntBetweenCompare(value2, value3);
        }
        return null;
    }

    public static int getRandomValue(final Compare<Integer> c) {
        switch (c.getCompareType()) {
            case BETWEEN: {
                final IntBetweenCompare compare = (IntBetweenCompare) c;
                return NumberUtil.getRandInt(compare.min, compare.max);
            }
            case LESS_THAN: {
                final AbstractIntCompare compare2 = (AbstractIntCompare) c;
                return NumberUtil.getRandInt(0, compare2.value);
            }
            case GREATER_THAN: {
                final AbstractIntCompare compare2 = (AbstractIntCompare) c;
                return NumberUtil.getRandInt(compare2.value, Integer.MAX_VALUE);
            }
            case EQUALS: {
                final AbstractIntCompare compare2 = (AbstractIntCompare) c;
                return compare2.value;
            }
            default: {
                return 1;
            }
        }
    }
}
