package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.client.gui.components.GuiComponentList;
import mimer29or40.productiontimer.common.model.Relay;
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

    private RelayList relayList;

    public GuiControllerRelays(GuiController parent)
    {
        this.parent = parent;

        xSize = 175;
        ySize = 130;
    }

    @Override
    public void initGui()
    {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        relayList = new RelayList(this, guiLeft + 8, guiTop + 24, 159, 98, 20);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        relayList.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (Mouse.isButtonDown(1))
        {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();

        relayList.drawEntryList(mouseX, mouseY);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation relayGuiTexture = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/controller_relay_list.png");
        mc.getTextureManager().bindTexture(relayGuiTexture);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    }

    private class RelayList extends GuiComponentList
    {
//        private HashMap<String, BlockPos> relayList;
        private ArrayList<Relay> relayList;

        public RelayList(GuiControllerRelays parent, int left, int top, int width, int height, int entryHeight)
        {
            super(mc, parent.width, parent.height, left, top, width, height, entryHeight);
            relayList = parent.parent.tileController.getLinkedRelays();
        }

        @Override
        public int getSize()
        {
            return relayList.size();
        }

        @Override
        public void entryClicked(int index, boolean doubleClick)
        {

        }

        @Override
        public boolean isSelected(int index)
        {
            return false;
        }

        @Override
        protected void drawEntry(int entryId, int entryLeft, int entryTop, int entryBuffer, Tessellator tess)
        {
            Relay relay = relayList.get(entryId);
//            String name = entry.getName();
//            String inputRelay = entry.getInputRelayName();

            fontRendererObj.drawString(relay.getName(), entryLeft + 3, entryTop + 2, 0xFFFFFF);
            fontRendererObj.drawString(relay.getPos().toString(), entryLeft + 13, entryTop + 12, 0xCCCCCC);
        }
    }
}
