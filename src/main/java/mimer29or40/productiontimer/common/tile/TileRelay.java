package mimer29or40.productiontimer.common.tile;

import mimer29or40.productiontimer.PTNames;
import mimer29or40.productiontimer.client.gui.GuiRelay;
import mimer29or40.productiontimer.common.container.ContainerRelay;
import mimer29or40.productiontimer.common.model.ConnectionType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileRelay extends TileMachine implements ISidedInventory
{
    private static final String TAG_LINKED_X = "LinkedX";
    private static final String TAG_LINKED_Y = "LinkedY";
    private static final String TAG_LINKED_Z = "LinkedZ";

    private static final String TAG_ITEMS = "Items";
    private static final String TAG_SLOT  = "Slot";

    public int x = 0, y = -1, z = 0;

    private ItemStack[] contents = new ItemStack[getSizeInventory()];

    public void setLinkedPos(@Nullable BlockPos pos)
    {
        if (pos != null)
        {
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
        }
        else
        {
            x = 0;
            y = -1;
            z = 0;
        }
        markDirtyClient();
    }

    @Nullable
    public BlockPos getLinkedPos()
    {
        if (y == -1) return null;

        return new BlockPos(x, y, z);
    }

    public final TileController getController()
    {
        BlockPos linkedPos = getLinkedPos();
        if (linkedPos == null)
            return null;

        if (worldObj.isAirBlock(linkedPos))
            return null;

        TileEntity tile = worldObj.getTileEntity(linkedPos);

        if (!(tile instanceof TileController))
            return null;

//        if (Math.abs(x - getPos().getX()) > getMaxDistance() ||
//            Math.abs(y - getPos().getY()) > getMaxDistance() ||
//            Math.abs(z - getPos().getY()) > getMaxDistance())
//        {
//            y = -1;
//            return null;
//        }

        return (TileController) tile;
    }

    /*
        Return true if block was linked
    */
    public ConnectionType linkController(BlockPos pos)
    {
        if (pos == null)
            return ConnectionType.FAILED;

        TileEntity tile = worldObj.getTileEntity(pos);

        if (!(tile instanceof TileController))
            return ConnectionType.NOTCONTROLLER;

        ConnectionType connectionToRelay = ((TileController) tile).linkRelay(getPos());

        if (connectionToRelay.isSuccess())
            setLinkedPos(pos);

        return connectionToRelay;
    }

    public void unlinkController()
    {
        setLinkedPos(null);
        markDirtyClient();
    }

    public void readCustomNBT(NBTTagCompound compound)
    {
        super.readCustomNBT(compound);

        if (compound == null)
            return;

        setLinkedPos(new BlockPos(compound.getInteger(TAG_LINKED_X), compound.getInteger(TAG_LINKED_Y), compound.getInteger(TAG_LINKED_Z)));

        contents = new ItemStack[getSizeInventory()];

        NBTTagList nbtTagList = compound.getTagList(TAG_ITEMS, 10);

        for (int i = 0; i < nbtTagList.tagCount(); i++)
        {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
            int j = nbtTagCompound.getByte(TAG_SLOT) & 255;

            if (j >= 0 && j < contents.length)
                contents[j] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
        }
    }

    public void writeCustomNBT(NBTTagCompound compound)
    {
        super.writeCustomNBT(compound);

        compound.setInteger(TAG_LINKED_X, x);
        compound.setInteger(TAG_LINKED_Y, y);
        compound.setInteger(TAG_LINKED_Z, z);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.contents.length; ++i)
        {
            if (this.contents[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte(TAG_SLOT, (byte) i);
                this.contents[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag(TAG_ITEMS, nbttaglist);
    }

    @Override
    public String getDefaultName()
    {
        return PTNames.Tile.RELAY;
    }

    @Override
    public void update()
    {
        ItemStack inputStack = contents[0];
        if (inputStack != null)
        {
            for (int i = 1; i < 10; i++)
            {
                ItemStack outputStack = contents[i];

                if (outputStack == null)
                {
                    setInventorySlotContents(i, inputStack);
                    setInventorySlotContents(0, null);
                    break;
                }

                NBTTagCompound inputTag = inputStack.serializeNBT();
                NBTTagCompound outputTag = outputStack.serializeNBT();

                if (inputStack.isItemEqual(outputStack) && inputTag.equals(outputTag) && inputStack.isStackable())
                {
                    int outputStackSizeRemaining = outputStack.getMaxStackSize() - outputStack.stackSize;

                    if (outputStackSizeRemaining > 0)
                    {
                        if (inputStack.stackSize <= outputStackSizeRemaining)
                        {
                            outputStack.stackSize += inputStack.stackSize;
                            setInventorySlotContents(0, null);
                            markDirty();
                            break;
                        }
                        else
                        {
                            outputStack.stackSize += outputStackSizeRemaining;
                            inputStack.stackSize -= outputStackSizeRemaining;

                            if (inputStack.stackSize == 0)
                            {
                                setInventorySlotContents(0, null);
                                markDirty();
                                break;
                            }
                            markDirty();
                        }
                    }
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return index != 0;
    }

    @Override
    public int getSizeInventory()
    {
        return 10;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index)
    {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return contents[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemStack = ItemStackHelper.getAndSplit(contents, index, count);

        if (itemStack != null) markDirty();

        return itemStack;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(contents, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        contents[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();

        markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D,
                                                                           (double) this.pos.getY() + 0.5D,
                                                                           (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < contents.length; ++i)
        {
            setInventorySlotContents(i, null);
        }
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryplayer, World world, BlockPos pos)
    {
        return new ContainerRelay(inventoryplayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen createGui(InventoryPlayer inventoryplayer, World world, BlockPos pos)
    {
        return new GuiRelay(inventoryplayer, this);
    }
}
