package mimer29or40.productiontimer.common.block;

import mimer29or40.productiontimer.ModInfo;
import mimer29or40.productiontimer.common.tile.TileBase;
import mimer29or40.productiontimer.common.util.Log;
import mimer29or40.productiontimer.common.util.Platform;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PTBlocks
{
    private static List<BlockBase> blocks = new ArrayList<>();

    public static void registerBlocks()
    {

    }

    private static BlockBase registerBlock(BlockBase block)
    {
        return registerBlock(new ItemBlock(block));
    }

    private static BlockBase registerBlock(ItemBlock itemBlock)
    {
        BlockBase block = (BlockBase) itemBlock.getBlock();
        try
        {
            String internalName = block.getInternalName();

            block.setRegistryName(ModInfo.MOD_ID, internalName);
            block.setUnlocalizedName(internalName);

            itemBlock.setRegistryName(block.getRegistryName());

            GameRegistry.register(block);
            GameRegistry.register(itemBlock);

            if (Platform.isClient()) block.registerBlockModel();

            blocks.add(block);
            Log.info("Registered block (%s)", block.getUnlocalizedName());
        }
        catch (Exception e)
        {
            Log.fatal("Fatal Error while registering block (%s)", block.getUnlocalizedName());
            e.printStackTrace();
        }

        return block;
    }

    private static BlockBase registerBlockWithTileEntity(BlockBase block, Class<? extends TileBase> tileEntityClass)
    {
        return registerBlockWithTileEntity(new ItemBlock(block), tileEntityClass);
    }

    private static BlockBase registerBlockWithTileEntity(ItemBlock itemBlock, Class<? extends TileBase> tileEntityClass)
    {
        BlockBase block = (BlockBase) itemBlock.getBlock();
        try
        {
            String internalName = block.getInternalName();

            if (!internalName.equals(internalName.toLowerCase(Locale.US)))
            { throw new IllegalArgumentException("Unlocalized names need to be all lowercase! Block: " + internalName); }

            if (internalName.isEmpty())
            { throw new IllegalArgumentException("Unlocalized name cannot be blank! Block: " + block.getUnlocalizedName()); }

            block.setRegistryName(ModInfo.MOD_ID, internalName);
            block.setUnlocalizedName(internalName);

            itemBlock.setRegistryName(block.getRegistryName());

            GameRegistry.register(block);
            GameRegistry.register(itemBlock);

            GameRegistry.registerTileEntity(tileEntityClass, block.getRegistryName().toString());

            if (Platform.isClient()) block.registerBlockModel();

            blocks.add(block);
            Log.info("Registered block with tile (%s)", block.getUnlocalizedName());
        }
        catch (Exception e)
        {
            Log.fatal("Fatal Error while registering block with tile (%s)", block.getUnlocalizedName());
            e.printStackTrace();
        }
        return block;
    }

    public static List<BlockBase> getBlocks()
    {
        return Collections.unmodifiableList(blocks);
    }
}
