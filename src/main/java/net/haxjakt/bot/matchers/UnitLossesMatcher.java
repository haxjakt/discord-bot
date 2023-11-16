package net.haxjakt.bot.matchers;

import net.haxjakt.bot.coreutls.CoreObject;
import net.haxjakt.bot.coreutls.LocalPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitLossesMatcher extends CoreObject {

    final String PATTERN_NAME = "UnitLossesPattern.txt";
    final List<String> ENTITIES = List.of("atkUnit", "atkDiff", "defUnit", "defDiff");
    Pattern pattern;

    UnitLossesMatcher() {
        try (InputStream inputStream = getClassLoader().getResourceAsStream(PATTERN_NAME)) {
            String strPattern = "";
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                strPattern = reader.lines().findFirst().orElse("");
            }
            pattern = Pattern.compile(strPattern);
        } catch (IOException e) {
            System.out.println("Cannot open resource: " + PATTERN_NAME);
            throw new RuntimeException(e);
        }
    }

    Map<String, LocalPair> getMapMatches(final String section) {
        Map<String, LocalPair> map = new HashMap<>();
        Map<String, String> parsedResult;
        String[] lines = section.split(System.lineSeparator());
        Matcher m = null;

        for (final String line : lines) {
//            System.out.println("line: " + line);
//            System.out.println("patt: " + pattern + "\n");
            m = pattern.matcher(line);
            if (!m.matches()) {
                throw new RuntimeException();
            }
            parsedResult = new HashMap<>();
            for (var entry : ENTITIES) {
                parsedResult.put(entry, m.group(ENTITIES.indexOf(entry) + 1));
            }
            LocalPair diffs = checkAndExtract(parsedResult);
            String unitName = getName(parsedResult);
            map.put("#" + unitName + "#", diffs);
        }
        return map;
    }

    private String getName(final Map<String, String> result) {
        if (result.get(ENTITIES.get(0)).isEmpty()) {
            return result.get(ENTITIES.get(2));
        }
        return result.get(ENTITIES.get(0));
    }

    private LocalPair checkAndExtract(final Map<String, String> result) {
        final int A = 0;
        final int D = 2;
        // checks:
        // 1. units cannot be both empty:
        if (result.get(ENTITIES.get(A)).isEmpty() &&
            result.get(ENTITIES.get(D)).isEmpty()) {
            throw new RuntimeException("Bad parsing of unit names (all_empty)" + result);
        }
        // 2. if both of them are not empty, then they have to be identical
        if ((!result.get(ENTITIES.get(A)).isEmpty() &&
                !result.get(ENTITIES.get(D)).isEmpty()) &&
                !result.get(ENTITIES.get(A)).equals(result.get(ENTITIES.get(D)))) {
            throw new RuntimeException("Bad parsing of unit names (not_equal)" + result);
        }
        return new LocalPair(result.get(ENTITIES.get(1)), result.get(ENTITIES.get(3)));
    }

}
