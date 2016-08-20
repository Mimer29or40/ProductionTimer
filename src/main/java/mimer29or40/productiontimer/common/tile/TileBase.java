package mimer29or40.productiontimer.common.tile;

import mimer29or40.productiontimer.common.registry.IRegisterGui;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;

import javax.annotation.Nullable;

public abstract class TileBase extends TileEntity implements ITickable, IWorldNameable, IRegisterGui
{
    private static final String TAG_CUSTOM_NAME = "CustomName";

    protected String customName;

    public void markDirtyClient()
    {
        markDirty();
        if (worldObj != null)
        {
            IBlockState state = worldObj.getBlockState(getPos());
            worldObj.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound updateTag = new NBTTagCompound();
        writeClientNBT(updateTag);
        return new SPacketUpdateTileEntity(getPos(), 1, updateTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readClientNBT(packet.getNbtCompound());
        markDirtyClient();
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound updateTag = super.getUpdateTag();
        writeClientNBT(updateTag);
        return updateTag;
    }

    public void readClientNBT(NBTTagCompound compound)
    {
        readFromNBT(compound);
    }

    public void readCustomNBT(NBTTagCompound compound)
    {
        if (compound != null && compound.hasKey(TAG_CUSTOM_NAME))
            customName = compound.getString(TAG_CUSTOM_NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        readCustomNBT(compound);
    }

    public void writeClientNBT(NBTTagCompound compound)
    {
        writeToNBT(compound);
    }

    public void writeCustomNBT(NBTTagCompound compound)
    {
        if (hasCustomName())
            compound.setString(TAG_CUSTOM_NAME, customName);
    }

    @Override
    @Nullable
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        writeCustomNBT(compound);
        return compound;
    }

    @Override
    public void update()
    {

    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : getDefaultName();
    }

    public String getDefaultName()
    {
        return "productiontimer.noname";
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    public void setCustomName(String name)
    {
        this.customName = name;
        markDirty();
        if (hasWorldObj() && !worldObj.isRemote)
            worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(this.pos), worldObj.getBlockState(this.pos), 0);

    }

    public void initTile() {}
}
