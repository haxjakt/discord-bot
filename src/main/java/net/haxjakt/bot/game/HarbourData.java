package net.haxjakt.bot.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.haxjakt.bot.coreutls.CoreObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * Describes the in-game data for {@code Trading Port} building.
 *
 * @author haxjakt
 * @since 16.nov.2023
 *
 * TODO extract the data into external file (JSON perhaps)
 */
public class HarbourData extends CoreObject {

    private static HarbourData INSTANCE;
    private static final Logger sLogger = LoggerFactory.getLogger(HarbourData.class);

    private List<Integer> mHarbourLoadingSpeed;

    private HarbourData() {
        try (InputStream in = getClassLoader().getResourceAsStream("gamedata/harbour.json")) {
            ObjectMapper mapper = new ObjectMapper();
            mHarbourLoadingSpeed = mapper.readValue(in, new TypeReference<List<Integer>>() {});
            sLogger.info("Read harbour data: " + mHarbourLoadingSpeed);
        } catch (Exception e) {
            sLogger.error("Caught error {" + e.getClass() + "} while trying to read harbour properties");
        }
    }

    public static HarbourData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HarbourData();
        }
        return INSTANCE;
    }

    public int getAvailableLevels() {
        return mHarbourLoadingSpeed.size();
    }

    public int getLoadingSpeed(int level) {
        return mHarbourLoadingSpeed.get(level);
    }

    public int getLoadingSpeed(int levelHarbour1, int levelHarbour2) {
        return mHarbourLoadingSpeed.get(levelHarbour1) + mHarbourLoadingSpeed.get(levelHarbour2);
    }

}
