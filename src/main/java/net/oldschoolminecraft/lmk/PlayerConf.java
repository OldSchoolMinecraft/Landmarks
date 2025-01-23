package net.oldschoolminecraft.lmk;

import org.bukkit.util.config.Configuration;

import java.io.File;

public class PlayerConf extends Configuration
{
    public PlayerConf(File file)
    {
        super(file);
        reload();
    }

    public void reload()
    {
        load();
        write();
        save();
    }

    public void write()
    {
        generateConfigOption("sorting.mode", SortingMode.DEFAULT.toString());
    }

    private void generateConfigOption(String key, Object defaultValue)
    {
        if (this.getProperty(key) == null) this.setProperty(key, defaultValue);
        final Object value = this.getProperty(key);
        this.removeProperty(key);
        this.setProperty(key, value);
    }

    public Object getConfigOption(String key)
    {
        return this.getProperty(key);
    }

    public Object getConfigOption(String key, Object defaultValue)
    {
        Object value = getConfigOption(key);
        if (value == null) value = defaultValue;
        return value;
    }
}
