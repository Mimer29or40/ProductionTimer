package mimer29or40.productiontimer.common.item;

import mimer29or40.productiontimer.PTInfo;
import mimer29or40.productiontimer.common.registry.IRegisterItemModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public abstract class ItemBase extends Item implements IRegisterItemModel
{
    protected String resourcePath = "";
    protected String internalName = "";

    public ItemBase(String resourcePath)
    {
        this.resourcePath = resourcePath;
    }

    @Override
    public String getUnlocalizedName()
    {
        String itemName = getUnwrappedUnlocalizedName(super.getUnlocalizedName());

        String test = String.format("item.%s.%s", PTInfo.MOD_ID, itemName);
        return test;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        String itemName = getUnwrappedUnlocalizedName(super.getUnlocalizedName(stack));

        String test = String.format("item.%s.%s", PTInfo.MOD_ID, itemName);
        return test;
    }

    public String getInternalName()
    {
        return internalName;
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
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

    public ItemStack getStack(int size, int damage)
    {
        return new ItemStack(this, size, damage);
    }

    @Override
    public void registerItemModel()
    {
        final String resourcePath = String.format("%s:%s", PTInfo.MOD_ID, this.resourcePath);

        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(resourcePath, "inventory"));
    }
}
