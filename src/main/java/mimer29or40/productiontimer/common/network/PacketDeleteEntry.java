package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PacketDeleteEntry extends AbstractPacket
{
    private BlockPos pos;
    private int      entry;

    public PacketDeleteEntry() {}

    public PacketDeleteEntry(BlockPos pos, int entry)
    {
        this.pos = pos;
        this.entry = entry;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = readPos(buf);
        entry = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        writePos(pos, buf);
        buf.writeInt(entry);
    }

    @Override
    public void handleClientMessage(NetHandlerPlayClient netHandler)
    {

    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
        EntityPlayer player = netHandler.playerEntity;
        TileEntity tile = player.worldObj.getTileEntity(pos);
        if (tile instanceof TileController)
        {
            TileController tileController = (TileController) tile;
            if (tileController.selectedEntry > 0) tileController.selectedEntry--;
            tileController.removeEntry(entry);
            if (tileController.entries.size() == 0) tileController.selectedEntry = -1;
        }
    }
}
