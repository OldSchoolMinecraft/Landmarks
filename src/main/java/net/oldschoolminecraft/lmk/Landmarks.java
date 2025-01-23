package net.oldschoolminecraft.lmk;

import net.oldschoolminecraft.lmk.cmd.Admin;
import net.oldschoolminecraft.lmk.cmd.ChangeSortMode;
import net.oldschoolminecraft.lmk.cmd.ListAndTP;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Landmarks extends JavaPlugin
{
    private LmkManager lmkManager;
    private UpdateManager updateManager;

    public void onEnable()
    {
        updateManager = new UpdateManager(this, "https://micro.os-mc.net/plugin_ci/Landmarks/latest");
        lmkManager = new LmkManager();
        lmkManager.reload();

        getCommand("landmarks").setExecutor(new ListAndTP(this));
        getCommand("lmkadmin").setExecutor(new Admin(this));
        getCommand("lmksort").setExecutor(new ChangeSortMode(this));

        System.out.println("Landmarks enabled");
    }

    public void onDisable()
    {
        updateManager.checkForUpdates();

        System.out.println("Landmarks disabled");
    }

    public PlayerConf getPlayerConfig(String name)
    {
        File playerConfDir = new File(getDataFolder(), "player-conf/");
        return new PlayerConf(new File(playerConfDir, name.toLowerCase() + ".json"));
    }

    public File getPluginFile()
    {
        return getFile();
    }

    public LmkManager getLmkManager()
    {
        return lmkManager;
    }
}
