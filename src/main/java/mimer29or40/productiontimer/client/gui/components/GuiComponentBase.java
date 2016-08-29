package mimer29or40.productiontimer.client.gui.components;

import mimer29or40.productiontimer.PTInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public abstract class GuiComponentBase extends Gui
{
    protected static final ResourceLocation ELEMENTS = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/elements.png");

    protected static Tessellator  tessellator  = Tessellator.getInstance();
    protected static VertexBuffer vertexBuffer = tessellator.getBuffer();
    protected static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    public boolean enabled;
    public int id;

    public int left;
    public int top;
    public int width;
    public int height;

    public GuiComponentBase(int id, int left, int top, int width, int height)
    {
        this.enabled = true;
        this.id = id;

        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;

        this.zLevel = 0.0F;
    }

    public boolean mouseOver(int mouseX, int mouseY)
    {
        return left < mouseX && mouseX <= left + width &&
               top < mouseY && mouseY <= top + height;
    }

    public abstract void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY);

    public abstract void drawForegroundLayer(Minecraft mc, int mouseX, int mouseY);

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    protected void drawStichedTexture(int left, int top, int width, int height, int u, int v)
    {
        // Top Left
        drawTexture(left, top, 2, 2, u, v, 2, 2);
        // Top Center
        drawTexture(left + 2, top, width - 4, 2, 1 + u, v, 1, 2);
        // Top Right
        drawTexture(left + width - 2, top, 2, 2, 1 + u, v, 2, 2);
        // Center Left
        drawTexture(left, top + 2, 2, height - 4, u, 1 + v, 2, 1);
        // Center
        drawTexture(left + 2, top + 2, width - 4, height - 4, 1 + u, 1 + v, 1, 1);
        // Center Right
        drawTexture(left + width - 2, top + 2, 2, height - 4, 1 + u, 1 + v, 2, 1);
        // Bottom Left
        drawTexture(left, top + height - 2, 2, 2, u, 1 + v, 2, 2);
        // Bottom Center
        drawTexture(left + 2, top + height - 2, width - 4, 2, 1 + u, 1 + v, 1, 2);
        // Bottom Right
        drawTexture(left + width - 2, top + height - 2, 2, 2, 1 + u, 1 + v, 2, 2);
    }

    protected void drawTexture(int x, int y, int width, int height, float u, float v, float tWidth, float tHeight)
    {
        double x1 = x;
        double y1 = y;
        double x2 = x + width;
        double y2 = y + height;

        double f = 1 / 256F;

        double u1 = u * f;
        double v1 = v * f;
        double u2 = (u + tWidth) * f;
        double v2 = (v + tHeight) * f;

        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(x1, y2, zLevel).tex(u1, v2).endVertex();
        vertexBuffer.pos(x2, y2, zLevel).tex(u2, v2).endVertex();
        vertexBuffer.pos(x2, y1, zLevel).tex(u2, v1).endVertex();
        vertexBuffer.pos(x1, y1, zLevel).tex(u1, v1).endVertex();
        tessellator.draw();
    }

    protected void drawText(String text, float x, float y, int color, boolean shadow)
    {
        if (shadow)
        {
            fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        else
        {
            fontRenderer.drawString(text, (int) x, (int) y, color);
        }
    }

    protected void drawTextLeft(String text, float x, float y, int color, boolean shadow)
    {
        int stringWidth = fontRenderer.getStringWidth(text);
        drawText(text, x - stringWidth, y, color, shadow);
    }

    protected void drawTextCenter(String text, float x, float y, int color, boolean shadow)
    {
        int stringWidth = fontRenderer.getStringWidth(text);
        drawText(text, x - stringWidth / 2, y, color, shadow);
    }
}
