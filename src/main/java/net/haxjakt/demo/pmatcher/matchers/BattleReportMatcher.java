package net.haxjakt.demo.pmatcher.matchers;

import net.haxjakt.demo.pmatcher.coreutls.CoreObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BattleReportMatcher extends CoreObject {

    final Logger sLogger = LoggerFactory.getLogger(BattleReportMatcher.class);
    final String PATTERN_NAME = "BattleReportPattern.txt";
    final String TEST_FILE_NAME = "test.txt";
    final List<String> ENTITIES = List.of(
            "target-town",
            "date-time",
            "attacker", "attackerAlliance", "attackerTown",
            "defender", "defenderAlliance", "defenderTown",
            "unitLossesSection",
            "attackerGeneralsDiff", "defenderGeneralsDiff",
            "attackerAttackPoints", "defenderDefencePoints",
            "attackerReceivedDmg", "defenderReceivedDmg",
            "attackerDmg%", "defenderDmg%",
            "winner",
            "loser");
    Pattern pattern;

    public BattleReportMatcher() {
        sLogger.info("Creating instance");
        try (InputStream inputStream = getClassLoader().getResourceAsStream(PATTERN_NAME)) {
            StringBuilder sb = new StringBuilder();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                reader.lines().forEach(line -> sb.append(line));
            }
            pattern = Pattern.compile(sb.toString(), Pattern.DOTALL);
        } catch (IOException e) {
            System.out.println("Cannot open resource: " + PATTERN_NAME);
            throw new RuntimeException(e);
        }
    }

    private String readTestFile() {
        try (InputStream inputStream = getClassLoader().getResourceAsStream(TEST_FILE_NAME)) {
            StringBuilder sb = new StringBuilder();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                reader.lines().forEach(line -> sb.append(line).append('\n'));
            }
//            System.out.println("sample = \n" + sb);
            return sb.toString();
        } catch (IOException e) {
            System.out.println("Cannot open resource: " + PATTERN_NAME);
            throw new RuntimeException(e);
        }
    }
    public Map<String, String> toMap(@NotNull final String sample) {
        sLogger.info("Begin to parse message. Message length: " + sample.length());
        Matcher m = pattern.matcher(sample);
        Map<String, String> map = new HashMap<>();
//        if (!m.matches()) {
//            sLogger.error("Could not match the template to the provided sample!");
//        } else {
            for (var entity : ENTITIES) {
                sLogger.info("Extracting entity: {" + entity + "}");
                if (entity.equals("unitLossesSection")) {
                    var ulm = new UnitLossesMatcher();
                    var diffs = ulm.getMapMatches(m.group(ENTITIES.indexOf(entity) + 1));
                    Map<String, String> res = diffs.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));
                    map.putAll(res);
                } else {
//                System.out.println(entity + " = '" + m.group(ENTITIES.indexOf(entity) + 1) + "'");
                    map.put(entity, m.group(ENTITIES.indexOf(entity) + 1));
                }
            }
//        }
        return map;
    }

}
