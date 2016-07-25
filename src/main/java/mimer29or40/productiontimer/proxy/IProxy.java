package mimer29or40.productiontimer.proxy;

import java.io.File;

public interface IProxy
{
    void registerBlocks();

    void registerItems();

    void registerRecipes();

    void registerEvents();

    void registerGUIs();

    void registerRenderers();

    void registerConfiguration(File configFile);
}
