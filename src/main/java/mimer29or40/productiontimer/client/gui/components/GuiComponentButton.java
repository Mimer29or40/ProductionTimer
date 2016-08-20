package mimer29or40.productiontimer.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiComponentButton extends GuiComponentBase
{
    public boolean selected;
    public String  buttonText;
    public String  toolTipText;

    public GuiComponentButton(int id, int left, int top, int width, int height, String buttonText, String toolTipText)
    {
        super(id, left, top, width, height);

        this.selected = false;
        this.buttonText = buttonText;
        this.toolTipText = toolTipText;
    }

    public GuiComponentButton(int id, int left, int top, int width, int height, String buttonText)
    {
        this(id, left, top, width, height, buttonText, null);
    }

    @Override
    public void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(ELEMENTS);

        int i = selected ? 1 : mouseOver(mouseX, mouseY) ? 2 : 0;

        drawStichedTexture(left, top, width, height, 3 * i, 41);
//        // Top Left
//        drawTexture(left, top, 2, 2, 3 * i, 41, 2, 2);
//        // Top Center
//        drawTexture(left + 2, top, width - 4, 2, 1 + 3 * i, 41, 1, 2);
//        // Top Right
//        drawTexture(left + width - 2, top, 2, 2, 1 + 3 * i, 41, 2, 2);
//        // Center Left
//        drawTexture(left, top + 2, 2, height - 4, 3 * i, 42, 2, 1);
//        // Center
//        drawTexture(left + 2, top + 2, width - 4, height - 4, 1 + 3 * i, 42, 1, 1);
//        // Center Right
//        drawTexture(left + width - 2, top + 2, 2, height - 4, 1 + 3 * i, 42, 2, 1);
//        // Bottom Left
//        drawTexture(left, top + height - 2, 2, 2, 3 * i, 42, 2, 2);
//        // Bottom Center
//        drawTexture(left + 2, top + height - 2, width - 4, 2, 1 + 3 * i, 42, 1, 2);
//        // Bottom Right
//        drawTexture(left + width - 2, top + height - 2, 2, 2, 1 + 3 * i, 42, 2, 2);

        if (buttonText != null)
        {
            int textColor = mouseOver(mouseX, mouseY) ? 0xFFFFA0 : 14737632;
            drawCenteredString(mc.fontRendererObj, buttonText, left + width / 2, top + (height - 8) / 2, textColor);
        }
    }

    @Override
    public void drawForegroundLayer(Minecraft mc, int mouseX, int mouseY)
    {
//        if (mouseOver(mc, mouseX, mouseY))
//        {
//            FontRenderer fontrenderer = mc.fontRendererObj;
//            GuiUtils.drawHoveringText(Arrays.asList(toolTipText), mouseX, mouseY, 256, 256, -1, fontrenderer);
//        }
    }
}
