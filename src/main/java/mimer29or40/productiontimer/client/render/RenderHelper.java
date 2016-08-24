package mimer29or40.productiontimer.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderHelper
{
    public int initialHighlightTime = 1000;
    private final HashMap<BlockPos, Integer> blocksToHighlight = new HashMap<>();

    public void addBlockToHighLight(BlockPos pos)
    {
        if (blocksToHighlight.containsKey(pos))
        {
            blocksToHighlight.replace(pos, initialHighlightTime);
        }
        else
        {
            blocksToHighlight.put(pos, initialHighlightTime);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void drawHighlight(final DrawBlockHighlightEvent event)
    {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        // TODO config option
//        GlStateManager.disableDepth();

        final EntityPlayer player = event.getPlayer();
        final World world = player.worldObj;
        final float partialTicks = event.getPartialTicks();

        ArrayList<BlockPos> indexesToModify = new ArrayList<>();

        for (BlockPos pos : blocksToHighlight.keySet())
        {
            int ticksLeft = blocksToHighlight.get(pos);

            if (ticksLeft > 0)
            {
                double alpha = (ticksLeft / (double) initialHighlightTime) * 100;
                highlightBlock(player, pos, partialTicks, 0xFF, 0x00, 0x00, (int) alpha);
                blocksToHighlight.replace(pos, ticksLeft - 1);
            }
            else
            {
                indexesToModify.add(pos);
            }
        }

        for (BlockPos pos : indexesToModify)
        {
            int ticksLeft = blocksToHighlight.get(pos);
            if (ticksLeft <= 0)
            {
                blocksToHighlight.remove(pos);
            }
        }

        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
    }

    public static void highlightBlock(EntityPlayer player, BlockPos blockPos, float partialTicks, int red, int green, int blue, int alpha)
    {
        final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double offset = 0.0001D;

        double x1 = blockPos.getX() - x     - offset;
        double y1 = blockPos.getY() - y     - offset;
        double z1 = blockPos.getZ() - z     - offset;
        double x2 = blockPos.getX() - x + 1 + offset;
        double y2 = blockPos.getY() - y + 1 + offset;
        double z2 = blockPos.getZ() - z + 1 + offset;

        Vec3d corner1 = new Vec3d(x1, y1, z1);
        Vec3d corner2 = new Vec3d(x2, y1, z1);
        Vec3d corner3 = new Vec3d(x2, y1, z2);
        Vec3d corner4 = new Vec3d(x1, y1, z2);
        Vec3d corner5 = new Vec3d(x1, y2, z1);
        Vec3d corner6 = new Vec3d(x2, y2, z1);
        Vec3d corner7 = new Vec3d(x2, y2, z2);
        Vec3d corner8 = new Vec3d(x1, y2, z2);

        renderFace(corner1, corner2, corner3, corner4, red, green, blue, alpha);
        renderFace(corner1, corner2, corner6, corner5, red, green, blue, alpha);
        renderFace(corner2, corner3, corner7, corner6, red, green, blue, alpha);
        renderFace(corner3, corner4, corner8, corner7, red, green, blue, alpha);
        renderFace(corner4, corner1, corner5, corner8, red, green, blue, alpha);
        renderFace(corner5, corner6, corner7, corner8, red, green, blue, alpha);
    }

    public static void renderFace(Vec3d corner1, Vec3d corner2, Vec3d corner3, Vec3d corner4, int red, int green, int blue, int alpha)
    {
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(corner1.xCoord, corner1.yCoord, corner1.zCoord).color(red, green, blue, alpha).endVertex();
        buffer.pos(corner2.xCoord, corner2.yCoord, corner2.zCoord).color(red, green, blue, alpha).endVertex();
        buffer.pos(corner3.xCoord, corner3.yCoord, corner3.zCoord).color(red, green, blue, alpha).endVertex();
        buffer.pos(corner4.xCoord, corner4.yCoord, corner4.zCoord).color(red, green, blue, alpha).endVertex();
        tess.draw();
    }

    public static void renderLine(Vec3d a, Vec3d b, int red, int green, int blue, int alpha)
    {
        final Tessellator tess = Tessellator.getInstance();
        final VertexBuffer buffer = tess.getBuffer();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(a.xCoord, a.yCoord, a.zCoord).color(red, green, blue, alpha).endVertex();
        buffer.pos(b.xCoord, b.yCoord, b.zCoord).color(red, green, blue, alpha).endVertex();
        tess.draw();
    }
}
