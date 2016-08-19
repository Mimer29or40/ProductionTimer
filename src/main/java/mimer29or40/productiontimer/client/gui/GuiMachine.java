package mimer29or40.productiontimer.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiMachine extends GuiContainer
{
    public GuiTextField textFieldID;

    public GuiMachine(Container inventorySlotsIn)
    {
        super(inventorySlotsIn);
    }
}
