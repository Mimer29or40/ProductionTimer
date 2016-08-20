package mimer29or40.productiontimer.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class GuiComponentGraphTab extends GuiComponentButton
{
    public GuiComponentGraphTab(int id, int left, int top, String text)
    {
        super(id, left, top, 48, 16, text);
    }

    public void drawBackgroundLayer(Minecraft mc, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(ELEMENTS);

        FontRenderer fontrenderer = mc.fontRendererObj;

        int i = selected ? 0 : 1;
        drawTexturedModalRect(left, top, 48 * i, 0, width, height);

        int j = 14737632;
        if (!selected) j = 10526880;

        drawCenteredString(fontrenderer, buttonText, left + width / 2, top + (height - 7) / 2, j);
    }

    public void drawButtonForegroundLayer(Minecraft mc, int mouseX, int mouseY)
    {

    }
}
