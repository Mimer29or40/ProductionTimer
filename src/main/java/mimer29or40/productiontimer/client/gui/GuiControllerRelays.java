package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.ProductionTimer;
import mimer29or40.productiontimer.client.gui.components.GuiComponentButton;
import mimer29or40.productiontimer.client.gui.components.GuiComponentList;
import mimer29or40.productiontimer.common.model.Relay;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketOpenGui;
import mimer29or40.productiontimer.common.network.PacketUnlinkRelay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class GuiControllerRelays extends GuiScreen
{
    private GuiController parent;

    protected int xSize;
    protected int ySize;

    protected int guiLeft;
    protected int guiTop;

    private int selectedRelay = -1;
    private GuiComponentListRelay guiListRelay;

    GuiComponentButton buttonBack;
    GuiComponentButton buttonHighlight;
    GuiComponentButton buttonUnlink;

    public GuiControllerRelays(GuiController parent)
    {
        this.parent = parent;

        xSize = 175;
        ySize = 146;
    }

    @Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        guiListRelay = new GuiComponentListRelay(guiLeft + 7, guiTop + 23, 161, 100, 26);

        buttonBack = new GuiComponentButton(0, guiLeft + 7, guiTop + 7, 40, 12, "Back");
        buttonHighlight = new GuiComponentButton(1, guiLeft + 57, guiTop + 7, 61, 12, "Highlight");
        buttonUnlink = new GuiComponentButton(2, guiLeft + 128, guiTop + 7, 40, 12, "Unlink");
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        guiListRelay.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (buttonBack.mouseOver(mouseX, mouseY))
        {
            PTNetwork.sendToServer(new PacketOpenGui(0, parent.tileController.getPos()));
        }

        if (buttonHighlight.mouseOver(mouseX, mouseY) && guiListRelay.getSelectedEntry() != -1)
        {
            Relay relay = guiListRelay.relayList.get(guiListRelay.getSelectedEntry());
            ProductionTimer.renderHelper.addBlockToHighLight(relay.getPos());
        }

        if (buttonUnlink.mouseOver(mouseX, mouseY) && guiListRelay.getSelectedEntry() != -1)
        {
            Relay relay = guiListRelay.relayList.get(guiListRelay.getSelectedEntry());
            PTNetwork.sendToServer(new PacketUnlinkRelay(relay.getPos(), parent.tileController.getPos()));
            guiListRelay.relayList.remove(guiListRelay.getSelectedEntry());
            guiListRelay.setSelectedEntry(-1);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
//            mc.displayGuiScreen(parent);
            PTNetwork.sendToServer(new PacketOpenGui(0, parent.tileController.getPos()));
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();

        guiListRelay.drawBackgroundLayer(mc, mouseX, mouseY);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation relayGuiTexture = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/controller_relay_list.png");
        mc.getTextureManager().bindTexture(relayGuiTexture);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        buttonBack.drawBackgroundLayer(mc, mouseX, mouseY);
        buttonHighlight.drawBackgroundLayer(mc, mouseX, mouseY);
        buttonUnlink.drawBackgroundLayer(mc, mouseX, mouseY);
    }

    private class GuiComponentListRelay extends GuiComponentList
    {
        private ArrayList<Relay> relayList;

        public GuiComponentListRelay(int left, int top, int width, int height, int entryHeight)
        {
            super(0, left, top, width, height, entryHeight);
            relayList = parent.tileController.linkedRelays;
        }

        @Override
        public int getSize()
        {
            return relayList.size();
        }

        @Override
        public int getSelectedEntry()
        {
            return selectedRelay;
        }

        @Override
        public void setSelectedEntry(int entry)
        {
            selectedRelay = entry;
        }

        @Override
        protected void drawEntry(int entryId, int entryLeft, int entryTop, int entryHeight, int entryWidth)
        {
            Relay relay = relayList.get(entryId);
//            String name = entry.getName();
//            String inputRelay = entry.getInputRelayName();

            fontRendererObj.drawString(relay.getName(), entryLeft + 3, entryTop + 2, 0xFFFFFF);
            String pos = String.format("{%s,%s,%s}", relay.getX(), relay.getY(), relay.getZ());
            fontRendererObj.drawString(pos, entryLeft + 13, entryTop + 13, 0xCCCCCC);
        }
    }
}
