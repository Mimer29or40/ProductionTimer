package mimer29or40.productiontimer.common.tile;

import mimer29or40.productiontimer.client.gui.GuiController;
import mimer29or40.productiontimer.common.container.ContainerController;
import mimer29or40.productiontimer.common.model.ConnectionType;
import mimer29or40.productiontimer.common.model.Relay;
import mimer29or40.productiontimer.common.util.Log;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

public class TileController extends TileMachine
{
    private static final String TAG_LINKED_RELAYS = "Relays";

    public Map<String, BlockPos> trackedBlocks = new HashMap<>();
    public ArrayList<Relay>      linkedRelays  = new ArrayList<>();
//    private HashMap<String, BlockPos> linkedRelays = new HashMap<>();

    private File saveFolder;
    private File nextIDFile;
    public File controllerFolder;

    public void createFolders()
    {
        try
        {
            saveFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "ProductionTimer");
            if (!saveFolder.exists()) Files.createDirectory(saveFolder.toPath());

            nextIDFile = new File(saveFolder, "nextID");
            if (!nextIDFile.exists())
            {
                Files.createFile(nextIDFile.toPath());
                PrintWriter writer = new PrintWriter(nextIDFile);
                writer.print("0");
                writer.close();
            }

            String controllerFolderName;
            if (hasCustomName())
                controllerFolderName = getName();
            else
                controllerFolderName = "" + getNextID();

            controllerFolder = new File(saveFolder, controllerFolderName);
            if (!controllerFolder.exists()) Files.createDirectory(controllerFolder.toPath());
        }
        catch (IOException e)
        {
            Log.error("An error occurred when creating a save folder.");
            e.printStackTrace();
        }
    }

    public int getNextID() throws IOException
    {
        List<String> lines = Files.readAllLines(nextIDFile.toPath());

        int nextID = 0;

        for (String line : lines)
            nextID = Integer.valueOf(line);

        PrintWriter writer = new PrintWriter(nextIDFile);
        writer.print(nextID + 1);
        writer.close();

        return nextID;
    }

    public ArrayList<Relay> getLinkedRelays()
    {
        return (ArrayList<Relay>) linkedRelays.clone();
    }

    public void inputFound(ItemStack itemStack, long timeFound)
    {
        worldObj.getWorldTime();
    }

    public ConnectionType linkRelay(BlockPos relayPos)
    {
        TileEntity tile = worldObj.getTileEntity(relayPos);
        if (tile instanceof TileRelay)
        {
            Relay newRelay = new Relay(((TileRelay) tile).getName(), relayPos);
//            if (linkedRelays.contains(newRelay))
//                return ConnectionType.ALREADYLINKED;

            for (int i = 0; i < linkedRelays.size(); i++)
            {
                Relay relay = linkedRelays.get(i);
                if (newRelay.getName().equals(relay.getName()) || newRelay.getPos().equals(relay.getPos()))
                {
                    linkedRelays.set(i, newRelay);
                }
                else
                {
                    linkedRelays.add(newRelay);
                }
            }
            markDirtyClient();
            return ConnectionType.LINKED;
        }
        return ConnectionType.NOTRELAY;
    }

    public void unlinkRelay(BlockPos pos)
    {
        int relayIndex = -1;
        for (int i = 0; i < linkedRelays.size(); i++)
        {
            Relay relay = linkedRelays.get(i);
            if (pos.equals(relay.getPos()))
            {
                relayIndex = i;
                break;
            }
        }
        if (relayIndex != -1)
        {
            TileEntity tile = worldObj.getTileEntity(pos);
            if (tile != null && tile instanceof TileRelay)
            {
                ((TileRelay) tile).unlinkController();
            }
            linkedRelays.remove(relayIndex);
            markDirtyClient();
        }
    }

    public void unlinkRelays()
    {
        for (Relay relay : linkedRelays)
        {
            TileEntity tile = worldObj.getTileEntity(relay.getPos());
            if (tile != null && tile instanceof TileRelay)
            {
                ((TileRelay) tile).unlinkController();
            }
        }
        linkedRelays.clear();
        markDirtyClient();
    }

    public void readCustomNBT(NBTTagCompound compound)
    {
        super.readCustomNBT(compound);

        if (compound == null)
            return;

        NBTTagList nbtLinkedRelays = compound.getTagList(TAG_LINKED_RELAYS, 10);

        linkedRelays.clear();
        for (int i = 0; i < nbtLinkedRelays.tagCount(); i++)
        {
            NBTTagCompound nbtRelay = nbtLinkedRelays.getCompoundTagAt(i);
            String name = nbtRelay.getString("id");
            BlockPos pos = new BlockPos(nbtRelay.getInteger("x"),
                                        nbtRelay.getInteger("y"),
                                        nbtRelay.getInteger("z"));
//            linkRelay(pos);
            linkedRelays.add(new Relay(name, pos));
        }
    }

    public void writeCustomNBT(NBTTagCompound compound)
    {
        super.writeCustomNBT(compound);

        NBTTagList nbtLinkedRelays = new NBTTagList();

        for (Relay relay : linkedRelays)
        {
            NBTTagCompound nbtRelay = new NBTTagCompound();
            nbtRelay.setString("id", relay.getName());
            nbtRelay.setInteger("x", relay.getX());
            nbtRelay.setInteger("y", relay.getY());
            nbtRelay.setInteger("z", relay.getZ());
            nbtLinkedRelays.appendTag(nbtRelay);
        }

        compound.setTag(TAG_LINKED_RELAYS, nbtLinkedRelays);
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryplayer, World world, BlockPos pos)
    {
        return new ContainerController(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen createGui(InventoryPlayer inventoryplayer, World world, BlockPos pos)
    {
        return new GuiController(this);
    }
}
