package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.ProductionTimer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;

public class PacketOpenGui extends AbstractPacket
{
    private int      id;
    private BlockPos pos;

    public PacketOpenGui() {}

    public PacketOpenGui(int id, BlockPos pos)
    {
        this.id = id;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readInt();
        pos = readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(id);
        writePos(pos, buf);
    }

    @Override
    public void handleClientMessage(NetHandlerPlayClient netHandler)
    {

    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
        EntityPlayer player = netHandler.playerEntity;
        player.openGui(ProductionTimer.INSTANCE, id, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
    }
}
