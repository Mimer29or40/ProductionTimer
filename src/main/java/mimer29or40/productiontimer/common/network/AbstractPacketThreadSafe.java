package mimer29or40.productiontimer.common.network;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractPacketThreadSafe extends AbstractPacket
{
    @Override
    public final IMessage handleClient(final NetHandlerPlayClient netHandler)
    {
        FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(() -> handleClientSafe(netHandler));
        return null;
    }

    @Override
    public final IMessage handleServer(final NetHandlerPlayServer netHandler)
    {
        FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(() -> handleServerSafe(netHandler));
        return null;
    }

    public abstract void handleClientSafe(NetHandlerPlayClient netHandler);

    public abstract void handleServerSafe(NetHandlerPlayServer netHandler);


}
