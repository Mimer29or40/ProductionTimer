package mimer29or40.productiontimer.common.container;

import mimer29or40.productiontimer.common.tile.TileRelay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ContainerRelay extends ContainerMachine<TileRelay>
{
    public ContainerRelay(IInventory playerInventory, TileRelay inventoryRelay)
    {
        super(inventoryRelay);

        // Input
        addSlotToContainer(new Slot(inventoryRelay, 0, 44, 42));

        // Output
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                addSlotToContainer(new Slot(inventoryRelay, j + i * 3 + 1, 98 + j * 18, 24 + i * 18));
            }
        }

        // Player Inventory
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 98 + i * 18));
            }
        }

        // Hot Bar
        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 156));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tile.isUseableByPlayer(playerIn);
    }

    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 10)
            {
                if (!this.mergeItemStack(itemstack1, 10, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 1, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        tile.closeInventory(playerIn);
    }
}
