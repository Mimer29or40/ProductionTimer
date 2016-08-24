package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.common.tile.TileRelay;
import mimer29or40.productiontimer.common.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketLinkRelay extends AbstractPacket
{
    public BlockPos relayPos;
    public BlockPos controllerPos;

    public PacketLinkRelay() {}

    public PacketLinkRelay(BlockPos relayPos, BlockPos controllerPos)
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
    public void handleClientMessage(NetHandlerPlayClient netHandler)
    {
        World world = Minecraft.getMinecraft().theWorld;
        TileEntity tile = world.getTileEntity(relayPos);
        if (tile instanceof TileRelay)
        {
            ((TileRelay) tile).linkController(controllerPos);
            Log.info("Client %s", ((TileRelay) tile).getLinkedPos());
        }
    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
        World world = netHandler.playerEntity.worldObj;
        TileEntity tile = world.getTileEntity(relayPos);
        if (tile instanceof TileRelay)
        {
            ((TileRelay) tile).linkController(controllerPos);
            PTNetwork.sendToAll(this);
            Log.info("Server %s", ((TileRelay) tile).getLinkedPos());
        }
    }
}
