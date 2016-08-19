package mimer29or40.productiontimer.common.model;

import net.minecraft.item.ItemStack;

public class Entry
{
    private String name;

    private String inputRelayName;
    private ItemStack[] inputItemStacks = new ItemStack[8];

    private String outputRelayName;
    private ItemStack[] outputItemStacks = new ItemStack[8];

    public Entry(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getInputRelayName()
    {
        return inputRelayName;
    }

    public void setInputRelayName(String name)
    {
        inputRelayName = name;
    }

    public ItemStack[] getInputItemStacks()
    {
        return inputItemStacks;
    }

    public void setInputItemStack(int index, ItemStack itemStack)
    {
        if (index < 0 || index > 8) return;

        inputItemStacks[index] = itemStack;
    }

    public String getOutputRelayName()
    {
        return outputRelayName;
    }

    public void setOutputRelayName(String name)
    {
        outputRelayName = name;
    }

    public ItemStack[] getOutputItemStacks()
    {
        return outputItemStacks;
    }

    public void setOutputItemStack(int index, ItemStack itemStack)
    {
        if (index < 0 || index > 8) return;

        outputItemStacks[index] = itemStack;
    }
}
