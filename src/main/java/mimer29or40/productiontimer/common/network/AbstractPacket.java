package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractPacket implements IMessage
{
    public final IMessage handleClient(final NetHandlerPlayClient netHandler)
    {
        FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(() -> handleClientMessage(netHandler));
        return null;
    }

    public final IMessage handleServer(final NetHandlerPlayServer netHandler)
    {
        FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(() -> handleServerMessage(netHandler));
        return null;
    }

    public abstract void handleClientMessage(NetHandlerPlayClient netHandler);

    public abstract void handleServerMessage(NetHandlerPlayServer netHandler);

    protected void writePos(BlockPos pos, ByteBuf buf)
    {
        if (pos != null)
        {
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        }
        else
        {
            buf.writeInt(0);
            buf.writeInt(0);
            buf.writeInt(0);
        }
    }

    protected BlockPos readPos(ByteBuf buf)
    {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        return new BlockPos(x, y, z);
    }
}
