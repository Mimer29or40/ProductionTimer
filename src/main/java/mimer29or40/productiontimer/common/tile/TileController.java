package mimer29or40.productiontimer.common.tile;

import mimer29or40.productiontimer.client.gui.GuiController;
import mimer29or40.productiontimer.client.gui.GuiControllerEntry;
import mimer29or40.productiontimer.common.container.ContainerController;
import mimer29or40.productiontimer.common.container.ContainerEntry;
import mimer29or40.productiontimer.common.model.ConnectionType;
import mimer29or40.productiontimer.common.model.Entry;
import mimer29or40.productiontimer.common.model.Relay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class TileController extends TileMachine
{
    public ArrayList<Relay> linkedRelays      = new ArrayList<>();
    public ArrayList<Entry> entries           = new ArrayList<>();
    public int              selectedEntry     = -1;
    public int              selectedTab       = 0;
    public int              selectedTimeScale = 0;

//    private File saveFolder;
//    private File nextIDFile;
//    public File controllerFolder;
//
//    public void createFolders()
//    {
//        try
//        {
//            saveFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "ProductionTimer");
//            if (!saveFolder.exists()) Files.createDirectory(saveFolder.toPath());
//
//            nextIDFile = new File(saveFolder, "nextID");
//            if (!nextIDFile.exists())
//            {
//                Files.createFile(nextIDFile.toPath());
//                PrintWriter writer = new PrintWriter(nextIDFile);
//                writer.print("0");
//                writer.close();
//            }
//
//            String controllerFolderName;
//            if (hasCustomName())
//                controllerFolderName = getName();
//            else
//                controllerFolderName = "" + getNextID();
//
//            controllerFolder = new File(saveFolder, controllerFolderName);
//            if (!controllerFolder.exists()) Files.createDirectory(controllerFolder.toPath());
//        }
//        catch (IOException e)
//        {
//            Log.error("An error occurred when creating a save folder.");
//            e.printStackTrace();
//        }
//    }
//
//    public int getNextID() throws IOException
//    {
//        List<String> lines = Files.readAllLines(nextIDFile.toPath());
//
//        int nextID = 0;
//
//        for (String line : lines)
//            nextID = Integer.valueOf(line);
//
//        PrintWriter writer = new PrintWriter(nextIDFile);
//        writer.print(nextID + 1);
//        writer.close();
//
//        return nextID;
//    }

//    public TileController()
//    {
////        for (int i = 0; i < 12; i++)
////        {
////            Entry entry = new Entry(this, "Entry " + i);
////            entry.inputRelayName = "Input Relay " + i;
////            entry.outputRelayName = "Output Relay " + i;
////            for (int item = 0; item < 9; item++)
////            {
////                entry.setInputItemStack(item, new ItemStack(Blocks.IRON_BLOCK, 2));
////            }
////            for (int j = 0; j < 9; j++)
////            {
////                entry.setOutputItemStack(j, new ItemStack(Blocks.GOLD_BLOCK, 2));
////            }
////            entries.add(entry);
////        }
////        markDirtyClient();
//    }

    public ConnectionType linkRelay(BlockPos relayPos)
    {
        TileEntity tile = worldObj.getTileEntity(relayPos);
        if (tile instanceof TileRelay)
        {
            Relay newRelay = new Relay(((TileRelay) tile).getName(), relayPos);

            for (int i = 0; i < linkedRelays.size(); i++)
            {
                Relay relay = linkedRelays.get(i);
                if (newRelay.getName().equals(relay.getName()) || newRelay.getPos().equals(relay.getPos()))
                {
                    linkedRelays.set(i, newRelay);
                    markDirtyClient();
                    return ConnectionType.RELAY_OVERWRITTEN;
                }
            }

            linkedRelays.add(newRelay);
            markDirtyClient();
            return ConnectionType.LINKED;
        }
        return ConnectionType.NOT_RELAY;
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

    public void addEntry(Entry entry)
    {
        entries.add(entry);
        markDirtyClient();
    }

    public void removeEntry(int index)
    {
        entries.remove(index);
        markDirtyClient();
    }

    public void readCustomNBT(NBTTagCompound compound)
    {
        super.readCustomNBT(compound);

        if (compound == null)
            return;

        selectedEntry = compound.getInteger("entry");
        selectedTab = compound.getInteger("tab");
        selectedTimeScale = compound.getInteger("timeScale");

        NBTTagList nbtLinkedRelays = compound.getTagList("Relays", 10);
        linkedRelays.clear();
        for (int i = 0; i < nbtLinkedRelays.tagCount(); i++)
        {
            NBTTagCompound nbtRelay = nbtLinkedRelays.getCompoundTagAt(i);
            String name = nbtRelay.getString("id");
            BlockPos pos = new BlockPos(nbtRelay.getInteger("x"), nbtRelay.getInteger("y"), nbtRelay.getInteger("z"));
            linkedRelays.add(new Relay(name, pos));
        }

        NBTTagList nbtEntries = compound.getTagList("Entries", 10);
        entries.clear();
        for (int i = 0; i < nbtEntries.tagCount(); i++)
        {
            NBTTagCompound nbtEntry = nbtEntries.getCompoundTagAt(i);

            Entry entry = new Entry(this);
            entry.name = nbtEntry.getString("name");
            entry.inputRelayName = nbtEntry.getString("inputRelay");
            entry.outputRelayName = nbtEntry.getString("outputRelay");

            NBTTagList nbtEntryInputItems = nbtEntry.getTagList("items", 10);
            for (int j = 0; j < nbtEntryInputItems.tagCount(); j++)
            {
                NBTTagCompound nbtEntryInputItem = nbtEntryInputItems.getCompoundTagAt(j);
                int k = nbtEntryInputItem.getByte("slot") & 255;

                if (0 <= k && k < entry.itemAmount * 2)
                    entry.setInventorySlotContents(k, ItemStack.loadItemStackFromNBT(nbtEntryInputItem));
            }
            entries.add(entry);
        }
    }

    public void writeCustomNBT(NBTTagCompound compound)
    {
        super.writeCustomNBT(compound);

        compound.setInteger("entry", selectedEntry);
        compound.setInteger("tab", selectedTab);
        compound.setInteger("timeScale", selectedTimeScale);

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
        compound.setTag("Relays", nbtLinkedRelays);

        NBTTagList nbtEntries = new NBTTagList();
        for (Entry entry : entries)
        {
            NBTTagCompound nbtEntry = new NBTTagCompound();

            nbtEntry.setString("name", entry.name);
            nbtEntry.setString("inputRelay", entry.inputRelayName);
            nbtEntry.setString("outputRelay", entry.outputRelayName);

            NBTTagList nbtEntryInputItems = new NBTTagList();
            for (int i = 0; i < entry.getSizeInventory(); i++)
            {
                ItemStack stack = entry.getStackInSlot(i);
                if (stack != null)
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("slot", (byte) i);
                    stack.writeToNBT(nbttagcompound);
                    nbtEntryInputItems.appendTag(nbttagcompound);
                }
            }
            nbtEntry.setTag("items", nbtEntryInputItems);

            nbtEntries.appendTag(nbtEntry);
        }
        compound.setTag("Entries", nbtEntries);
    }

    @Override
    public Container createContainer(int id, InventoryPlayer inventoryplayer, World world, BlockPos pos)
    {
        switch (id)
        {
            case 1:
                return new ContainerEntry(this, inventoryplayer, entries.get(selectedEntry));
        }
        return new ContainerController(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen createGui(int id, InventoryPlayer inventoryplayer, World world, BlockPos pos)
    {
        switch (id)
        {
            case 1:
                return new GuiControllerEntry((GuiController) Minecraft.getMinecraft().currentScreen, inventoryplayer, entries.get(selectedEntry));
        }
        return new GuiController(this);
    }
}
