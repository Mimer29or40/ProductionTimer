package mimer29or40.productiontimer.common.container;

import mimer29or40.productiontimer.client.gui.GuiMachine;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketMachineID;
import mimer29or40.productiontimer.common.tile.TileMachine;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public abstract class ContainerMachine<T extends TileMachine> extends Container
{
    protected       String   ID;
    protected       T        tile;
    protected final World    world;
    protected final BlockPos pos;
    protected final Block    originalBlock;

    public ContainerMachine(T tile)
    {
        this.tile = tile;

        this.world = tile.getWorld();
        this.pos = tile.getPos();
        this.originalBlock = world.getBlockState(pos).getBlock();
    }

    public String getID()
    {
        return ID;
    }

    public void setID(String ID)
    {
        this.ID = ID;
        tile.setCustomName(ID);

        if (world.isRemote)
        {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen instanceof GuiMachine)
            {
                ((GuiMachine) screen).textFieldID.setText(ID);
            }
        }
    }

    public T getTile()
    {
        return tile;
    }

    public void syncOnOpen(EntityPlayerMP playerOpened)
    {
        // find another player that already has the gui for this tile open
        WorldServer server = playerOpened.getServerWorld();
        for (EntityPlayer player : server.playerEntities)
        {
            if (player == playerOpened)
            {
                continue;
            }
            if (player.openContainer instanceof ContainerMachine)
            {
                if (this.sameGui((ContainerMachine<T>) player.openContainer))
                {
                    syncWithOtherContainer((ContainerMachine<T>) player.openContainer, playerOpened);
                    return;
                }
            }
        }

        // no player has a container open for the tile
        syncNewContainer(playerOpened);
    }

    /**
     * Called when the container is opened and another player already has a container for this tile open
     * Sync to the same state here.
     */
    protected void syncWithOtherContainer(ContainerMachine<T> otherContainer, EntityPlayerMP player)
    {
        // set same selection as other container
        this.setID(otherContainer.ID);
        // also send the data to the player
        if (otherContainer.ID != null && !otherContainer.ID.isEmpty())
        {
            PTNetwork.sendTo(new PacketMachineID(otherContainer.ID), player);
        }
    }

    /**
     * Called when the container is opened and no other player has it open.
     * Set the default state here.
     */
    protected void syncNewContainer(EntityPlayerMP player)
    {

    }

    public boolean sameGui(ContainerMachine otherContainer)
    {
        return this.tile == otherContainer.tile;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
    {
        Block block = world.getBlockState(pos).getBlock();
        // does the block we interacted with still exist?
        if (block == Blocks.AIR || block != originalBlock)
        {
            return false;
        }

        // too far away from block?
        return playerIn.getDistanceSq((double) pos.getX() + 0.5d,
                                      (double) pos.getY() + 0.5d,
                                      (double) pos.getZ() + 0.5d) <= 64;
    }
}
