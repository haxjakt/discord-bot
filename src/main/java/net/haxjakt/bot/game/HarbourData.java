package net.haxjakt.bot.game;

import java.util.List;

/**
 * Describes the in-game data for {@code Trading Port} building.
 *
 * @author haxjakt
 * @since 16.nov.2023
 *
 * TODO extract the data into external file (JSON perhaps)
 */
public class HarbourData {

    private static final List<Integer> HARBOUR_LOADING_SPEED = List.of(10, 30, 60, 93, 129, 169, 213, 261, 315, 373,
            437, 508, 586, 672, 766, 869, 983, 1108, 1246, 1398, 1565, 1748, 1950, 2172, 2416, 2685, 2980, 3305, 3663);

    public static int getLoadingSpeed(int level) { return 0; }

}
