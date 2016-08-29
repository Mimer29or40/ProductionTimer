package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.client.gui.components.GuiComponentButton;
import mimer29or40.productiontimer.client.gui.components.GuiComponentGraphTab;
import mimer29or40.productiontimer.client.gui.components.GuiComponentList;
import mimer29or40.productiontimer.common.container.ContainerController;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.network.*;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class GuiController extends GuiMachine
{
    public TileController tileController;

    private GuiComponentListEntry guiListEntry;

    public final ArrayList<GuiComponentGraphTab> guiTabs        = new ArrayList<>();
    public final ArrayList<GuiComponentButton>   guiTimeButtons = new ArrayList<>();

    private GuiComponentButton buttonRelays;
    private GuiComponentButton buttonEditRelay;
    private GuiComponentButton buttonNewRelay;
    private GuiComponentButton buttonDeleteRelay;

    public GuiController(TileController tileController)
    {
        super(new ContainerController(tileController));

        this.tileController = tileController;
        xSize = 256;
        ySize = 256;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        textFieldID = new GuiTextField(0, fontRendererObj, guiLeft + 22, guiTop + 9, 145, 8);
        textFieldID.setCanLoseFocus(true);
        textFieldID.setEnableBackgroundDrawing(false);
        textFieldID.setMaxStringLength(40);

        if (tileController.hasCustomName()) textFieldID.setText(tileController.getName());

        guiListEntry = new GuiComponentListEntry(guiLeft + 7, guiTop + 23, 242, 100, 30);

        guiTabs.add(new GuiComponentGraphTab(0, guiLeft + 7,           guiTop + 127, "Total"));
        guiTabs.add(new GuiComponentGraphTab(1, guiLeft + 7 + 48,      guiTop + 127, "#/sec"));
        guiTabs.add(new GuiComponentGraphTab(2, guiLeft + 7 + 48 + 48, guiTop + 127, "Test"));
        guiTabs.get(tileController.selectedTab).selected = true;

        guiTimeButtons.add(new GuiComponentButton(0, guiLeft + 234, guiTop + 147         , 10, 10, null, "5 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(1, guiLeft + 234, guiTop + 147 + 15    , 10, 10, null, "10 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(2, guiLeft + 234, guiTop + 147 + 15 * 2, 10, 10, null, "15 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(3, guiLeft + 234, guiTop + 147 + 15 * 3, 10, 10, null, "30 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(4, guiLeft + 234, guiTop + 147 + 15 * 4, 10, 10, null, "60 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(5, guiLeft + 234, guiTop + 147 + 15 * 5, 10, 10, null, "5 Minutes"));
        guiTimeButtons.get(tileController.selectedTimeScale).selected = true;

        buttonRelays = new GuiComponentButton(0, guiLeft + 172, guiTop + 7, 40, 12, "Relays");

        buttonEditRelay = new GuiComponentButton(0, guiLeft + 222, guiTop + 7, 30, 12, "Edit");
        buttonNewRelay = new GuiComponentButton(0, guiLeft + 262, guiTop + 7, 30, 12, "New");
        buttonDeleteRelay = new GuiComponentButton(0, guiLeft + 302, guiTop + 7, 30, 12, "Delete");
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        guiListEntry.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textFieldID.mouseClicked(mouseX, mouseY, mouseButton);

        for (GuiComponentGraphTab tab : guiTabs)
        {
            if (tab.mouseOver(mouseX - 1, mouseY - 1))
            {
                guiTabs.get(tileController.selectedTab).selected = false;
                tab.selected = true;
                PTNetwork.sendToServer(new PacketUpdateController(tileController, null, tab.id, null));
                break;
            }
        }

        for (GuiComponentButton button : guiTimeButtons)
        {
            if (button.mouseOver(mouseX - 1, mouseY - 1))
            {
                guiTimeButtons.get(tileController.selectedTimeScale).selected = false;
                button.selected = true;
                PTNetwork.sendToServer(new PacketUpdateController(tileController, null, null, button.id));
                break;
            }
        }

        if (buttonRelays.mouseOver(mouseX - 1, mouseY - 1))
        {
            mc.displayGuiScreen(new GuiControllerRelays(this));
        }

        if (buttonEditRelay.mouseOver(mouseX - 1, mouseY - 1))
        {
            if (tileController.selectedEntry != -1)
            {
                PTNetwork.sendToServer(new PacketOpenGui(1, tileController.getPos()));
            }
        }

        if (buttonNewRelay.mouseOver(mouseX - 1, mouseY - 1))
        {
            PTNetwork.sendToServer(new PacketNewEntry(tileController.getPos()));
        }

        if (buttonDeleteRelay.mouseOver(mouseX - 1, mouseY - 1))
        {
            if (tileController.selectedEntry != -1)
            {
                PTNetwork.sendToServer(new PacketDeleteEntry(tileController.getPos(), tileController.selectedEntry));
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            mc.thePlayer.closeScreen();
        }
        else if (textFieldID.isFocused())
        {
            textFieldID.textboxKeyTyped(typedChar, keyCode);
            PTNetwork.sendToServer(new PacketMachineID(textFieldID.getText()));
            tileController.setCustomName(textFieldID.getText());
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
        textFieldID.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        zLevel = 0;
        drawDefaultBackground();

        guiListEntry.drawBackgroundLayer(mc, mouseX, mouseY);

        zLevel = 1;
        drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        GlStateManager.translate(guiLeft, guiTop, 0.0F);

        drawGuiContainerForegroundLayer(mouseX, mouseY);

        GlStateManager.disableDepth();
        GlStateManager.translate(-guiLeft, -guiTop, 0.0F);
        String mousePos = String.format("(%s,%s)", mouseX, mouseY);
        fontRendererObj.drawString(mousePos, 2, 2, 0xFFFFFFFF);
        drawVerticalLine(mouseX, 0, mouseY, 0xFFFFFFFF);
        drawHorizontalLine(0, mouseX, mouseY, 0xFFFFFFFF);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableDepth();

        ResourceLocation relayGuiTexture = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/controller.png");
        mc.getTextureManager().bindTexture(relayGuiTexture);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        textFieldID.drawTextBox();

        for (GuiComponentGraphTab tab : guiTabs)
        {
            tab.drawBackgroundLayer(mc, mouseX, mouseY);
        }

        for (GuiComponentButton button : guiTimeButtons)
        {
            button.drawBackgroundLayer(mc, mouseX, mouseY);
        }

        buttonRelays.drawBackgroundLayer(mc, mouseX, mouseY);
        buttonEditRelay.drawBackgroundLayer(mc, mouseX, mouseY);
        buttonNewRelay.drawBackgroundLayer(mc, mouseX, mouseY);
        buttonDeleteRelay.drawBackgroundLayer(mc, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString("ID:", 7, 9, 4210752);

        for (GuiComponentButton button : guiTimeButtons)
        {
            button.drawForegroundLayer(mc, mouseX, mouseY);
        }
    }

    private class GuiComponentListEntry extends GuiComponentList
    {
        public GuiComponentListEntry(int left, int top, int width, int height, int entryHeight)
        {
            super(0, left, top, width, height, entryHeight);
        }

        @Override
        public int getSize()
        {
            return tileController.entries.size();
        }

        @Override
        public int getSelectedEntry()
        {
            return tileController.selectedEntry;
        }

        @Override
        public void setSelectedEntry(int entry)
        {
            PTNetwork.sendToServer(new PacketUpdateController(tileController, entry, null, null));
        }

        @Override
        protected void drawEntry(int entryId, int entryLeft, int entryTop, int entryHeight, int entryWidth)
        {
            // TODO make pretty
            Entry entry = tileController.entries.get(entryId);
            String name = entry.getName();
            String inputRelay = entry.inputRelayName;

            fontRendererObj.drawString("Entry Name: " + name, entryLeft, entryTop, 0xFFFFFF);
            fontRendererObj.drawString(inputRelay, entryLeft + 13, entryTop + 12, 0xCCCCCC);

            for (int i = 0; i < entry.itemAmount; i++)
            {
                ItemStack stack = entry.getInputItemStack(i);
                if (stack == null) continue;
                drawItem(stack, entryLeft + i * 20 + 7, entryTop + 7);
            }
        }

        private void drawItem(ItemStack stack, int x, int y)
        {
            if (stack != null && stack.getItem() != null)
            {

                IBakedModel model = itemRender.getItemModelMesher().getItemModel(stack);
                model = model.getOverrides().handleItemState(model, stack, null, mc.thePlayer);

                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

                RenderHelper.enableGUIStandardItemLighting();

                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();

                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1F);

                GlStateManager.translate(x + 8.0F, y + 8.0F, zLevel + 5);
                GlStateManager.scale(16.0F, -16.0F, 16.0F);

                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);

                itemRender.renderItem(stack, model);

                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();

                GlStateManager.disableRescaleNormal();
                GlStateManager.popMatrix();

                RenderHelper.disableStandardItemLighting();

                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

                if (stack.stackSize > 1)
                {
                    String itemAmount = TextFormatting.WHITE + "" + stack.stackSize;
                    float textPosX = (float) x + 17;
                    float textPosY = (float) y + 9;
                    GlStateManager.disableDepth();
                    drawTextLeft(itemAmount, textPosX, textPosY, 16777215, true);
                    GlStateManager.enableDepth();
                }
            }
        }
    }
}
