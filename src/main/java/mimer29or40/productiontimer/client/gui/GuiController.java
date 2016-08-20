package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.client.gui.components.GuiComponentButton;
import mimer29or40.productiontimer.client.gui.components.GuiComponentGraphTab;
import mimer29or40.productiontimer.client.gui.components.GuiComponentList;
import mimer29or40.productiontimer.common.container.ContainerController;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketMachineID;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class GuiController extends GuiMachine
{
    public TileController tileController;

    public  int              selectedEntry = 0;
    private ArrayList<Entry> entries = new ArrayList<>();
    private EntryList        entryList;

    public  int                                   selectedTab = 0;
    private final ArrayList<GuiComponentGraphTab> guiTabs     = new ArrayList<>();

    public  int                                 selectedTimeButton = 0;
    private final ArrayList<GuiComponentButton> guiTimeButtons     = new ArrayList<>();

    private GuiComponentButton buttonRelays;

    public GuiController(TileController tileController)
    {
        super(new ContainerController(tileController));

        this.tileController = tileController;
        xSize = 256;
        ySize = 256;
    }

    public void setSelectedEntry(int index)
    {
        if (index == selectedEntry)
            return;
        selectedEntry = index;
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

        if (tileController.hasCustomName())
            textFieldID.setText(tileController.getName());

        for (int i = 0; i < 20; i++)
        {
            Entry entry = new Entry("Test" + i);
            entry.setInputRelayName("Relay1");
            entry.setOutputRelayName("Relay2");
            entries.add(entry);
        }
        entryList = new EntryList(guiLeft + 8, guiTop + 24, 240, 98, 25);

        guiTabs.add(new GuiComponentGraphTab(0, guiLeft + 7, guiTop + 127, "Total"));
        guiTabs.add(new GuiComponentGraphTab(1, guiLeft + 7 + 48, guiTop + 127, "#/sec"));
        guiTabs.add(new GuiComponentGraphTab(2, guiLeft + 7 + 48 + 48, guiTop + 127, "Test"));
        guiTabs.get(selectedTab).enabled = true;

        guiTimeButtons.add(new GuiComponentButton(0, guiLeft + 234, guiTop + 147         , 10, 10, null, "5 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(1, guiLeft + 234, guiTop + 147 + 15    , 10, 10, null, "10 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(2, guiLeft + 234, guiTop + 147 + 15 * 2, 10, 10, null, "15 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(3, guiLeft + 234, guiTop + 147 + 15 * 3, 10, 10, null, "30 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(4, guiLeft + 234, guiTop + 147 + 15 * 4, 10, 10, null, "60 Seconds"));
        guiTimeButtons.add(new GuiComponentButton(5, guiLeft + 234, guiTop + 147 + 15 * 5, 10, 10, null, "5 Minutes"));
        guiTimeButtons.get(selectedTimeButton).enabled = true;

//        buttonRelays = new GuiButton(0, guiLeft + 172, guiTop + 7, 50, 10, "Relays");
        buttonRelays = new GuiComponentButton(0, guiLeft + 172, guiTop + 7, 40, 12, "Relays");
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

        entryList.handleMouseInput(mouseX, mouseY);
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
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (!textFieldID.isFocused())
        {
            super.keyTyped(typedChar, keyCode);
        }
        else
        {
            if (keyCode == 1)
            {
                this.mc.thePlayer.closeScreen();
            }

            textFieldID.textboxKeyTyped(typedChar, keyCode);
            PTNetwork.sendToServer(new PacketMachineID(textFieldID.getText()));
            tileController.setCustomName(textFieldID.getText());
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
        entryList.drawBackgroundLayer(mc, mouseX, mouseY);

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

    private class EntryList extends GuiComponentList
    {
        public EntryList(int left, int top, int width, int height, int entryHeight)
        {
            super(0, left, top, width, height, entryHeight);
        }

        @Override
        public int getSize()
        {
            return entries.size();
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
            Entry entry = entries.get(entryId);
            String name = entry.getName();
            String inputRelay = entry.getInputRelayName();

            fontRendererObj.drawString(name, entryLeft + 3, entryTop + 2, 0xFFFFFF);
            fontRendererObj.drawString(inputRelay, entryLeft + 13, entryTop + 12, 0xCCCCCC);
        }
    }
}
