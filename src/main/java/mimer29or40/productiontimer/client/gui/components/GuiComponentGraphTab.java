package mimer29or40.productiontimer.client.gui.components;

import mimer29or40.productiontimer.PTInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiComponentGraphTab extends Gui
{
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/elements.png");
    public boolean enabled;
    public int     id;
    public int     xPosition;
    public int     yPosition;
    public int     width;
    public int     height;
    public String  displayString;

    public GuiComponentGraphTab(int buttonId, int x, int y, String buttonText)
    {
        this.enabled = false;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = 48;
        this.height = 16;
        this.displayString = buttonText;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        FontRenderer fontrenderer = mc.fontRendererObj;
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

        int i = enabled ? 0 : 1;
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 48 * i, 0, this.width, this.height);

        int j = 14737632;
        if (!this.enabled) j = 10526880;

        this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 7) / 2, j);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return xPosition <= mouseX && mouseX < xPosition + width &&
               yPosition <= mouseY && mouseY < yPosition + height;
    }

    public void drawButtonForegroundLayer(Minecraft mc, int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
