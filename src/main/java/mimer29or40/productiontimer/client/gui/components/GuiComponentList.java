package mimer29or40.productiontimer.client.gui.components;

import mimer29or40.productiontimer.PTInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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

    protected final int border;

    protected final int entryHeight;
    protected final int entryBorder;
    protected final int entrySpacing;

    protected final int scrollBarWidth = 6;

    protected int mouseX;
    protected int mouseY;

    protected float initialMouseClickY = -2.0F;
    protected int   firstEntry;
    protected float scrollFactor;
    protected float scrollDistance;

    protected long lastClickTime = 0L;

    public GuiComponentList(int id, int left, int top, int width, int height, int entryHeight)
    {
        super(id, left, top, width, height);

        this.baseLeft = left + 1;
        this.baseTop = top + 1;
        this.baseWidth = width - 2;
        this.baseHeight = height - 2;

        this.border = 2;

        this.entryHeight = entryHeight;
        this.entryBorder = 2;
        this.entrySpacing = 2;

        this.firstEntry = 0;
    }

    public abstract int getSize();

    public abstract int getSelectedEntry();

    public abstract void setSelectedEntry(int entry);

    public boolean mouseOverEntry(int entryId, int mouseX, int mouseY)
    {
        if (mouseOver(mouseX, mouseY))
        {
            if (0 <= entryId && entryId < getSize())
            {
                int mouseListY = mouseY - baseTop + firstEntry * (entryHeight + entrySpacing) - border;

                int entryTop = entryId * (entryHeight + entrySpacing);

                if (entryTop < mouseListY && mouseListY <= entryTop + entryHeight)
                {
                    int entryLeft = baseLeft + border;
                    int scrollBarLeft = baseLeft + baseWidth - scrollBarWidth;
                    int entryWidth = getContentHeight() - baseHeight > 0 ? scrollBarLeft - entryLeft - border : baseWidth - 2 * border;

                    return entryLeft < mouseX && mouseX <= entryLeft + entryWidth;
                }
            }
        }
        return false;
    }

    public void entryClicked(int entry, boolean doubleClick)
    {
        setSelectedEntry(entry);
    }

    public boolean isSelected(int entry)
    {
        return getSelectedEntry() == entry;
    }

    protected int getContentHeight()
    {
        return getSize() * entryHeight + 2 * border + (getSize() - 1) * entrySpacing;
    }

    private void applyScrollLimits()
    {
        int listHeight = getContentHeight() - baseHeight;

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

    protected abstract void drawEntry(int entryId, int entryLeft, int entryTop, int entryHeight, int entryWidth);

    @Override
    public void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        ResourceLocation ELEMENTS = new ResourceLocation(PTInfo.MOD_ID + ":textures/gui/elements.png");

        int scrollBarLeft = baseLeft + baseWidth - scrollBarWidth;

        int extraHeight = getContentHeight() - baseHeight;

        int entryLeft = baseLeft + border;
        int entryWidth = extraHeight > 0 ? scrollBarLeft - entryLeft - border : baseWidth - 2 * border;

        if (Mouse.isButtonDown(0))
        {
            if (initialMouseClickY == -1.0F)
            {
                if (mouseOver(mouseX, mouseY))
                {
                    for (int entryId = 0; entryId < getSize(); entryId++)
                    {
                        if (mouseOverEntry(entryId, mouseX, mouseY))
                        {
                            entryClicked(entryId, entryId == getSelectedEntry() && System.currentTimeMillis() - lastClickTime < 250L);
                            lastClickTime = System.currentTimeMillis();
                            break;
                        }
                    }

                    if (scrollBarLeft <= mouseX && mouseX <= scrollBarLeft + scrollBarWidth)
                    {
                        scrollFactor = -1.0F;
                        int scrollHeight = getContentHeight() - baseHeight;
                        if (scrollHeight < 1) scrollHeight = 1;

                        int scrollBarHeight = (int) ((float) (baseHeight * baseHeight) / (float) getContentHeight());
                        if (scrollBarHeight < 15) scrollBarHeight = 15;
                        if (scrollBarHeight > baseHeight - 2 * entryBorder) scrollBarHeight = baseHeight - 2 * border;

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

        firstEntry = (int) scrollDistance / (entryHeight + entrySpacing);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(ELEMENTS);
        drawStichedTexture(left, top, width, height, 0, 44);

        int baseY = baseTop - firstEntry * (entryHeight + entrySpacing) + border;

        for (int entryId = 0; entryId < getSize(); ++entryId)
        {
            int entryTop = baseY + entryId * (entryHeight + entrySpacing);
            int entryBuffer = entryHeight - 2 * entryBorder;

            if (entryTop <= baseTop + baseHeight && entryTop + entryHeight > baseTop)
            {
                if (mouseOverEntry(entryId, mouseX, mouseY))
                {
                    drawRect(entryLeft, entryTop, entryLeft + entryWidth, entryTop + entryHeight, 0x66FFFFFF);
                }

                if (isSelected(entryId))
                {
                    mc.getTextureManager().bindTexture(ELEMENTS);

                    drawStichedTexture(entryLeft, entryTop, entryWidth, entryHeight, 3, 44);
                }

                drawEntry(entryId, entryLeft + entryBorder, entryTop + entryBorder, entryBuffer, entryWidth - 2 * entryBorder);
            }
        }

        if (extraHeight > 0)
        {
            int scrollBarHeight = (baseHeight * baseHeight) / getContentHeight();

            if (scrollBarHeight < 15) scrollBarHeight = 15;
            if (scrollBarHeight > baseHeight - entryBorder * 2) scrollBarHeight = baseHeight - entryBorder * 2;

            int scrollBarTop = (int) scrollDistance * (baseHeight - scrollBarHeight) / extraHeight + baseTop;
            if (scrollBarTop < baseTop) scrollBarTop = baseTop;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            mc.getTextureManager().bindTexture(ELEMENTS);
            drawStichedTexture(scrollBarLeft, scrollBarTop, scrollBarWidth, scrollBarHeight, 0, 41);
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
