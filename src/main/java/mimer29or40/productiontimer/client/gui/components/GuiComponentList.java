package mimer29or40.productiontimer.client.gui.components;

import mimer29or40.productiontimer.PTInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public abstract class GuiComponentList extends GuiComponentBase
{
    protected final int baseLeft;
    protected final int baseTop;
    protected final int baseWidth;
    protected final int baseHeight;

    protected final int entryHeight;

    protected int mouseX;
    protected int mouseY;

    protected float initialMouseClickY = -2.0F;
    protected float scrollFactor;
    protected float scrollDistance;

    protected int selectedEntry = -1;

    protected long lastClickTime = 0L;

    protected boolean highlightSelected = true;

    public GuiComponentList(int id, int left, int top, int width, int height, int entryHeight)
    {
        super(id, left, top, width, height);

        this.baseLeft = left + 1;
        this.baseTop = top + 1;
        this.baseWidth = width - 2;
        this.baseHeight = height - 2;

        this.entryHeight = entryHeight;
    }

    public abstract int getSize();

    public void entryClicked(int entry, boolean doubleClick)
    {
        selectedEntry = entry;
    }

    public boolean isSelected(int entry)
    {
        return selectedEntry == entry;
    }

    protected int getContentHeight()
    {
        return getSize() * entryHeight;
    }

    private void applyScrollLimits()
    {
        int listHeight = getContentHeight() - height;

        if (listHeight < 0) listHeight /= 2;

        if (scrollDistance < 0.0F) scrollDistance = 0.0F;
        if (scrollDistance > (float) listHeight) scrollDistance = (float) listHeight;
    }

    public void handleMouseInput(int mouseX, int mouseY) throws IOException
    {
        if (!mouseOver(mouseX, mouseY))
            return;

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0)
        {
            scrollDistance += (-1 * scroll / 120.0F) * entryHeight / 2;
        }
    }

    protected abstract void drawEntry(int entryId, int entryLeft, int entryTop, int entryBuffer, Tessellator tess);

    @Override
    public void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        ResourceLocation ELEMENTS = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/elements.png");

        int listLength = getSize();

        int scrollBarWidth = 12;
        int scrollBarLeft = baseLeft + baseWidth - scrollBarWidth;

        int entryLeft = baseLeft;
        int entryWidth = scrollBarLeft - 1 - entryLeft;

        int border = 4;

        if (Mouse.isButtonDown(0))
        {
            if (initialMouseClickY == -1.0F)
            {
                if (mouseOver(mouseX, mouseY))
                {
                    int mouseListY = mouseY - baseTop + (int) scrollDistance - border;
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
                        int scrollHeight = getContentHeight() - baseHeight - border;
                        if (scrollHeight < 1) scrollHeight = 1;

                        int scrollBarHeight = (int) ((float) (baseHeight * baseHeight) / (float) getContentHeight());

                        if (scrollBarHeight < 32) scrollBarHeight = 32;
                        if (scrollBarHeight > baseHeight - 2 * border) scrollBarHeight = baseHeight - 2 * border;
                        scrollBarHeight = 15;

                        scrollFactor /= (float) (baseHeight - scrollBarHeight) / (float) scrollHeight;
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

//        drawGradientRect(left, top, left + width, top + height, 0xFF8B8B8B, 0xFF8B8B8B);
        mc.getTextureManager().bindTexture(ELEMENTS);
        drawStichedTexture(left, top, width, height, 0, 44);

        int baseY = baseTop + 2 - (int) scrollDistance;

        for (int entryId = 0; entryId < listLength; ++entryId)
        {
            int entryTop = baseY + entryId * entryHeight;
            int entryBuffer = entryHeight - border;

            if (entryTop <= baseTop + baseHeight && entryTop + entryBuffer >= baseTop)
            {
                if (highlightSelected && isSelected(entryId))
                {
                    int min = baseLeft;
                    int max = entryLeft + entryWidth;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableTexture2D();
                    worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(min, entryTop + entryBuffer + 2, 0).tex(0, 1).color(0x60, 0x60, 0x60, 0xFF).endVertex();
                    worldr.pos(max, entryTop + entryBuffer + 2, 0).tex(1, 1).color(0x60, 0x60, 0x60, 0xFF).endVertex();
                    worldr.pos(max, entryTop - 2, 0).tex(1, 0).color(0x60, 0x60, 0x60, 0xFF).endVertex();
                    worldr.pos(min, entryTop - 2, 0).tex(0, 0).color(0x60, 0x60, 0x60, 0xFF).endVertex();
                    worldr.pos(min + 1, entryTop + entryBuffer + 1, 0).tex(0, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, entryTop + entryBuffer + 1, 0).tex(1, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, entryTop - 1, 0).tex(1, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(min + 1, entryTop - 1, 0).tex(0, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    tess.draw();
                    GlStateManager.enableTexture2D();
                }

                drawEntry(entryId, entryLeft, entryTop, entryBuffer, tess);
            }
        }

        int extraHeight = getContentHeight() - baseHeight;

        if (extraHeight > 0)
        {
            int scrollBarHeight = (baseHeight * baseHeight) / getContentHeight();

            if (scrollBarHeight < 32) scrollBarHeight = 32;

            if (scrollBarHeight > baseHeight - border * 2)
                scrollBarHeight = baseHeight - border * 2;
            scrollBarHeight = 15;

            int barTop = (int) scrollDistance * (baseHeight - scrollBarHeight) / extraHeight + baseTop;
            if (barTop < baseTop)
            {
                barTop = baseTop;
            }
            GlStateManager.disableTexture2D();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft, baseTop + baseHeight, 0.0D).tex(0.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth, baseTop + baseHeight, 0.0D).tex(1.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft + scrollBarWidth, baseTop, 0.0D).tex(1.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft, baseTop, 0.0D).tex(0.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            GlStateManager.enableTexture2D();

            mc.getTextureManager().bindTexture(ELEMENTS);
            drawTexture(scrollBarLeft, barTop, scrollBarWidth, scrollBarHeight, 0, 26, 12, 15);
//            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//            worldr.pos(scrollBarLeft, barTop + scrollBarHeight, 0.0D).tex(0.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
//            worldr.pos(scrollBarLeft + scrollBarWidth, barTop + scrollBarHeight, 0.0D).tex(1.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
//            worldr.pos(scrollBarLeft + scrollBarWidth, barTop, 0.0D).tex(1.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
//            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
//            tess.draw();
//            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//            worldr.pos(scrollBarLeft, barTop + scrollBarHeight - 1, 0.0D).tex(0.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
//            worldr.pos(scrollBarLeft + scrollBarWidth - 1, barTop + scrollBarHeight - 1, 0.0D).tex(1.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
//            worldr.pos(scrollBarLeft + scrollBarWidth - 1, barTop, 0.0D).tex(1.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
//            worldr.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
//            tess.draw();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void drawForegroundLayer(Minecraft mc, int mouseX, int mouseY)
    {

    }
}
