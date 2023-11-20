package net.haxjakt.bot.discordcmd;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.haxjakt.bot.annotations.JDAListener;
import net.haxjakt.bot.game.HarbourData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Map.entry;

@JDAListener
@SuppressWarnings("unused")
public class TravelTimeCommand extends ListenerAdapter {

    private static final String COMMAND_NAME = "time";
    private static final Logger sLogger = LoggerFactory.getLogger(TravelTimeCommand.class);

    private static final Map<String, Integer> TROOP_WEIGHT = Map.ofEntries(
            entry("slinger", 3),
            entry("archer", 5),
            entry("carabineer", 5),
            entry("spearman", 3),
            entry("swordman", 3),
            entry("hoplite", 5),
            entry("giant", 15),
            entry("gyro", 15),
            entry("balloon", 30),
            entry("ram", 30),
            entry("catapult", 30),
            entry("mortar", 30),
            entry("doctor", 10),
            entry("cook", 20),
            entry("spartan", 5)
    );

    private static final Map<String, Integer> TROOP_SPEED = Map.ofEntries(
            entry("slinger", 60),
            entry("archer", 60),
            entry("carabineer", 60),
            entry("spearman", 60),
            entry("swordman", 60),
            entry("hoplite", 60),
            entry("giant", 40),
            entry("gyro", 80),
            entry("balloon", 20),
            entry("ram", 40),
            entry("catapult", 40),
            entry("mortar", 40),
            entry("doctor", 60),
            entry("cook", 40),
            entry("spartan", 60)
    );

    private static final Map<String, Integer> BATTLESHIP_SPEED = Map.ofEntries(
            entry("ram-ship", 40),
            entry("ballista", 40),
            entry("fireship", 40),
            entry("catapult-ship", 40),
            entry("mortar-ship", 30),
            entry("steamram", 40),
            entry("rocket", 30),
            entry("diving", 40),
            entry("paddle", 60),
            entry("carrier", 20),
            entry("tender", 30)
    );

    @SuppressWarnings("unused")
    public static String[] getTroopNames() {
        return TROOP_WEIGHT.keySet().toArray(new String[0]);
    }

    private static class TravelTimeException extends RuntimeException {
        TravelTimeException(String cause) {
            super(cause);
        }
    }
    private static class InputData {
        int[] x = new int[2];
        int[] y = new int[2];
        Integer troopWeight = 0;
        Integer troopSpeed;
        Double harbourLoadingSpeed;
        boolean isBattleship;

        InputData(final String c1, final String c2, final String t, final String h) {
            convertCoords(c1, 0);
            convertCoords(c2, 1);
            resolveTroopWeightMulti(t);
            resolveTroopSpeedMulti(t);
            resolveHarbourLoadingSpeed(h);
            sLogger.info("weight: " + troopWeight + ", speed: " + troopSpeed + ", loading: " + harbourLoadingSpeed);
        }

        public boolean isHarborSpecific() {
            return troopWeight != null && harbourLoadingSpeed != null;
        }

        public boolean isSameIsland() {
            return x[0] == x[1] && y[0] == y[1];
        }

        public boolean isBattleship() { return isBattleship; }

        @Override
        public String toString() {
            return "[" + x[0] + ':' + y[0] + "]-[" + x[1] + ':' + y[1] + "] w:" + troopWeight + " p:" + harbourLoadingSpeed;
        }

        private void convertCoords(final String coordString, int what) {
            if (coordString.isEmpty()) {
                x[what] = y[what] = 0;
                return;
            }
            int separator = coordString.indexOf(':');
            if (separator == -1) {
                throw new TravelTimeException("Coordonatele nu sunt scrise bine");
            }
            x[what] = Integer.parseInt(coordString.substring(0, separator));
            y[what] = Integer.parseInt(coordString.substring(separator + 1));
        }

        private void resolveTroopWeightMulti(final String troop) {
            String[] troops = troop.split("\\s+|,");
            for (var t : troops) {
                int test = troopWeight;
                resolveTroopWeight(t);
                sLogger.debug("Calcul greutate pentru: " + t + ", computed weight:" + (troopWeight - test));
            }
        }

        private void resolveTroopWeight(final String troop) {
            int separator = troop.indexOf(':');
            if (separator == -1) {
                // do not modify troop weight - no ammount of troops specified
                return;
            }
            String troopName = troop.substring(0, separator);
            if (BATTLESHIP_SPEED.containsKey(troopName)) {
                // do not modify troop weight - battleships do not have weight
                return;
            }
            int troopCount = Integer.parseInt(troop.substring(separator + 1));
            if (!TROOP_WEIGHT.containsKey(troopName)) {
                throw new TravelTimeException("Nu s-a gasit unitatea " + troopName + " in lista de unitati cunoscute");
            }
            troopWeight += troopCount * TROOP_WEIGHT.get(troopName);
        }

