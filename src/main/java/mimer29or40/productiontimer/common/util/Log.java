package mimer29or40.productiontimer.common.util;

import mimer29or40.productiontimer.PTInfo;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Log
{
    private static void log(Level level, String format, Object... object)
    {
        FMLLog.log(PTInfo.MOD_NAME, level, format, object);
    }

    public static void off(String format, Object... object)
    {
        log(Level.OFF, format, object);
    }

    public static void off(Object object)
    {
        off("%s", object);
    }

    public static void fatal(String format, Object... object)
    {
        log(Level.FATAL, format, object);
    }

    public static void fatal(Object object)
    {
        fatal("%s", object);
    }

    public static void error(String format, Object... object)
    {
        log(Level.ERROR, format, object);
    }

    public static void error(Object object)
    {
        error("%s", object);
    }

    public static void warn(String format, Object... object)
    {
        log(Level.WARN, format, object);
    }

    public static void warn(Object object)
    {
        warn("%s", object);
    }

    public static void info(String format, Object... object)
    {
        log(Level.INFO, format, object);
    }

    public static void info(Object object)
    {
        info("%s", object);
    }

    public static void debug(String format, Object... object)
    {
        log(Level.DEBUG, format, object);
    }

    public static void debug(Object object)
    {
        debug("%s", object);
    }

    public static void trace(String format, Object... object)
    {
        log(Level.TRACE, format, object);
    }

    public static void trace(Object object)
    {
        trace("%s", object);
    }

    public static void all(String format, Object... object)
    {
        log(Level.ALL, format, object);
    }

    public static void all(Object object)
    {
        all("%s", object);
    }
}
