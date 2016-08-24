package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.client.gui.components.GuiComponentButton;
import mimer29or40.productiontimer.client.gui.components.GuiComponentGraphTab;
import mimer29or40.productiontimer.client.gui.components.GuiComponentList;
import mimer29or40.productiontimer.common.container.ContainerController;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketMachineID;
import mimer29or40.productiontimer.common.network.PacketOpenGui;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class GuiController extends GuiMachine
{
    public TileController tileController;

    private GuiComponentListEntry guiListEntry;

    public  int                                   selectedTab = 0;
    private final ArrayList<GuiComponentGraphTab> guiTabs     = new ArrayList<>();

    public  int                                 selectedTimeButton = 0;
    private final ArrayList<GuiComponentButton> guiTimeButtons     = new ArrayList<>();

    private GuiComponentButton buttonRelays;
    private GuiComponentButton buttonEditRelay;
    private GuiComponentButton buttonNewRelay;

    public GuiController(TileController tileController)
    {
        super(new ContainerController(tileController));

        this.tileController = tileController;
        xSize = 256;
        ySize = 256;
    }

    public void setSelectedEntry(int index)
    {
        if (index == tileController.selectedEntry)
            return;
        tileController.selectedEntry = index;
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

        guiListEntry = new GuiComponentListEntry(guiLeft + 7, guiTop + 23, 242, 100, 25);

        guiTabs.add(new GuiComponentGraphTab(0, guiLeft + 7,           guiTop + 127, "Total"));
        guiTabs.add(new GuiComponentGraphTab(1, guiLeft + 7 + 48,      guiTop + 127, "#/sec"));
        guiTabs.add(new GuiComponentGraphTab(2, guiLeft + 7 + 48 + 48, guiTop + 127, "Test"));
        guiTabs.get(selectedTab).selected = true;

        guiTimeButtons.add(new GuiComponentButton(0, guiLeft + 234, guiTop + 147         , 10, 10, null, "5 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(1, guiLeft + 234, guiTop + 147 + 15    , 10, 10, null, "10 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(2, guiLeft + 234, guiTop + 147 + 15 * 2, 10, 10, null, "15 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(3, guiLeft + 234, guiTop + 147 + 15 * 3, 10, 10, null, "30 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(4, guiLeft + 234, guiTop + 147 + 15 * 4, 10, 10, null, "60 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(5, guiLeft + 234, guiTop + 147 + 15 * 5, 10, 10, null, "5 Minutes"));
        guiTimeButtons.get(selectedTimeButton).selected = true;

        buttonRelays = new GuiComponentButton(0, guiLeft + 172, guiTop + 7, 40, 12, "Relays");

        buttonEditRelay = new GuiComponentButton(0, guiLeft + 222, guiTop + 7, 30, 12, "Edit");
        buttonNewRelay = new GuiComponentButton(0, guiLeft + 262, guiTop + 7, 30, 12, "New");
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
                guiTabs.get(selectedTab).selected = false;
                selectedTab = tab.id;
                tab.selected = true;
                break;
            }
        }

        for (GuiComponentButton button : guiTimeButtons)
        {
            if (button.mouseOver(mouseX - 1, mouseY - 1))
            {
                guiTimeButtons.get(selectedTimeButton).selected = false;
                selectedTimeButton = button.id;
                button.selected = true;
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
            Entry newEntry = new Entry(tileController);
            tileController.addEntry(newEntry);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1) this.mc.thePlayer.closeScreen();

        if (textFieldID.isFocused())
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
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        guiListEntry.drawBackgroundLayer(mc, mouseX, mouseY);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString("ID:", 7, 9, 4210752);

        for (GuiComponentButton button : guiTimeButtons)
        {
            button.drawForegroundLayer(mc, mouseX - 1, mouseY - 1);
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
        public void entryClicked(int entry, boolean doubleClick)
        {
            setSelectedEntry(entry);
        }

        @Override
        public boolean isSelected(int index)
        {
            return selectedEntry == index;
        }

        @Override
        protected void drawEntry(int entryId, int entryLeft, int entryTop, int entryBuffer, Tessellator tess)
        {
            Entry entry = tileController.entries.get(entryId);
            String name = entry.getName();
            String inputRelay = entry.inputRelayName;

            fontRendererObj.drawString("Entry Name: " + name, entryLeft + 3, entryTop + 2, 0xFFFFFF);
            fontRendererObj.drawString(inputRelay, entryLeft + 13, entryTop + 12, 0xCCCCCC);

            for (int i = 0; i < entry.itemAmount; i++)
            {
                ItemStack stack = entry.getInputItemStack(i);
                if (stack == null) continue;
                String string = TextFormatting.YELLOW + "" + stack.stackSize;
                drawItem(stack, entryLeft + i * 20 + 7, entryTop + 7);
//                itemRender.renderItemAndEffectIntoGUI(mc.thePlayer, stack, entryLeft + i * 20 + 30, entryTop + 15);
//                itemRender.renderItemOverlayIntoGUI(fontRendererObj, stack, entryLeft + i * 20 + 30, entryTop + 15, string);
            }
        }

        private void drawItem(ItemStack stack, int x, int y)
        {
            if (stack != null && stack.getItem() != null)
            {
                RenderItem itemRenderer = mc.getRenderItem();
                IBakedModel model = itemRenderer.getItemModelMesher().getItemModel(stack);
                model = model.getOverrides().handleItemState(model, stack, (World) null, mc.thePlayer);

                GlStateManager.pushMatrix();
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                GlStateManager.translate((float) x, (float) y, zLevel);
                GlStateManager.translate(8.0F, 8.0F, 0.0F);
                GlStateManager.scale(1.0F, -1.0F, 1.0F);
                GlStateManager.scale(16.0F, 16.0F, 16.0F);
                GlStateManager.disableDepth();
                GlStateManager.enableLighting();

                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);

                itemRenderer.renderItem(stack, model);

                GlStateManager.disableAlpha();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            }
        }
    }
}
