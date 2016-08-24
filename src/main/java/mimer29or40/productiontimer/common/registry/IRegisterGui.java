package mimer29or40.productiontimer.common.registry;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRegisterGui
{
    Container createContainer(int id, InventoryPlayer inventoryplayer, World world, BlockPos pos);

    @SideOnly(Side.CLIENT)
    GuiScreen createGui(int id, InventoryPlayer inventoryplayer, World world, BlockPos pos);
}
