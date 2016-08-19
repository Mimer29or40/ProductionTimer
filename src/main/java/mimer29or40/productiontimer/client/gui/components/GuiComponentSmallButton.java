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
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.Arrays;

public class GuiComponentSmallButton extends Gui
{
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/elements.png");
    public boolean enabled;
    public int     id;
    public int     left;
    public int     top;
    public int     width;
    public int     height;
    public String  toolTipText;

    public GuiComponentSmallButton(int buttonId, int left, int top, String toolTipText)
    {
        this.enabled = false;
        this.id = buttonId;
        this.left = left;
        this.top = top;
        this.width = 10;
        this.height = 10;
        this.toolTipText = toolTipText;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

        int i = enabled ? 1 : 0;
        drawTexturedModalRect(left, top, 10 * i, 16, width, height);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return left <= mouseX && mouseX < left + width &&
               top <= mouseY && mouseY < top + height;
    }

    public void drawButtonForegroundLayer(Minecraft mc, int mouseX, int mouseY)
    {
        if (mousePressed(mc, mouseX, mouseY))
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            GuiUtils.drawHoveringText(Arrays.asList(toolTipText), mouseX, mouseY, 256, 256, -1, fontrenderer);
        }
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
