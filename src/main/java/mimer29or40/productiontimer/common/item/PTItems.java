package mimer29or40.productiontimer.common.item;

import mimer29or40.productiontimer.ModInfo;
import mimer29or40.productiontimer.common.util.Log;
import mimer29or40.productiontimer.common.util.Platform;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PTItems
{
    private static List<ItemBase> items = new ArrayList<>();

    public static void registerItems()
    {

    }

    private static <I extends ItemBase> I registerItem(I item)
    {
        try
        {
            String internalName = item.getInternalName();

            item.setRegistryName(ModInfo.MOD_ID, internalName);
            item.setUnlocalizedName(internalName);

            GameRegistry.register(item);

            if (Platform.isClient()) item.registerItemModel();

            items.add(item);
            Log.info("Registered item (%s)", item.getUnlocalizedName());
        }
        catch (Exception e)
        {
            Log.fatal("Fatal Error while registering item (%s)", item.getUnlocalizedName());
            e.printStackTrace();
        }
        return item;
    }

    public static List<ItemBase> getItems()
    {
        return Collections.unmodifiableList(items);
    }
}
