package local.mock;

import java.util.Vector;

public class Item {
    public static final int RICHTEXT = 0;
    public static final int TEXT = 0;
    public static final int NUMBERS = 0;
    public static final int NAMES = 0;
    public static final int DATETIMES = 0;
    public static final int READERS = 0;

    public void setSummary(boolean value) {}

    public void recycle() {}

    public int getType() { return 0; }

    public String getText() { return null; }

    public String getName() { return null; }

    public Vector<Object> getValues() { return null; }
}
