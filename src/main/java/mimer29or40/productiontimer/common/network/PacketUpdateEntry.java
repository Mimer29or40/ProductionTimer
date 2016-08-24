package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.common.container.ContainerEntry;
import mimer29or40.productiontimer.common.model.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketUpdateEntry extends AbstractPacket
{
    private BlockPos pos;
    private int      selectedEntry;

    private String name;
    private String inputRelayName;
    private String outputRelayName;

    public PacketUpdateEntry() {}

    public PacketUpdateEntry(BlockPos pos, int selectedEntry, Entry entry)
    {
        this.pos = pos;
        this.selectedEntry = selectedEntry;

        this.name = entry.name;
        this.inputRelayName = entry.inputRelayName;
        this.outputRelayName = entry.outputRelayName;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = readPos(buf);
        selectedEntry = buf.readInt();

        name = ByteBufUtils.readUTF8String(buf);
        inputRelayName = ByteBufUtils.readUTF8String(buf);
        outputRelayName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        writePos(pos, buf);
        buf.writeInt(selectedEntry);

        ByteBufUtils.writeUTF8String(buf, name);
        ByteBufUtils.writeUTF8String(buf, inputRelayName);
        ByteBufUtils.writeUTF8String(buf, outputRelayName);
    }

    @Override
    public void handleClientMessage(NetHandlerPlayClient netHandler)
    {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        if (container instanceof ContainerEntry)
        {
            Entry newEntry = new Entry(name);
            newEntry.inputRelayName = inputRelayName;
            newEntry.outputRelayName = outputRelayName;

            ((ContainerEntry) container).updateEntry(selectedEntry, newEntry);
        }
    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
        Container container = netHandler.playerEntity.openContainer;
        if (container instanceof ContainerEntry)
        {
            Entry newEntry = new Entry(name);
            newEntry.inputRelayName = inputRelayName;
            newEntry.outputRelayName = outputRelayName;

            ((ContainerEntry) container).updateEntry(selectedEntry, newEntry);

//            WorldServer server = netHandler.playerEntity.getServerWorld();
//            for (EntityPlayer player : server.playerEntities)
//            {
//                if (player.openContainer instanceof ContainerMachine)
//                {
//                    if (((ContainerEntry) container).sameGui((ContainerMachine) player.openContainer))
//                    {
//                        PTNetwork.sendTo(this, (EntityPlayerMP) player);
//                    }
//                }
//            }
        }
    }
}
