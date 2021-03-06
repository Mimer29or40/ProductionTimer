package mimer29or40.productiontimer;

public class PTInfo
{
    public static final String PACKAGE_NAME       = "mimer29or40.";
    public static final String MOD_ID             = "productiontimer";
    public static final String MOD_NAME           = "Production Timer";
    public static final String VERSION_BUILD      = "@VERSION@";
    public static final String MINECRAFT_VERSION  = "@MCVERSION@";
    public static final String DEPENDENCIES       = "";
    public static final String SERVER_PROXY_CLASS = PACKAGE_NAME + MOD_ID + ".proxy.ServerProxy";
    public static final String CLIENT_PROXY_CLASS = PACKAGE_NAME + MOD_ID + ".proxy.ClientProxy";
    public static final String FINGERPRINT        = "@FINGERPRINT@";
    public static final String GUI_FACTORY        = PACKAGE_NAME + MOD_ID + ".common.config.ConfigGuiFactory";
    public static final String PATH_INTEGRATIONS  = PACKAGE_NAME + MOD_ID + ".common.integrations.";

    private PTInfo() {}
}
