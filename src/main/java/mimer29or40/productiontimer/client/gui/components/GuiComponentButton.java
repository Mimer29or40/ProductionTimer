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
