package mimer29or40.productiontimer.common.container;

import mimer29or40.productiontimer.client.gui.GuiControllerEntry;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ContainerEntry extends Container
{
    private TileController parent;
    private Entry entry;

    public ContainerEntry(TileController parent, IInventory playerInv, Entry entry)
    {
        this.parent = parent;
        this.entry = entry;

        for (int i = 0; i < entry.itemAmount; i++)
        {
            addSlotToContainer(new Slot(entry, i, 8 + i * 18, 40));
        }

        for (int i = 0; i < entry.itemAmount; i++)
        {
            addSlotToContainer(new Slot(entry, i + 9, 8 + i * 18, 78));
        }

        // Player Inventory
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 100 + i * 18));
            }
        }

        // Hot Bar
        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 158));
        }
    }

    public void updateEntry(int index, Entry newEntry)
    {
        if (newEntry != null)
        {
            entry.name = newEntry.name;
            entry.inputRelayName = newEntry.inputRelayName;
            entry.outputRelayName = newEntry.outputRelayName;

            parent.entries.get(index).name = newEntry.name;
            parent.entries.get(index).inputRelayName = newEntry.inputRelayName;
            parent.entries.get(index).outputRelayName = newEntry.outputRelayName;
        }

        if (parent.getWorld().isRemote)
        {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen instanceof GuiControllerEntry)
            {
                ((GuiControllerEntry) screen).updateEntry(newEntry);
            }
        }
    }

    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
//        ItemStack itemstack = null;
//        Slot slot = inventorySlots.get(index);
//
//        if (slot != null && slot.getHasStack())
//        {
//            ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//
//            if (index < 10)
//            {
//                if (!this.mergeItemStack(itemstack1, 10, this.inventorySlots.size(), true))
//                {
//                    return null;
//                }
//            }
//            else if (!this.mergeItemStack(itemstack1, 0, 1, false))
//            {
//                return null;
//            }
//
//            if (itemstack1.stackSize == 0)
//            {
//                slot.putStack(null);
//            }
//            else
//            {
//                slot.onSlotChanged();
//            }
//        }
//
//        return itemstack;
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }
}
