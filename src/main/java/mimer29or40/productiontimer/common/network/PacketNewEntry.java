package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.Minecraft;
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
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        TileEntity tile = player.worldObj.getTileEntity(pos);
        if (tile instanceof TileController)
        {
            TileController tileController = (TileController) tile;
            tileController.addEntry(new Entry(tileController));
            tileController.selectedEntry = tileController.entries.size() - 1;
        }
    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
//        EntityPlayer player = netHandler.playerEntity;
        TileEntity tile = netHandler.playerEntity.worldObj.getTileEntity(pos);
        if (tile instanceof TileController)
        {
            TileController tileController = (TileController) tile;

            tileController.addEntry(new Entry((TileController) tile));
            tileController.selectedEntry = tileController.entries.size() - 1;

//            WorldServer server = netHandler.playerEntity.getServerWorld();
//            for (EntityPlayer player : server.playerEntities)
//            {
//                if (player.openContainer instanceof ContainerController)
//                {
//                    if (((ContainerController) netHandler.playerEntity.openContainer).sameGui((ContainerController) player.openContainer))
//                    {
//                        PTNetwork.sendTo(this, (EntityPlayerMP) player);
//                    }
//                }
//            }

            // TODO make this work, this gets called before tile gets synced to client so this fails when going from 0 -> 1
//            BlockPos pos = tile.getPos();
//            netHandler.playerEntity.openGui(ProductionTimer.INSTANCE, 1, netHandler.playerEntity.worldObj, pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
