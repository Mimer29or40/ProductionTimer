package mimer29or40.productiontimer.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public abstract class GuiComponentList
{
    protected final Minecraft client;

    protected final int screenWidth;
    protected final int screenHeight;

    protected final int left;
    protected final int top;
    protected final int width;
    protected final int height;

    protected final int entryHeight;

    protected int mouseX;
    protected int mouseY;

    protected float initialMouseClickY = -2.0F;
    protected float scrollFactor;
    protected float scrollDistance;

    protected int selectedEntry = -1;

    protected long lastClickTime = 0L;

    protected boolean highlightSelected = true;

    public GuiComponentList(Minecraft client, int screenWidth, int screenHeight, int left, int top, int width, int height, int entryHeight)
    {
        this.client = client;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;

        this.entryHeight = entryHeight;
    }

    public abstract int getSize();

    public abstract void entryClicked(int index, boolean doubleClick);

    public abstract boolean isSelected(int index);

    protected int getContentHeight()
    {
        return this.getSize() * entryHeight;
    }

    private void applyScrollLimits()
    {
        int listHeight = this.getContentHeight() - height;

        if (listHeight < 0) listHeight /= 2;

        if (scrollDistance < 0.0F) scrollDistance = 0.0F;
        if (scrollDistance > (float) listHeight) scrollDistance = (float) listHeight;
    }

    public void handleMouseInput(int mouseX, int mouseY) throws IOException
    {
        boolean isHovering = left <= mouseX && mouseX <= left + width &&
                             top <= mouseY && mouseY <= top + height;
        if (!isHovering)
            return;

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0)
        {
            scrollDistance += (-1 * scroll / 120.0F) * entryHeight / 2;
        }
    }

    protected abstract void drawEntry(int entryId, int entryLeft, int entryTop, int entryBuffer, Tessellator tess);

    public void drawEntryList(int mouseX, int mouseY)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        boolean isHovering = left <= mouseX && mouseX <= left + width &&
                             top <= mouseY && mouseY <= top + height;

        int listLength = getSize();

        int scrollBarWidth = 6;
        int scrollBarLeft = left + width - scrollBarWidth;

        int entryLeft = left;
        int entryWidth = scrollBarLeft - 1 - entryLeft;

        int border = 4;

        if (Mouse.isButtonDown(0))
        {
            if (initialMouseClickY == -1.0F)
            {
                if (isHovering)
                {
                    int mouseListY = mouseY - top + (int) scrollDistance - border;
                    int entryIndex = mouseListY / entryHeight;

                    if (entryLeft <= mouseX && mouseX <= entryLeft + entryWidth && listLength > entryIndex && entryIndex >= 0 && mouseListY >= 0)
                    {
                        entryClicked(entryIndex, entryIndex == selectedEntry && System.currentTimeMillis() - lastClickTime < 250L);
                        selectedEntry = entryIndex;
                        lastClickTime = System.currentTimeMillis();
                    }

                    if (scrollBarLeft <= mouseX && mouseX <= scrollBarLeft + scrollBarWidth)
                    {
                        scrollFactor = -1.0F;
                        int scrollHeight = getContentHeight() - height - border;
                        if (scrollHeight < 1) scrollHeight = 1;

                        int var = (int) ((float) (height * height) / (float) getContentHeight());

                        if (var < 32) var = 32;
                        if (var > height - 2 * border) var = height - 2 * border;

                        scrollFactor /= (float) (height - var) / (float) scrollHeight;
                    }
                    else
                    {
                        scrollFactor = 1.0F;
                    }

                    initialMouseClickY = mouseY;
                }
                else
                {
                    initialMouseClickY = -2.0F;
                }
            }
            else if (initialMouseClickY >= 0.0F)
            {
                scrollDistance -= ((float) mouseY - initialMouseClickY) * scrollFactor;
                initialMouseClickY = (float) mouseY;
            }
        }
        else
        {
            initialMouseClickY = -1.0F;
        }

        applyScrollLimits();

        Tessellator tess = Tessellator.getInstance();
        VertexBuffer worldr = tess.getBuffer();

        drawGradientRect(left, top, left + width, top + height, 0xFF8B8B8B, 0xFF8B8B8B);

        int baseY = top + 2 - (int) scrollDistance;

        for (int entryId = 0; entryId < listLength; ++entryId)
        {
            int entryTop = baseY + entryId * entryHeight;
            int entryBuffer = entryHeight - border;

            if (entryTop <= top + height && entryTop + entryBuffer >= top)
            {
                if (highlightSelected && isSelected(entryId))
                {
                    int min = left;
                    int max = entryLeft + entryWidth;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableTexture2D();
                    worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(min, entryTop + entryBuffer + 2, 0).tex(0, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(max, entryTop + entryBuffer + 2, 0).tex(1, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(max, entryTop - 2, 0).tex(1, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min, entryTop - 2, 0).tex(0, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min + 1, entryTop + entryBuffer + 1, 0).tex(0, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, entryTop + entryBuffer + 1, 0).tex(1, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, entryTop - 1, 0).tex(1, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(min + 1, entryTop - 1, 0).tex(0, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    tess.draw();
                    GlStateManager.enableTexture2D();
                }

                drawEntry(entryId, entryLeft, entryTop, entryBuffer, null);
            }
        }

        int extraHeight = getContentHeight() - height;

        if (extraHeight > 0)
        {
            int height = (this.height * this.height) / this.getContentHeight();

            if (height < 32) height = 32;

            if (height > this.height - border * 2)
                height = this.height - border * 2;

            int barTop = (int) this.scrollDistance * (this.height - height) / extraHeight + this.top;
            if (barTop < this.top)
            {
                barTop = this.top;
            }

            GlStateManager.disableTexture2D();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, top + this.height, 0.0D).tex(0.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth, top + this.height, 0.0D).tex(1.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth, top, 0.0D).tex(1.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, top, 0.0D).tex(0.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, barTop + height, 0.0D).tex(0.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth, barTop + height, 0.0D).tex(1.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth, barTop, 0.0D).tex(1.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, barTop + height - 1, 0.0D).tex(0.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth - 1, barTop + height - 1, 0.0D).tex(1.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth - 1, barTop, 0.0D).tex(1.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            tess.draw();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2)
    {
        float a1 = (float) (color1 >> 24 & 255) / 255.0F;
        float r1 = (float) (color1 >> 16 & 255) / 255.0F;
        float g1 = (float) (color1 >> 8 & 255) / 255.0F;
        float b1 = (float) (color1 & 255) / 255.0F;
        float a2 = (float) (color2 >> 24 & 255) / 255.0F;
        float r2 = (float) (color2 >> 16 & 255) / 255.0F;
        float g2 = (float) (color2 >> 8 & 255) / 255.0F;
        float b2 = (float) (color2 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer VertexBuffer = tessellator.getBuffer();
        VertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        VertexBuffer.pos(right, top, 0.0D).color(r1, g1, b1, a1).endVertex();
        VertexBuffer.pos(left, top, 0.0D).color(r1, g1, b1, a1).endVertex();
        VertexBuffer.pos(left, bottom, 0.0D).color(r2, g2, b2, a2).endVertex();
        VertexBuffer.pos(right, bottom, 0.0D).color(r2, g2, b2, a2).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
