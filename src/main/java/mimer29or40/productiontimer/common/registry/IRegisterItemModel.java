package mimer29or40.productiontimer.common.registry;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRegisterItemModel
{
    @SideOnly(Side.CLIENT)
    void registerItemModel();
}
