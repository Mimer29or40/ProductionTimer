package mimer29or40.productiontimer.common.block;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.common.tile.TileBase;
import mimer29or40.productiontimer.common.tile.TileController;
import mimer29or40.productiontimer.common.tile.TileRelay;
import mimer29or40.productiontimer.common.util.Log;
import mimer29or40.productiontimer.common.util.Platform;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PTBlocks
{
    private static List<BlockBase> blocks = new ArrayList<>();

    public static BlockController blockController;
    public static BlockRelay      blockRelay;
    public static BlockTest       blockTest;

    public static void registerBlocks()
    {
        blockController = (BlockController) registerBlockWithTileEntity(new BlockController(), TileController.class);
        blockRelay = (BlockRelay) registerBlockWithTileEntity(new BlockRelay(), TileRelay.class);

//        blockTest = new BlockTest();
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

            block.setRegistryName(PTInfo.MOD_ID, internalName);
            block.setUnlocalizedName(internalName);

            itemBlock.setRegistryName(block.getRegistryName());

            GameRegistry.register(block);
            GameRegistry.register(itemBlock);

            if (Platform.isClient()) block.registerBlockModel();

            blocks.add(block);
            Log.info("Registered block (%s)", block.getClass().getCanonicalName());
        }
        catch (Exception e)
        {
            Log.fatal("Fatal Error while registering block (%s)", block.getClass().getCanonicalName());
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

            block.setRegistryName(PTInfo.MOD_ID, internalName);
            block.setUnlocalizedName(internalName);

            itemBlock.setRegistryName(block.getRegistryName());

            GameRegistry.register(block);
            GameRegistry.register(itemBlock);

            GameRegistry.registerTileEntity(tileEntityClass, block.getRegistryName().toString());

            if (Platform.isClient()) block.registerBlockModel();

            blocks.add(block);
            Log.info("Registered block (%s) with tile (%s)", block.getClass().getCanonicalName(), tileEntityClass.getCanonicalName());
        }
        catch (Exception e)
        {
            Log.fatal("Fatal Error while registering block with tile (%s)", block.getClass().getCanonicalName());
            e.printStackTrace();
        }
        return block;
    }

    public static List<BlockBase> getBlocks()
    {
        return Collections.unmodifiableList(blocks);
    }
}
