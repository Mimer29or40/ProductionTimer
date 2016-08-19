package mimer29or40.productiontimer.common.model;

import mimer29or40.productiontimer.common.tile.TileController;
import mimer29or40.productiontimer.common.util.Log;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

public class DataManager
{
    private static File baseFolder;
    private static File nextIDFile;

    public static void init()
    {
        try
        {
            baseFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "ProductionTimer");
            if (!baseFolder.exists()) Files.createDirectory(baseFolder.toPath());

            nextIDFile = new File(baseFolder, "nextID");
            if (!nextIDFile.exists()) Files.createFile(nextIDFile.toPath());
        }
        catch (IOException e)
        {
            Log.error("An error occurred when creating save folder");
            e.printStackTrace();
        }
    }

    public static void initControllerFolder(TileController tileController)
    {

    }

    private static int getNextID() throws IOException
    {
        List<String> lines = Files.readAllLines(nextIDFile.toPath());

        if (lines.isEmpty())
        {
            PrintWriter writer = new PrintWriter(nextIDFile);
            writer.print("1");
            writer.close();
            return 0;
        }
        int nextID = 0;
        for (String line : lines)
        {
            nextID = Integer.valueOf(line) + 1;
            PrintWriter writer = new PrintWriter(nextIDFile);
            writer.print(nextID);
            writer.close();
        }
        return nextID;
    }

    public static File getBaseFolder()
    {
        return baseFolder;
    }

    public static File getNextIDFile()
    {
        return nextIDFile;
    }

    private DataManager() {}
}
