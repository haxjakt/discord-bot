package net.haxjakt.bot.coreutls;

public class LocalPair {
    private final String firstItem;
    private final String secondItem;

    public LocalPair(final String first, final String second) {
        firstItem = first;
        secondItem = second;
    }

    public String getFirst() { return firstItem; }
    public String getSecond() { return secondItem; }

    @Override
    public String toString() {
        return "{atkDiff: " + firstItem + ", defDiff: " + secondItem + "}";
    }
}
