package mimer29or40.productiontimer.common.model;

import net.minecraft.util.math.BlockPos;

public class Relay
{
    private final String name;
    private final BlockPos pos;

    public Relay(String name, BlockPos pos)
    {
        this.name = name;
        this.pos = pos;
    }

    public Relay(String name, int x, int y, int z)
    {
        this(name, new BlockPos(x, y, z));
    }

    public String getName()
    {
        return name;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public int getX()
    {
        return pos.getX();
    }

    public int getY()
    {
        return pos.getY();
    }

    public int getZ()
    {
        return pos.getZ();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof Relay &&
               name.equals(((Relay) o).getName()) &&
               pos.equals(((Relay) o).getPos());
    }
}
