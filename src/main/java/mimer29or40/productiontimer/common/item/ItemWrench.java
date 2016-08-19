package mimer29or40.productiontimer.common.item;

import mimer29or40.productiontimer.PTCreativeTab;
import mimer29or40.productiontimer.common.model.ConnectionType;
import mimer29or40.productiontimer.common.registry.IRegisterRecipe;
import mimer29or40.productiontimer.common.tile.TileController;
import mimer29or40.productiontimer.common.tile.TileRelay;
import mimer29or40.productiontimer.common.util.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemWrench extends ItemBase implements IRegisterRecipe
{
    private static final String TAG_POS_X = "posx";
    private static final String TAG_POS_Y = "posy";
    private static final String TAG_POS_Z = "posz";

    public ItemWrench()
    {
        super("wrench");

        internalName = "wrench";

        setCreativeTab(PTCreativeTab.tabGeneral);
        setMaxStackSize(1);
    }

    public void setPosition(ItemStack stack, @Nullable BlockPos pos)
    {
        if (pos != null)
        {
            ItemNBTHelper.setInt(stack, TAG_POS_X, pos.getX());
            ItemNBTHelper.setInt(stack, TAG_POS_Y, pos.getY());
            ItemNBTHelper.setInt(stack, TAG_POS_Z, pos.getZ());
        }
        else
        {
            ItemNBTHelper.setInt(stack, TAG_POS_X, 0);
            ItemNBTHelper.setInt(stack, TAG_POS_Y, -1);
            ItemNBTHelper.setInt(stack, TAG_POS_Z, 0);
        }
    }

    @Nullable
    public BlockPos getPosition(ItemStack stack)
    {
        int x = ItemNBTHelper.getInt(stack, TAG_POS_X, 0);
        int y = ItemNBTHelper.getInt(stack, TAG_POS_Y, -1);
        int z = ItemNBTHelper.getInt(stack, TAG_POS_Z, 0);

        if (y == -1) return null;

        return BlockPos.ORIGIN.add(x, y, z);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (worldIn.isRemote)
            return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);

        // clicked on a block?
        RayTraceResult mop = this.rayTrace(worldIn, playerIn, false);

        if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            if (playerIn.isSneaking())
            {
                setPosition(itemStackIn, null);
//            playSound(playerIn, worldIn, playerIn.playerLocation);
                playerIn.addChatMessage(new TextComponentTranslation("productiontimer.connector.clear"));
                return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return EnumActionResult.PASS;

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileController)
        {
            if (((TileController) tile).getName() == null)
            {
                playerIn.addChatComponentMessage(new TextComponentTranslation(ConnectionType.NEEDSID.getMessage()));
                return EnumActionResult.FAIL;
            }
            setPosition(stack, pos);
//                playSound(playerIn, worldIn, controllerPos);
            playerIn.addChatMessage(new TextComponentTranslation(ConnectionType.SET.getMessage()));
            return EnumActionResult.SUCCESS;
        }

        if (tile instanceof TileRelay)
        {
            TileRelay tileRelay = (TileRelay) tile;

            if (getPosition(stack) == null)
            {
                playerIn.addChatMessage(new TextComponentTranslation(ConnectionType.NOTCONTROLLER.getMessage()));
                return EnumActionResult.FAIL;
            }

            BlockPos storedPos = getPosition(stack);

            if (storedPos.equals(pos))
            {
                playerIn.addChatMessage(new TextComponentTranslation(ConnectionType.SAMEBLOCK.getMessage()));
                return EnumActionResult.FAIL;
            }

            // TODO add logic to make range limited
//                if (Math.abs(storedPos.getX() - controllerPos.getX()) > controller.getMaxDistance() ||
//                    Math.abs(storedPos.getY() - controllerPos.getY()) > controller.getMaxDistance() ||
//                    Math.abs(storedPos.getZ() - controllerPos.getZ()) > controller.getMaxDistance())
//                {
//                    playerIn.addChatMessage(new TextComponentTranslation("productiontimer.connector.toofar"));
//                    return EnumActionResult.FAIL;
//                }


            if (((TileRelay) tile).getName() == null)
            {
                playerIn.addChatComponentMessage(new TextComponentTranslation(ConnectionType.NEEDSID.getMessage()));
                return EnumActionResult.FAIL;
            }

            ConnectionType connectionToController = tileRelay.linkController(storedPos);

            playerIn.addChatMessage(new TextComponentTranslation(connectionToController.getMessage()));

            if (connectionToController.isSuccess())
                return EnumActionResult.SUCCESS;

            return EnumActionResult.FAIL;
        }

        return EnumActionResult.PASS;
    }

    // TODO Play sound
//    private void playSound(EntityPlayer playerIn, World world, BlockPos controllerPos)
//    {
//        if (!world.isRemote)
//            world.playSound(playerIn, controllerPos, SoundEvents.ENTITY_EXPERIENCE_ORB_TOUCH, SoundCategory.PLAYERS, 0.8F, 1F);
//    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced)
    {
        list.add("Linked to:");

        BlockPos pos = getPosition(stack);

        if (pos != null)
            list.add(String.format(" (%s,%s,%s)", pos.getX(), pos.getY(), pos.getZ()));
        else
            list.add(" Nothing");
    }

    @Override
    public void registerRecipes()
    {

    }
}