        private void resolveTroopSpeedMulti(final String troop) {
            String[] troops = troop.split("\\s+|,");
            for (var t : troops) {
                resolveTroopSpeed(t);
            }
        }

        private void minTroopSpeed(int speed) {
            if (troopSpeed == null) {
                troopSpeed = speed;
            } else {
                troopSpeed = Math.min(troopSpeed, speed);
            }
        }

        private void resolveTroopSpeed(String troop) {
            sLogger.info("Resolving troop speed for: " + troop);
            int separator = troop.indexOf(':');
            if (separator != -1) {
                troop = troop.substring(0, separator);
            }
            if (TROOP_SPEED.containsKey(troop)) {
                minTroopSpeed(TROOP_SPEED.get(troop));
                isBattleship = false;
                return;
            }
            if (BATTLESHIP_SPEED.containsKey(troop)) {
                minTroopSpeed(BATTLESHIP_SPEED.get(troop));
                isBattleship = true;
                return;
            }
            // TODO maybe create some more SELF EXPLAINATORY exceptions
            throw new TravelTimeException("Nu s-a gasit unitatea " + troop + " in lista de unitati cunoscute");

        }
        private void resolveHarbourLoadingSpeed(final String harborLevel) {
            if (harborLevel.isEmpty()) {
                harbourLoadingSpeed = null;
                return;
            }
            int level = Integer.parseInt(harborLevel);
            HarbourData harbourData = HarbourData.getInstance();
            if (level > harbourData.getAvailableLevels()) {
                throw new TravelTimeException("Botul inca nu suporta un nivel atat de mare la port: " + level);
            }
            double loadingPerMinute = harbourData.getLoadingSpeed(level);
            harbourLoadingSpeed = loadingPerMinute / 60;
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // time X1:Y1 X2:Y2 troop[:amount] [harbour-level]
        if (!event.getName().equals(COMMAND_NAME)) return;
        sLogger.info("Processing command TIME");
        String coord1 = event.getOption("coord1", "", OptionMapping::getAsString);
        String coord2 = event.getOption("coord2", "", OptionMapping::getAsString);
        String troops = event.getOption("troops", "", OptionMapping::getAsString);
        String harbor = event.getOption("port", "", OptionMapping::getAsString);

        try {
            InputData in = new InputData(coord1, coord2, troops, harbor);
            sLogger.info(in.toString());
            if (in.isSameIsland() || in.isBattleship()) {
                int travelTime = (int)Math.ceil(((double) 72_000 / in.troopSpeed) * getDistance(in.x[0], in.y[0], in.x[1], in.y[1]));
                event.reply("Timpul de deplasare este: " + secondsToDisplay(travelTime)).queue();
            } else if (in.isHarborSpecific()) {
                StringBuilder sb = new StringBuilder();

                int loadingTime = (int) Math.floor(in.troopWeight / in.harbourLoadingSpeed);
                sb.append("Timpul de incarcare in port: ").append(secondsToDisplay(loadingTime));

                int travelTime = (int) Math.ceil(getTravelTimeOnSea(in.x[0], in.y[0], in.x[1], in.y[1]));
                sb.append("Timpul de deplasare: ").append(secondsToDisplay(travelTime));

                int total = loadingTime + travelTime;
                sb.append("Timpul total: ").append(secondsToDisplay(total));
                event.reply(sb.toString()).queue();
            } else {
                int travelTime = (int) Math.ceil(getTravelTimeOnSea(in.x[0], in.y[0], in.x[1], in.y[1]));
                event.reply("Timpul de deplasate este: " + secondsToDisplay(travelTime)).queue();
            }
        } catch (NumberFormatException nfe) {
            event.reply("Unul din numere este scris gresit (cred)").queue();
        } catch (TravelTimeException tte) {
            event.reply(tte.getMessage()).queue();
        }
    }

    private String secondsToDisplay(int seconds) {
        int hours = seconds / 3600;
        seconds -= hours*3600;
        int minutes = seconds / 60;
        seconds -= minutes*60;
        return hours + "h " + minutes + "min " + seconds + "sec\n";
    }
    private double getDistance(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) return 0.5;
        double dX = Math.pow(Math.abs(x1 - x2), 2);
        double dY = Math.pow(Math.abs(y1 - y2), 2);
        return Math.sqrt(dX + dY);
    }

    private double getTravelTimeOnSea(int x1, int y1, int x2, int y2) {
        // TODO there is a more complex formula to compute this
        return 1200 * getDistance(x1, y1, x2, y2);
    }

}
