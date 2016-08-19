package mimer29or40.productiontimer.proxy;

import mimer29or40.productiontimer.ProductionTimer;
import mimer29or40.productiontimer.client.gui.GuiHandler;
import mimer29or40.productiontimer.common.block.BlockBase;
import mimer29or40.productiontimer.common.block.PTBlocks;
import mimer29or40.productiontimer.common.config.Config;
import mimer29or40.productiontimer.common.item.ItemBase;
import mimer29or40.productiontimer.common.item.PTItems;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.registry.IRegisterRecipe;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;

public class CommonProxy implements IProxy
{
    @Override
    public void registerBlocks()
    {
         PTBlocks.registerBlocks();
    }

    @Override
    public void registerItems()
    {
         PTItems.registerItems();
    }

    @Override
    public void registerRecipes()
    {
        for (BlockBase block : PTBlocks.getBlocks())
        {
            if (block instanceof IRegisterRecipe)
            { ((IRegisterRecipe) block).registerRecipes(); }
        }

        for (ItemBase item : PTItems.getItems())
        {
            if (item instanceof IRegisterRecipe)
            { ((IRegisterRecipe) item).registerRecipes(); }
        }
    }

    @Override
    public void registerEvents()
    {

    }

    @Override
    public void registerGUIs()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(ProductionTimer.INSTANCE, new GuiHandler());
    }

    @Override
    public void registerRenderers()
    {

    }

    @Override
    public void registerNetwork()
    {
//        PacketHandler.registerMessages(PTInfo.MOD_ID);
        PTNetwork.instance.setup();
    }

    @Override
    public void registerHandlers()
    {

    }

    @Override
    public void registerConfiguration(File configFile)
    {
        ProductionTimer.configuration = Config.initConfig(configFile);
    }
}
