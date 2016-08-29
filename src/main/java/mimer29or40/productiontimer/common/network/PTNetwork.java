package mimer29or40.productiontimer.common.network;

import mimer29or40.productiontimer.PTInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PTNetwork
{
    public static PTNetwork instance = new PTNetwork();

    public final    SimpleNetworkWrapper  network;
    protected final AbstractPacketHandler handler;
    private int id = 0;

    public PTNetwork()
    {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(PTInfo.MOD_ID);
        handler = new AbstractPacketHandler();
    }

    public void setup()
    {
        registerPacket(PacketMachineID.class);
        registerPacket(PacketLinkRelay.class);
        registerPacket(PacketUnlinkRelay.class);
        registerPacket(PacketUpdateEntry.class);
        registerPacket(PacketNewEntry.class);
        registerPacket(PacketUpdateController.class);

        registerPacketServer(PacketOpenGui.class);
        registerPacketServer(PacketDeleteEntry.class);
    }

    public static void sendToAll(AbstractPacket packet)
    {
        instance.network.sendToAll(packet);
    }

    public static void sendTo(AbstractPacket packet, EntityPlayerMP player)
    {
        instance.network.sendTo(packet, player);
    }


    public static void sendToAllAround(AbstractPacket packet, NetworkRegistry.TargetPoint point)
    {
        instance.network.sendToAllAround(packet, point);
    }

    public static void sendToDimension(AbstractPacket packet, int dimensionId)
    {
        instance.network.sendToDimension(packet, dimensionId);
    }

    public static void sendToServer(AbstractPacket packet)
    {
        instance.network.sendToServer(packet);
    }

    public static void sendToClients(WorldServer world, BlockPos pos, AbstractPacket packet)
    {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        for (EntityPlayer player : world.playerEntities)
        {
            // only send to relevant players
            if (!(player instanceof EntityPlayerMP))
            {
                continue;
            }
            EntityPlayerMP playerMP = (EntityPlayerMP) player;
            if (world.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition))
            {
                PTNetwork.sendTo(packet, playerMP);
            }
        }
    }

    /**
     * Packet will be received on both client and server side.
     */
    private void registerPacket(Class<? extends AbstractPacket> packetClazz)
    {
        registerPacketClient(packetClazz);
        registerPacketServer(packetClazz);
    }

    /**
     * Packet will only be received on the client side
     */
    private void registerPacketClient(Class<? extends AbstractPacket> packetClazz)
    {
        registerPacketImpl(packetClazz, Side.CLIENT);
    }

    /**
     * Packet will only be received on the server side
     */
    private void registerPacketServer(Class<? extends AbstractPacket> packetClazz)
    {
        registerPacketImpl(packetClazz, Side.SERVER);
    }

    private void registerPacketImpl(Class<? extends AbstractPacket> packetClazz, Side side)
    {
        network.registerMessage(handler, packetClazz, id++, side);
    }

    private class AbstractPacketHandler implements IMessageHandler<AbstractPacket, IMessage>
    {
        @Override
        public IMessage onMessage(AbstractPacket packet, MessageContext ctx)
        {
            if (ctx.side == Side.SERVER)
            {
                return packet.handleServer(ctx.getServerHandler());
            }
            else
            {
                return packet.handleClient(ctx.getClientHandler());
            }
        }
    }
}
