package mimer29or40.productiontimer.common.block;

import mimer29or40.productiontimer.PTCreativeTab;
import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.common.registry.IRegisterBlockModel;
import mimer29or40.productiontimer.common.tile.TileBase;
import mimer29or40.productiontimer.common.util.Platform;
import mimer29or40.productiontimer.common.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockBase extends Block implements IRegisterBlockModel
{
    protected final String resourcePath;
    protected final String internalName;

    public BlockBase(Material material, String resourcePath, String internalName)
    {
        super(material);

        this.resourcePath = resourcePath;
        this.internalName = internalName;

        setSoundType(SoundType.STONE);
        setHardness(2.2F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

        setCreativeTab(PTCreativeTab.tabGeneral);
    }

    public BlockBase(Material material, String internalName)
    {
        this(material, internalName, internalName);
    }

    public String getInternalName()
    {
        return internalName;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName()
    {
        String blockName = getUnwrappedUnlocalizedName(super.getUnlocalizedName());

        return String.format("tile.%s.%s", PTInfo.MOD_ID, blockName);
    }

    private String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    public ItemStack getStack()
    {
        return new ItemStack(this);
    }

    public ItemStack getStack(int size)
    {
        return new ItemStack(this, size);
    }

    public ItemStack getStack(int size, int meta)
    {
        return new ItemStack(this, size, meta);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileBase)
        {
            NBTTagCompound tagCompound = stack.getTagCompound();
            ((TileBase) tile).readCustomNBT(tagCompound);
//            ((TileBase) tile).initTile();
            ((TileBase) tile).markDirtyClient();
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        super.onBlockHarvested(worldIn, pos, state, player);
        if (player.capabilities.isCreativeMode)
        {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileBase)
            {
                ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
                NBTTagCompound tagCompound = new NBTTagCompound();
                ((TileBase) tile).writeCustomNBT(tagCompound);
                stack.setTagCompound(tagCompound);
                WorldHelper.dropStack(worldIn, pos, stack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockModel()
    {
        final String resourcePath = String.format("%s:%s", PTInfo.MOD_ID, this.resourcePath);

        ModelLoader.setCustomStateMapper(this, new DefaultStateMapper()
        {
            @Override
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return new ModelResourceLocation(resourcePath, getPropertyString(state.getProperties()));
            }
        });

        Item item = Item.getItemFromBlock(this);

        if (item != null)
        {
            List<ItemStack> subBlocks = new ArrayList<>();
            getSubBlocks(item, null, subBlocks);

            for (ItemStack itemStack : subBlocks)
            {
                IBlockState blockState = this.getStateFromMeta(itemStack.getItemDamage());
                ModelLoader.setCustomModelResourceLocation(item, itemStack.getItemDamage(),
                                                           new ModelResourceLocation(resourcePath,
                                                                                     Platform.getPropertyString(blockState.getProperties()))
                                                          );
            }
        }
    }
}
