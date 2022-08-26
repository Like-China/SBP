package planer;

public class Settings {

    public static String roadName = "NYC";
    public static boolean isStatic = false;
    public static boolean isSelfAware = true;
    public static boolean isExpTime = true;
    public static boolean isPreCheck = true;
    public static int[] userSizesNY = { 2000,4000,6000,8000,10000};
    public static int[] userSizesTG = { 4000, 8000, 12000, 16000, 20000 };
    public static float[] es = { 0.0002f, 0.0004f, 0.0006f, 0.0008f, 0.001f };
    public static int[] intervals = { 1, 2, 3, 4, 5 };
    public static int[] queryDensitys = { 20, 40, 60, 80, 100 };

    /**
	 * The default settings
	 */
	public static int userSize = (roadName == "TGC") ? 4000 : 2000;
	public static int interval = 2;
	public static float e = 0.001f;
	public static int queryDensity = 50;
	public static int expNum = 1;

}
