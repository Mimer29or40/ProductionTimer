package mimer29or40.productiontimer.common.network;

import io.netty.buffer.ByteBuf;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class PacketUpdateController extends AbstractPacket
{
    private BlockPos pos;
    private int entry;
    private int tab;
    private int timeScale;

    public PacketUpdateController() {}

    public PacketUpdateController(TileController tileController, @Nullable Integer entry, @Nullable Integer tab, @Nullable Integer timeScale)
    {
        this.pos = tileController.getPos();

        if (entry == null)
            this.entry = tileController.selectedEntry;
        else
            this.entry = entry;

        if (tab == null)
            this.tab = tileController.selectedTab;
        else
            this.tab = tab;

        if (timeScale == null)
            this.timeScale = tileController.selectedTimeScale;
        else
            this.timeScale = timeScale;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = readPos(buf);
        entry = buf.readInt();
        tab = buf.readInt();
        timeScale = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        writePos(pos, buf);
        buf.writeInt(entry);
        buf.writeInt(tab);
        buf.writeInt(timeScale);
    }

    @Override
    public void handleClientMessage(NetHandlerPlayClient netHandler)
    {
//        TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos);
//        if (tile instanceof TileController)
//        {
//            ((TileController) tile).selectedEntry = entry;
//        }
//        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
//        if (screen instanceof GuiController)
//        {
//
//            ((GuiController) screen).guiTabs.get(((GuiController) screen).tileController.selectedTab).selected = false;
//            selectedTab = tab.id;
//            tab.selected = true;
//        }
    }

    @Override
    public void handleServerMessage(NetHandlerPlayServer netHandler)
    {
        TileEntity tile = netHandler.playerEntity.worldObj.getTileEntity(pos);
        if (tile instanceof TileController)
        {
            ((TileController) tile).selectedEntry = entry;
            ((TileController) tile).selectedTab = tab;
            ((TileController) tile).selectedTimeScale = timeScale;
            ((TileController) tile).markDirtyClient();
        }
    }
}
