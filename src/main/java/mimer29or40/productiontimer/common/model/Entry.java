package mimer29or40.productiontimer.common.model;

import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class Entry implements IInventory
{
    private TileController parent;

    public String name = "";

    public String inputRelayName  = "";
    public String outputRelayName = "";
    public int    itemAmount      = 9;

    private ItemStack[] contents = new ItemStack[itemAmount * 2];

    public Entry() {}

    public Entry(String name)
    {
        this.name = name;
    }

    public Entry(TileController parent)
    {
        this.parent = parent;
    }

    public Entry(TileController parent, String name)
    {
        this.parent = parent;
        this.name = name;
    }

    public void setParent(TileController parent)
    {
        this.parent = parent;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    public ItemStack getInputItemStack(int index)
    {
        if (0 <= index && index < itemAmount) return contents[index];
        return null;
    }

    public void setInputItemStack(int index, ItemStack stack)
    {
        if (0 <= index && index < itemAmount) setInventorySlotContents(index, stack);
    }

    public ItemStack getOutputItemStack(int index)
    {
        if (0 <= index && index < itemAmount) return contents[index + itemAmount];
        return null;
    }

    public void setOutputItemStack(int index, ItemStack stack)
    {
        if (0 <= index && index < itemAmount) setInventorySlotContents(index + itemAmount, stack);
    }

    public ItemStack[] getInputItemStacks()
    {
        ItemStack[] stacks = new ItemStack[itemAmount];
        for (int i = 0; i < itemAmount; i++)
        {
            stacks[i] = contents[i];
        }
        return stacks;
    }

    public ItemStack[] getOutputItemStacks()
    {
        ItemStack[] stacks = new ItemStack[itemAmount];
        for (int i = 0; i < itemAmount; i++)
        {
            stacks[i] = contents[i + itemAmount];
        }
        return stacks;
    }

    public ItemStack[] getContents()
    {
        return contents;
    }

    @Override
    public int getSizeInventory()
    {
        return itemAmount * 2;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index)
    {
        if (0 <= index && index < itemAmount * 2) return contents[index];
        return null;
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
        if (0 <= index && index < itemAmount * 2)
        {
            contents[index] = stack;
            markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
        parent.markDirtyClient();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return false;
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
        contents = new ItemStack[itemAmount * 2];
    }
}
