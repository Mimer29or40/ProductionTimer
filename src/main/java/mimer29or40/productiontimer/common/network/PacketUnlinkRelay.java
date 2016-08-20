package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketUnlinkRelay extends AbstractPacketThreadSafe
{
    public BlockPos relayPos;
    public BlockPos controllerPos;

    public PacketUnlinkRelay() {}

    public PacketUnlinkRelay(BlockPos relayPos, BlockPos controllerPos)
    {
        this.relayPos = relayPos;
        this.controllerPos = controllerPos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        relayPos = readPos(buf);
        controllerPos = readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        writePos(relayPos, buf);
        writePos(controllerPos, buf);
    }

    @Override
    public void handleClientSafe(NetHandlerPlayClient netHandler)
    {
        World world = Minecraft.getMinecraft().theWorld;
        TileEntity tile = world.getTileEntity(controllerPos);
        if (tile instanceof TileController)
        {
            ((TileController) tile).unlinkRelay(relayPos);
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer netHandler)
    {
        World world = netHandler.playerEntity.worldObj;
        TileEntity tile = world.getTileEntity(controllerPos);
        if (tile instanceof TileController)
        {
            ((TileController) tile).unlinkRelay(relayPos);
            PTNetwork.sendToAll(this);
        }
    }
}
