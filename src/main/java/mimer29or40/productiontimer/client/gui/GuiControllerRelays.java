package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.ProductionTimer;
import mimer29or40.productiontimer.client.gui.components.GuiComponentButton;
import mimer29or40.productiontimer.client.gui.components.GuiComponentList;
import mimer29or40.productiontimer.common.model.Relay;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketUnlinkRelay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
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

        if (Mouse.isButtonDown(1) || buttonBack.mouseOver(mouseX, mouseY))
        {
            mc.displayGuiScreen(parent);
        }

        if (buttonHighlight.mouseOver(mouseX, mouseY) && guiListRelay.selectedEntry != -1)
        {
            Relay relay = guiListRelay.relayList.get(guiListRelay.selectedEntry);
            ProductionTimer.renderHelper.addBlockToHighLight(relay.getPos());
        }

        if (buttonUnlink.mouseOver(mouseX, mouseY))
        {
            if (guiListRelay.selectedEntry != -1)
            {
                Relay relay = guiListRelay.relayList.get(guiListRelay.selectedEntry);
                PTNetwork.sendToServer(new PacketUnlinkRelay(relay.getPos(), parent.tileController.getPos()));
                guiListRelay.relayList.remove(guiListRelay.selectedEntry);
                guiListRelay.selectedEntry = -1;
            }
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
            relayList = parent.tileController.getLinkedRelays();
        }

        @Override
        public int getSize()
        {
            return relayList.size();
        }

        @Override
        protected void drawEntry(int entryId, int entryLeft, int entryTop, int entryBuffer, Tessellator tess)
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
