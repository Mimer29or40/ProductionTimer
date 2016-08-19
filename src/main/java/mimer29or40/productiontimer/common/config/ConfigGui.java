package mimer29or40.productiontimer.common.config;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.ProductionTimer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.io.File;
import java.util.Arrays;

public class ConfigGui extends GuiConfig
{
    public static final String CONFIG_GENERAL  = "general";

    public ConfigGui(GuiScreen parentScreen)
    {
        super(parentScreen,
              Arrays.asList(new IConfigElement[]
                                    {
                                            new ConfigElement(ProductionTimer.configuration.getCategory(CONFIG_GENERAL))
                                    }),
              PTInfo.MOD_ID,
              false,
              false,
              "Production Timer Configuration");
        titleLine2 = ProductionTimer.configuration.getConfigFile().getAbsolutePath();
    }
}
