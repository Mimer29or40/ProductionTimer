package mimer29or40.productiontimer.client.gui;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.common.container.ContainerRelay;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketMachineID;
import mimer29or40.productiontimer.common.tile.TileController;
import mimer29or40.productiontimer.common.tile.TileRelay;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiRelay extends GuiMachine
{
    public TileRelay tileRelay;
    private String controllerID;

    public GuiRelay(IInventory playerInv, TileRelay tileRelay)
    {
        super(new ContainerRelay(playerInv, tileRelay));

        this.tileRelay = tileRelay;
        xSize = 176;
        ySize = 180;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        textFieldID = new GuiTextField(0, fontRendererObj, guiLeft + 22, guiTop + 9, 145, 8);
        //textFieldID.setFocused(true);
        textFieldID.setCanLoseFocus(true);
        textFieldID.setEnableBackgroundDrawing(false);
        textFieldID.setMaxStringLength(40);

        if (tileRelay.hasCustomName())
            textFieldID.setText(tileRelay.getName());

        if (tileRelay.getController() != null)
        {
            TileController tile = tileRelay.getController();
            controllerID = tile.getName();
        }
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
        textFieldID.mouseClicked(mouseX, mouseY, mouseButton);
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
            tileRelay.setCustomName(textFieldID.getText());
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        textFieldID.updateCursorCounter();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation relayGuiTexture = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/relay.png");
        this.mc.getTextureManager().bindTexture(relayGuiTexture);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        textFieldID.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString("ID:", 7, 9, 4210752);
        int l = this.fontRendererObj.drawString("Linked To", 7, 83, 4210752);
        this.fontRendererObj.drawString(controllerID, 7 + l, 83, 4210752);
    }
}
