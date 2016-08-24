package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.common.container.ContainerMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketMachineID extends AbstractPacket
{
    public String ID;

    public PacketMachineID() {}

    public PacketMachineID(String ID)
    {
        this.ID = ID;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        ID = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, ID);
    }

    @Override
    public void handleClientMessage(NetHandlerPlayClient netHandler)
    {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        if (container instanceof ContainerMachine)
        {
            ((ContainerMachine) container).setID(ID);
        }
    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
        Container container = netHandler.playerEntity.openContainer;
        if (container instanceof ContainerMachine)
        {
            ((ContainerMachine) container).setID(ID);

            WorldServer server = netHandler.playerEntity.getServerWorld();
            for (EntityPlayer player : server.playerEntities)
            {
                if (player.openContainer instanceof ContainerMachine)
                {
                    if (((ContainerMachine) container).sameGui((ContainerMachine) player.openContainer))
                    {
                        PTNetwork.sendTo(this, (EntityPlayerMP) player);
                    }
                }
            }
        }
    }
}
