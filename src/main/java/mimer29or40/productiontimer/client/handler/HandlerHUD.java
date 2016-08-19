package mimer29or40.productiontimer.client.handler;

import mimer29or40.productiontimer.common.item.PTItems;
import mimer29or40.productiontimer.common.tile.TileBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class HandlerHUD
{
    @SubscribeEvent
    public void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            Minecraft mc = Minecraft.getMinecraft();
            RayTraceResult result = mc.objectMouseOver;
            if (result != null)
            {
                ItemStack stack = mc.thePlayer.getHeldItemMainhand();
                if (stack != null && stack.getItem() == PTItems.itemWrench)
                {
                    if (result.getBlockPos() != null)
                    {
                        TileEntity tile = mc.theWorld.getTileEntity(result.getBlockPos());
                        if (tile instanceof TileBase)
                        {
                            NBTTagCompound tagCompound = new NBTTagCompound();
                            ((TileBase) tile).writeCustomNBT(tagCompound);
                            drawTextAtLocation(tagCompound.toString(), ScreenLocation.TOP_LEFT, event.getResolution());
                        }
                    }
                }
            }
        }
    }

    private static void drawTextAtCoord(Object text, int x, int y)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getMinecraft().fontRendererObj.drawString(String.valueOf(text), x, y, 0x00FFFFFF);

        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void drawTextAtLocation(Object obj, ScreenLocation location, ScaledResolution res)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        String text = String.valueOf(obj);

        int x, y;
        int textLength = fontRenderer.getStringWidth(text);
        int textHeight = 10;
        int centerOffset = 7;
        int inventoryOffset = 12;

        switch (location)
        {
            case TOP:
                x = (res.getScaledWidth() - textLength) / 2;
                y = 10;
                break;
            case BOTTOM:
                x = (res.getScaledWidth() - textLength) / 2;
                y = res.getScaledHeight() - 10 - textHeight - inventoryOffset;
                break;
            case LEFT:
                x = 10;
                y = res.getScaledHeight() / 2;
                break;
            case RIGHT:
                x = res.getScaledWidth() - 10 - textLength;
                y = res.getScaledHeight() / 2;
                break;
            case TOP_LEFT:
                x = 10;
                y = 10;
                break;
            case TOP_RIGHT:
                x = res.getScaledWidth() - 10 - textLength;
                y = 10;
                break;
            case BOTTOM_LEFT:
                x = 10;
                y = res.getScaledHeight() - 10 - textHeight;
                break;
            case BOTTOM_RIGHT:
                x = res.getScaledWidth() - 10 - textLength;
                y = res.getScaledHeight() - 10 - textHeight;
                break;
            case CENTER:
                x = (res.getScaledWidth() - textLength) / 2;
                y = res.getScaledHeight() / 2 + centerOffset;
                break;
            default:
                x = (res.getScaledWidth() - textLength) / 2;
                y = res.getScaledHeight() / 2 + centerOffset;
        }
        fontRenderer.drawString(text, x, y, 0x00FFFFFF);
    }

    private enum ScreenLocation
    {
        TOP, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER;
    }
}
