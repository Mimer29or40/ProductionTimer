package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.common.registry.IRegisterGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof IRegisterGui)
        {
            return ((IRegisterGui) te).createContainer(ID, player.inventory, world, new BlockPos(x, y, z));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof IRegisterGui)
        {
            return ((IRegisterGui) te).createGui(ID, player.inventory, world, new BlockPos(x, y, z));
        }
        return null;
    }
}
