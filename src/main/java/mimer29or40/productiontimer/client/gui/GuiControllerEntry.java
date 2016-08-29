package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.common.container.ContainerEntry;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketOpenGui;
import mimer29or40.productiontimer.common.network.PacketUpdateEntry;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiControllerEntry extends GuiContainer
{
    private GuiController parent;
    private Entry entry;

    private GuiTextField entryName;
    private GuiTextField entryInputRelay;
    private GuiTextField entryOutputRelay;

    public GuiControllerEntry(GuiController parent, IInventory playerInv, Entry entry)
    {
        super(new ContainerEntry(parent.tileController, playerInv, entry));

        this.parent = parent;
        this.entry = entry;

        xSize = 176;
        ySize = 182;
    }

    public void updateEntry(Entry newEntry)
    {
        entry.name = newEntry.name;
        entry.inputRelayName = newEntry.inputRelayName;
        entry.outputRelayName = newEntry.outputRelayName;

        entryName.setText(newEntry.name);
        entryInputRelay.setText(newEntry.inputRelayName);
        entryOutputRelay.setText(newEntry.outputRelayName);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        entryName = new GuiTextField(0, fontRendererObj, guiLeft + 36, guiTop + 9, 132, 8);
        entryName.setCanLoseFocus(true);
        entryName.setEnableBackgroundDrawing(false);
        entryName.setMaxStringLength(40);

        if (!entry.getName().isEmpty()) entryName.setText(entry.name);

        entryInputRelay = new GuiTextField(1, fontRendererObj, guiLeft + 75, guiTop + 25, 93, 8);
        entryInputRelay.setCanLoseFocus(true);
        entryInputRelay.setEnableBackgroundDrawing(false);
        entryInputRelay.setMaxStringLength(40);

        if (!entry.inputRelayName.isEmpty()) entryInputRelay.setText(entry.inputRelayName);

        entryOutputRelay = new GuiTextField(1, fontRendererObj, guiLeft + 75, guiTop + 63, 93, 8);
        entryOutputRelay.setCanLoseFocus(true);
        entryOutputRelay.setEnableBackgroundDrawing(false);
        entryOutputRelay.setMaxStringLength(40);

        if (!entry.outputRelayName.isEmpty()) entryOutputRelay.setText(entry.outputRelayName);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        entryName.mouseClicked(mouseX, mouseY, mouseButton);
        entryInputRelay.mouseClicked(mouseX, mouseY, mouseButton);
        entryOutputRelay.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            PTNetwork.sendToServer(new PacketOpenGui(0, parent.tileController.getPos()));
        }
        else if (entryName.isFocused())
        {
            entryName.textboxKeyTyped(typedChar, keyCode);
            entry.name = entryName.getText();
            PTNetwork.sendToServer(new PacketUpdateEntry(parent.tileController.getPos(), parent.tileController.selectedEntry, entry));
        }
        else if (entryInputRelay.isFocused())
        {
            entryInputRelay.textboxKeyTyped(typedChar, keyCode);
            entry.inputRelayName = entryInputRelay.getText();
            PTNetwork.sendToServer(new PacketUpdateEntry(parent.tileController.getPos(), parent.tileController.selectedEntry, entry));
        }
        else if (entryOutputRelay.isFocused())
        {
            entryOutputRelay.textboxKeyTyped(typedChar, keyCode);
            entry.outputRelayName = entryOutputRelay.getText();
            PTNetwork.sendToServer(new PacketUpdateEntry(parent.tileController.getPos(), parent.tileController.selectedEntry, entry));
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        entryName.updateCursorCounter();
        entryInputRelay.updateCursorCounter();
        entryOutputRelay.updateCursorCounter();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation relayGuiTexture = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/controller_entry.png");
        this.mc.getTextureManager().bindTexture(relayGuiTexture);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        entryName.drawTextBox();
        entryInputRelay.drawTextBox();
        entryOutputRelay.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString("Name:", 7, 9, 4210752);
        fontRendererObj.drawString("Input Relay:", 7, 25, 4210752);
        fontRendererObj.drawString("Output Relay:", 7, 63, 4210752);
    }
}
