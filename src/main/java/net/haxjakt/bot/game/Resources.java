package net.haxjakt.bot.game;

public class Resources {

//    public enum Type { WOOD, WINE, MARBLE, CRYSTAL, SULPHUR, GOLD, CITIZENS }

    private int mWood = 0;
    private int mWine = 0;
    private int mMarble = 0;
    private int mCrystal = 0;
    private int mSulphur = 0;

    Resources wood(int amount) { mWood += amount; return this; }
    Resources wine(int amount) { mWine += amount; return this; }

    Resources marble(int amount) { mMarble += amount; return this; }

    Resources crystal(int amount) { mCrystal += amount; return this; }

    Resources sulphur(int amount) { mSulphur += amount; return this; }
    public int getmWood() {
        return mWood;
    }

    public int getmWine() {
        return mWine;
    }

    public int getmMarble() {
        return mMarble;
    }

    public int getmCrystal() {
        return mCrystal;
    }

    public int getmSulphur() {
        return mSulphur;
    }

}
