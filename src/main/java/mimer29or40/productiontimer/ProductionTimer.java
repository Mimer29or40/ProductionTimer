package mimer29or40.productiontimer;

import com.google.common.base.Stopwatch;
import mimer29or40.productiontimer.common.config.Config;
import mimer29or40.productiontimer.common.util.Log;
import mimer29or40.productiontimer.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.TimeUnit;

@Mod(modid = PTInfo.MOD_ID, name = PTInfo.MOD_NAME, certificateFingerprint = PTInfo.FINGERPRINT, dependencies = PTInfo.DEPENDENCIES, version = PTInfo.VERSION_BUILD)
public class ProductionTimer
{
    @Mod.Instance(PTInfo.MOD_ID)
    public static ProductionTimer INSTANCE;

    @SidedProxy(clientSide = PTInfo.CLIENT_PROXY_CLASS, serverSide = PTInfo.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static Configuration configuration;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        Log.info("Pre Initialization (Started)");

        //Make sure we are running on java 7 or newer
//        if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_7))
//        {
//            throw new OutdatedJavaException(String.format("%s requires Java 7 or newer, Please update your java", ModInfo.MOD_NAME));
//        }

        proxy.registerConfiguration(event.getSuggestedConfigurationFile());

        proxy.registerBlocks();

        proxy.registerItems();

        proxy.registerGUIs();

        proxy.registerEvents();

        proxy.registerRenderers();

        proxy.registerNetwork();

        proxy.registerHandlers();

//        IntegrationsManager.instance().index();
//        IntegrationsManager.instance().preInit();

        Log.info("Pre Initialization (Ended after " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms)");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        Log.info("Initialization (Started)");

        proxy.registerRecipes();

//        WorldGen worldGen = new WorldGen();
//        GameRegistry.registerWorldGenerator(worldGen, 0);
//        MinecraftForge.EVENT_BUS.register(worldGen);

        MinecraftForge.EVENT_BUS.register(this);

//        IntegrationsManager.instance().init();

        Log.info("Initialization (Ended after " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms)");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        Log.info("Post Initialization (Started)");

//        IntegrationsManager.instance().postInit();

        Log.info("Post Initialization (Ended after " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms)");
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(PTInfo.MOD_ID))
        {
            Config.loadConfiguration();
        }
    }
}
