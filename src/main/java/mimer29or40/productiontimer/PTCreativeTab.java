package mimer29or40.productiontimer;

import mimer29or40.productiontimer.common.item.PTItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class PTCreativeTab
{
    public static final CreativeTabs tabGeneral = new CreativeTabs(PTInfo.MOD_ID)
    {
        @Override
        public Item getTabIconItem()
        {
            return PTItems.itemWrench;
        }

        @Override
        public String getTabLabel()
        {
            return PTInfo.MOD_ID + ".general";
        }
    };

    private PTCreativeTab() {}
}
