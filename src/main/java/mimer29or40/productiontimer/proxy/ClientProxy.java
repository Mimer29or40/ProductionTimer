package mimer29or40.productiontimer.proxy;

import mimer29or40.productiontimer.client.handler.HandlerHUD;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrank.class, new RenderCrank());
//        PTBlocks.blockTest.initModel();
    }

    public void registerHandlers()
    {
        MinecraftForge.EVENT_BUS.register(new HandlerHUD());
    }
}
