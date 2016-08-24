package mimer29or40.productiontimer.common.block;

import mimer29or40.productiontimer.PTNames;
import mimer29or40.productiontimer.ProductionTimer;
import mimer29or40.productiontimer.common.container.ContainerMachine;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.network.PTNetwork;
import mimer29or40.productiontimer.common.network.PacketUnlinkRelay;
import mimer29or40.productiontimer.common.registry.IRegisterRecipe;
import mimer29or40.productiontimer.common.tile.TileController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockController extends BlockMachine implements ITileEntityProvider, IRegisterRecipe
{
    public BlockController()
    {
        super(PTNames.Block.INTERNAL_NAME_CONTROLLER);
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileController();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
                                    @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(ProductionTimer.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            if (playerIn.openContainer instanceof ContainerMachine)
            {
                ((ContainerMachine) playerIn.openContainer).syncOnOpen((EntityPlayerMP) playerIn);
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockstate)
    {
        PTNetwork.sendToServer(new PacketUnlinkRelay(pos));
        TileController tileController = (TileController) world.getTileEntity(pos);
        for (Entry entry : tileController.entries)
        {
            InventoryHelper.dropInventoryItems(world, pos, entry);
        }

        super.breakBlock(world, pos, blockstate);
    }

    @Override
    public void registerRecipes()
    {
        GameRegistry.addShapelessRecipe(PTBlocks.blockController.getStack(1), PTBlocks.blockController.getStack(1));
    }
}
