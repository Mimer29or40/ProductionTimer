package mimer29or40.productiontimer.client.gui.components;

import mimer29or40.productiontimer.PTInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public abstract class GuiComponentBase extends Gui
{
    protected static final ResourceLocation ELEMENTS = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/elements.png");
    public boolean enabled;
    public int id;
    public int left;
    public int top;
    public int width;
    public int height;

    public GuiComponentBase(int id, int left, int top, int width, int height)
    {
        this.id = id;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public boolean mouseOver(int mouseX, int mouseY)
    {
        return left <= mouseX && mouseX < left + width &&
               top <= mouseY && mouseY < top + height;
    }

    public abstract void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY);

    public abstract void drawForegroundLayer(Minecraft mc, int mouseX, int mouseY);

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    protected static void drawStichedTexture(int left, int top, int width, int height, int u, int v)
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

    protected static void drawTexture(int x, int y, int width, int height, float u, float v, float tWidth, float tHeight)
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

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x1, y2, 0.0D).tex(u1, v2).endVertex();
        vertexbuffer.pos(x2, y2, 0.0D).tex(u2, v2).endVertex();
        vertexbuffer.pos(x2, y1, 0.0D).tex(u2, v1).endVertex();
        vertexbuffer.pos(x1, y1, 0.0D).tex(u1, v1).endVertex();
        tessellator.draw();
    }
}
