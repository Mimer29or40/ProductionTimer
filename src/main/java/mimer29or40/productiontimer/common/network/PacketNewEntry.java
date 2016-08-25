package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.ProductionTimer;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PacketNewEntry extends AbstractPacket
{
    private BlockPos pos;

    public PacketNewEntry() {}

    public PacketNewEntry(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        writePos(pos, buf);
    }

    @Override
    public void handleClientMessage(NetHandlerPlayClient netHandler)
    {
//        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//        TileEntity tile = player.worldObj.getTileEntity(pos);
//        if (tile instanceof TileController)
//        {
//            ((TileController) tile).addEntry(new Entry((TileController) tile));
//        }
    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
        EntityPlayer player = netHandler.playerEntity;
        TileEntity tile = player.worldObj.getTileEntity(pos);
        if (tile instanceof TileController)
        {
            ((TileController) tile).addEntry(new Entry((TileController) tile));
            ((TileController) tile).selectedEntry = ((TileController) tile).entries.size() - 1;
            BlockPos pos = tile.getPos();
            player.openGui(ProductionTimer.INSTANCE, 1, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
