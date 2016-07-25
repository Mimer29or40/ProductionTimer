package mimer29or40.productiontimer.proxy;

import java.io.File;

public class CommonProxy implements IProxy
{
    @Override
    public void registerBlocks()
    {
        // ModBlocks.registerBlocks();
    }

    @Override
    public void registerItems()
    {
        // ModItems.registerItems();
    }

    @Override
    public void registerRecipes()
    {
//        for (BlockBase block : ModBlocks.getBlocks())
//        {
//            if (block instanceof IRegisterRecipe)
//            { ((IRegisterRecipe) block).registerRecipes(); }
//        }
//
//        for (ItemBase item : ModItems.getItems())
//        {
//            if (item instanceof IRegisterRecipe)
//            { ((IRegisterRecipe) item).registerRecipes(); }
//        }
    }

    @Override
    public void registerEvents()
    {

    }

    @Override
    public void registerGUIs()
    {

    }

    @Override
    public void registerRenderers()
    {

    }

    @Override
    public void registerConfiguration(File configFile)
    {
        // EtherealStorage.configuration = Config.initConfig(configFile);
    }
}
